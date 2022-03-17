package org.quiltmc.qsl.fluid.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
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

    @ModifyReceiver(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/tag/TagKey;)Z"))
    private FluidState checkForCustomFluid(FluidState receiver, TagKey fluidTag) {
        if (receiver.isIn(QuiltFluidAPI.QUILT_FLUIDS)) {
            return Fluids.WATER.getDefaultState();
        }
        return receiver;
    }

}
