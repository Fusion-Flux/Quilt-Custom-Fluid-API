package org.quiltmc.qsl.fluid.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.quiltmc.qsl.fluid.QuiltFluidAPI;
import org.quiltmc.qsl.fluid.interfaces.CustomFluidInteracting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity implements CustomFluidInteracting {
    @Shadow private double waterLevel;

    @Shadow private BoatEntity.Location location;

    public BoatEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    // just need to implement the interface, defaults are based on water
    @Inject(method = "method_7544", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/tag/TagKey;)Z"),locals = LocalCapture.CAPTURE_FAILSOFT)
    public void method_7544(CallbackInfoReturnable<Float> cir, Box box,int i,int j,int k, int l, int m, int n, BlockPos.Mutable mutable,int o,float f,int p ,int q,FluidState fluidState) {
        if (fluidState.isIn(QuiltFluidAPI.QUILT_FLUIDS)) {
            f = Math.max(f, fluidState.getHeight(this.world, mutable));//needs a modifyvariable
        }
    }


    @Inject(method = "checkBoatInWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/tag/TagKey;)Z"),locals = LocalCapture.CAPTURE_FAILSOFT,cancellable = true)
    private void checkBoatInWater(CallbackInfoReturnable<Boolean> cir, Box box,int i,int j,int k, int l, int m, int n, boolean bl, BlockPos.Mutable mutable, int o, int p, int q, FluidState fluidState) {
        if (fluidState.isIn(QuiltFluidAPI.QUILT_FLUIDS)) {
            float f = (float)p + fluidState.getHeight(this.world, mutable);
            this.waterLevel = Math.max((double)f, this.waterLevel);
            bl |= box.minY < (double)f;//needs a modifyvariable
        }
    }

    @Inject(method = "getUnderWaterLocation", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/tag/TagKey;)Z"),locals = LocalCapture.CAPTURE_FAILSOFT,cancellable = true)
    private void getUnderWaterLocation(CallbackInfoReturnable<BoatEntity.Location> cir,Box box,double d,int i,int j,int k,int l,int m,int n,boolean bl,BlockPos.Mutable mutable,int o,int p,int q,FluidState fluidState) {
        if (fluidState.isIn(QuiltFluidAPI.QUILT_FLUIDS) && d < (double)((float)mutable.getY() + fluidState.getHeight(this.world, mutable))) {
            if (!fluidState.isStill()) {
                cir.setReturnValue(BoatEntity.Location.UNDER_FLOWING_WATER);
                cir.cancel();
            }
            bl = true;//needs a modifyvariable
        }
    }

    @Inject(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getFluidState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/fluid/FluidState;"), cancellable = true)
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition, CallbackInfo ci) {
        if (!this.world.getFluidState(this.getBlockPos().down()).isIn(QuiltFluidAPI.QUILT_FLUIDS) && heightDifference < 0.0) {
            this.fallDistance -= (float)heightDifference;
        }
        if (this.world.getFluidState(this.getBlockPos().down()).isIn(QuiltFluidAPI.QUILT_FLUIDS)) {
            ci.cancel();
        }
    }


    @Override
    public boolean isSubmergedInCustomFluid() {
        return this.location == BoatEntity.Location.UNDER_WATER || this.location == BoatEntity.Location.UNDER_FLOWING_WATER;
    }

    }
