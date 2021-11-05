package net.fabricmc.example.mixin;


import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.fabricmc.example.ExampleMod;
import net.fabricmc.example.fluid.FlowableFluidExtensions;
import net.fabricmc.example.interfaces.CustomFluidInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements CustomFluidInterface {


    @Shadow
    public abstract boolean equals(Object o);


    @Shadow
    @Nullable
    public abstract Entity getVehicle();


    @Shadow
    public abstract boolean updateMovementInFluid(Tag<Fluid> tag, double d);

    @Shadow
    protected boolean firstUpdate;

    @Shadow
    protected abstract void onSwimmingStart();

    @Shadow
    public float fallDistance;

    @Shadow
    public abstract void extinguish();


    @Shadow
    protected Object2DoubleMap<Tag<Fluid>> fluidHeight;


    @Shadow
    @Nullable
    protected Tag<Fluid> submergedFluidTag;

    @Shadow
    public abstract double getEyeY();

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getZ();

    @Shadow
    public World world;

    @Shadow
    public abstract BlockPos getBlockPos();

    @Shadow
    public abstract boolean isTouchingWater();

    @Shadow
    public abstract boolean isSprinting();

    @Shadow
    public abstract void setSwimming(boolean swimming);

    @Shadow
    public abstract boolean isSwimming();

    @Shadow
    public abstract boolean hasVehicle();

    @Shadow
    public abstract boolean isSubmergedInWater();

    @Shadow
    private BlockPos blockPos;
    protected boolean inCustomFluid;

    protected boolean submergedInCustomFluid;

    protected Tag<Fluid> submergedCustomFluidTag;

    @Inject(method = "baseTick", at = @At("TAIL"), cancellable = true)
    public void baseTick(CallbackInfo ci) {
        this.checkCustomFluidState();
        this.updateSubmergedInCustomFluidState();
        //this.updateSwimming(); do later
    }

    @Override
    public boolean isInCustomFluid() {
        return this.inCustomFluid;
    }


    @Override
    public boolean isSubmergedInCustomFluid() {
        return this.submergedInCustomFluid && this.isInCustomFluid();
    }



    void checkCustomFluidState() {
        FluidState fluidState = this.world.getFluidState(this.getBlockPos());
        if (fluidState.getFluid() instanceof FlowableFluidExtensions fluid) {
            if (this.getVehicle() instanceof BoatEntity) {
                System.out.println("False A");
                this.inCustomFluid = false;
            } else if (this.updateMovementInFluid(ExampleMod.FABRIC_FLUIDS, fluid.getPushStrength(fluidState,((Entity) (Object) this)))) {
                if (!this.inCustomFluid && !this.firstUpdate) {
                    this.onSwimmingStart();
                }

                this.fallDistance = 0.0F;
                this.inCustomFluid = true;
                this.extinguish();
            } else {
                System.out.println("False B");
                this.inCustomFluid = false;
            }
        } else {

            this.inCustomFluid = false;
        }
    }

    private void updateSubmergedInCustomFluidState() {
        this.submergedInCustomFluid = this.isSubmergedInCustom(ExampleMod.FABRIC_FLUIDS);
        this.submergedCustomFluidTag = null;
        double d = this.getEyeY() - 0.1111111119389534D;
        Entity entity = this.getVehicle();
        if (entity instanceof BoatEntity) {
            BoatEntity boatEntity = (BoatEntity) entity;
            if (!boatEntity.isSubmergedInWater() && boatEntity.getBoundingBox().maxY >= d && boatEntity.getBoundingBox().minY <= d) {
                return;
            }
        }

        BlockPos blockPos = new BlockPos(this.getX(), d, this.getZ());
        FluidState fluidState = this.world.getFluidState(blockPos);


        double e = (double) ((float) blockPos.getY() + fluidState.getHeight(this.world, blockPos));
        if (e > d) {
            this.submergedCustomFluidTag = ExampleMod.FABRIC_FLUIDS;
        }

    }

    public boolean isSubmergedInCustom(Tag<Fluid> fluidTag) {
        return this.submergedCustomFluidTag == fluidTag;
    }



    @Inject(method = "updateSwimming", at = @At("TAIL"), cancellable = true)
    public void updateSwimming(CallbackInfo ci) {
        boolean canSwimIn = false;
        if (this.isInCustomFluid()) {
            FluidState fluidState = this.world.getFluidState(this.getBlockPos());
            if (fluidState.getFluid() instanceof FlowableFluidExtensions fluid) {
                canSwimIn = fluid.canSwimIn(fluidState, ((Entity) (Object) this));
            }
            if (this.isSwimming()) {
                this.setSwimming(this.isSprinting() &&canSwimIn && this.isInCustomFluid() && !this.hasVehicle());
            } else {
                this.setSwimming(this.isSprinting() && this.isSubmergedInCustomFluid()&& canSwimIn && !this.hasVehicle() && this.world.getFluidState(this.blockPos).isIn(ExampleMod.FABRIC_FLUIDS));
            }

        }
    }
}
