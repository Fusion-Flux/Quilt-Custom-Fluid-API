package org.quiltmc.qsl.fluid.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.quiltmc.qsl.fluid.QuiltFluidAPI;
import org.quiltmc.qsl.fluid.fluid.FlowableFluidExtensions;
import org.quiltmc.qsl.fluid.interfaces.CustomFluidInteracting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity implements CustomFluidInteracting {
    @Shadow
    private double waterLevel;

    @Shadow
    private BoatEntity.Location location;

    public BoatEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }


    @ModifyReceiver(
            method = "method_7544",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/tag/TagKey;)Z")
    )
    private FluidState changeObject(FluidState receiver, TagKey fluidTag) {
        if (receiver.isIn(QuiltFluidAPI.QUILT_FLUIDS)) {
            return Fluids.WATER.getDefaultState();
        }
        return receiver;
    }

    @ModifyReceiver(
            method = "checkBoatInWater",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/tag/TagKey;)Z")
    )
    private FluidState checkFluidModify(FluidState receiver, TagKey fluidTag) {
        if (receiver.isIn(QuiltFluidAPI.QUILT_FLUIDS)) {
            return Fluids.WATER.getDefaultState();
        }
        return receiver;
    }

    @ModifyReceiver(
            method = "getUnderWaterLocation",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/tag/TagKey;)Z")
    )
    private FluidState getUnderwaterModify(FluidState receiver, TagKey fluidTag) {
        if (receiver.isIn(QuiltFluidAPI.QUILT_FLUIDS)) {
            return Fluids.WATER.getDefaultState();
        }
        return receiver;
    }

    @Inject(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getFluidState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/fluid/FluidState;"), cancellable = true)
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition, CallbackInfo ci) {
        if (!this.world.getFluidState(this.getBlockPos().down()).isIn(QuiltFluidAPI.QUILT_FLUIDS) && heightDifference < 0.0) {
            this.fallDistance -= (float) heightDifference;
        }
        if (this.world.getFluidState(this.getBlockPos().down()).isIn(QuiltFluidAPI.QUILT_FLUIDS)) {
            ci.cancel();
        }
    }

    @Inject(method = "updateVelocity", at = @At(value = "TAIL"))
    private void updateVelocity(CallbackInfo ci) {
        if(this.isInCustomFluid()){
            FluidState fluidState = world.getFluidState(getBlockPos());
            float horizVisc = 0.8f;
            float vertVisc = 0.8f;

            if ((fluidState.getFluid() instanceof FlowableFluidExtensions fluid)) {
                horizVisc = this.isSprinting() ? 0.9f : fluid.getHorizontalViscosity(fluidState, this);
                vertVisc = fluid.getVerticalViscosity(fluidState, this);
            }
            Vec3d vec3d = this.getVelocity();
            this.setVelocity(vec3d.multiply(horizVisc/.8, vertVisc/.8, horizVisc/.8));
        }
    }

    @Override
    public boolean isSubmergedInCustomFluid() {
        return this.location == BoatEntity.Location.UNDER_WATER || this.location == BoatEntity.Location.UNDER_FLOWING_WATER;
    }

    }
