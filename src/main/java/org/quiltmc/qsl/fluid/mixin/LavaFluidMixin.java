package org.quiltmc.qsl.fluid.mixin;

import org.quiltmc.qsl.fluid.fluid.FlowableFluidExtensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(LavaFluid.class)
public abstract class LavaFluidMixin extends FlowableFluid implements FlowableFluidExtensions {
	private static final int lavaColor = 0x991900;
	
	@Override
	public float defaultTemperature(World world, BlockPos blockPos) {
		return LAVA_TEMPERATURE;
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
	
	@Override
	public boolean canExtinguish(FluidState state, Entity effected) {
		return false;
	}
	
	@Override
	public int getColor(FluidState state, World world, BlockPos pos) {
		return lavaColor;
	}
	
	@Override
	public boolean canSprintSwim(FluidState state, Entity affected) {
		return false;
	}
	
	@Override
	public float modifyHorizontalViscosity(LivingEntity affected, float horizontalViscosity) {
		return horizontalViscosity;
	}
	
	@Override
	public boolean enableSpacebarSwimming(FluidState state, Entity affected) {
		return true;
	}
	
	@Override
	public void drownEffects(FluidState state, LivingEntity drowning, Random random) {
	}

	@Override
	public int getFogColor(FluidState state, Entity affected) {
		return lavaColor;
	}
	
	@Override
	public SoundEvent splashSound(Entity splashing, Vec3d splashPos, Random random) {
		return null;
	}
	
	@Override
	public SoundEvent highSpeedSplashSound(Entity splashing, Vec3d splashPos, Random random) {
		return null;
	}
	
	@Override
	public ParticleEffect splashParticle(Entity splashing, Vec3d splashPos, Random random) {
		return null;
	}
	
	@Override
	public ParticleEffect bubbleParticle(Entity splashing, Vec3d splashPos, Random random) {
		return null;
	}
	
	@Override
	public GameEvent splashGameEvent(Entity splashing, Vec3d splashPos, Random random) {
		return null;
	}
	
	@Override
	public void spawnSplashParticles(Entity splashing, Vec3d splashPos, Random random) {
	}
	
	@Override
	public void spawnBubbleParticles(Entity splashing, Vec3d splashPos, Random random) {
	}
	
	@Override
	public void onSplash(World world, Vec3d pos, Entity splashing, Random random) {
	}
	
	@Override
	public float[] customEnchantmentEffects(Vec3d movementInput, LivingEntity entity, float horizontalViscosity, float speed) {
		return new float[]{horizontalViscosity, speed};
	}
}
