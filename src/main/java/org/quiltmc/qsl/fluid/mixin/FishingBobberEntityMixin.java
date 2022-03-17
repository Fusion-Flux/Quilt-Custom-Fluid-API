package org.quiltmc.qsl.fluid.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import org.quiltmc.qsl.fluid.QuiltFluidAPI;
import org.quiltmc.qsl.fluid.fluid.FlowableFluidExtensions;
import org.quiltmc.qsl.fluid.interfaces.CustomFluidInteracting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin implements CustomFluidInteracting {

    @ModifyVariable(
            method = "tick",
            at = @At(value = "STORE", ordinal = 0)
    )
    public float customFluidCheck(float f) {
        BlockPos blockPos = ((FishingBobberEntity) (Object) this).getBlockPos();
        FluidState fluidState = ((FishingBobberEntity) (Object) this).world.getFluidState(blockPos);
        if ((fluidState.getFluid() instanceof FlowableFluidExtensions fluid)) {
            if (fluidState.isIn(QuiltFluidAPI.QUILT_FLUIDS) && fluid.bobberFloats(fluidState, (Entity) ((FishingBobberEntity) (Object) this))) {
                return fluidState.getHeight(((FishingBobberEntity) (Object) this).world, blockPos);
            }
        }
        return f;
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/tag/TagKey;)Z", ordinal = 1))
    private boolean onlyIfInNoFluid(boolean original) {
        BlockPos blockPos = ((FishingBobberEntity) (Object) this).getBlockPos();
        FluidState fluidState = ((FishingBobberEntity) (Object) this).world.getFluidState(blockPos);
        return original || fluidState.isIn(QuiltFluidAPI.QUILT_FLUIDS);
    }
}
