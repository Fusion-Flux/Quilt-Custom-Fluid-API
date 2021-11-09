package net.fabricmc.example.mixin;

import net.fabricmc.example.fluid.FlowableFluidExtensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LavaFluid.class)
public abstract class LavaFluidMixin extends FlowableFluid implements FlowableFluidExtensions {
	@Override
	public float defaultTemperature(World world, BlockPos blockPos) {
		return LAVA_DEFAULT_TEMP;
	}

	@Override
	public float defaultDensity(World world, BlockPos blockPos) {
		return LAVA_DENSITY;
	}

	@Override
	public float getHorizontalViscosity(FluidState state, Entity effected) {
		return LAVA_VISCOSITY;
	}

	@Override
	public float getVerticalViscosity(FluidState state, Entity effected) {
		return WATER_VISCOSITY;
	}

	@Override
	public float getPushStrength(FluidState state, Entity effected) {
		return effected.world.getDimension().isUltrawarm() ? LAVA_PUSH_STRENGTH_ULTRAWARM : LAVA_PUSH_STRENGTH_OVERWORLD;
	}

	@Override
	public boolean canExtinguish(Entity effected) {
		return false;
	}

	@Override
	public float fallDamageReduction(Entity entity) {
		return HALF_FALL_DAMAGE_REDUCTION;
	}

	@Override
	public float getFogStart(FluidState state, Entity affected, float viewDistance) {
		if (affected.isSpectator()) {
			return -8.0f;
		} else if (affected instanceof LivingEntity living && living.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
			return 0.0f;
		} else {
			return 0.25f;
		}
	}

	@Override
	public float getFogEnd(FluidState state, Entity affected, float viewDistance) {
		if (affected.isSpectator()) {
			return viewDistance / 2;
		} else if (affected instanceof LivingEntity living && living.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
			return 3.0f;
		} else {
			return 1.0f;
		}
	}
}
