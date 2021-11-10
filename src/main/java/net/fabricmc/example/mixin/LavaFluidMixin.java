package net.fabricmc.example.mixin;

import net.fabricmc.example.fluid.FlowableFluidExtensions;
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
		return LAVA_COLOR;
	}
	
	@Override
	public boolean canSprintSwim(FluidState state, Entity affected) {
		return FlowableFluidExtensions.super.canSprintSwim(state, affected);
	}
	
	@Override
	public float modifyHorizontalViscosity(LivingEntity affected, float horizontalViscosity) {
		return FlowableFluidExtensions.super.modifyHorizontalViscosity(affected, horizontalViscosity);
	}
	
	@Override
	public boolean enableSpacebarSwimming(FluidState state, Entity affected) {
		return FlowableFluidExtensions.super.enableSpacebarSwimming(state, affected);
	}
	
	@Override
	public boolean canIgnite(FluidState state, Entity affected) {
		return FlowableFluidExtensions.super.canIgnite(state, affected);
	}
	
	@Override
	public void drownEffects(FluidState state, LivingEntity drowning, Random random) {
		FlowableFluidExtensions.super.drownEffects(state, drowning, random);
	}
	
	@Override
	public int getNextAirSubmerged(int air, LivingEntity entity, Random random) {
		return FlowableFluidExtensions.super.getNextAirSubmerged(air, entity, random);
	}
	
	@Override
	public int getFogColor(FluidState state, Entity affected) {
		return FlowableFluidExtensions.super.getFogColor(state, affected);
	}
	
	@Override
	public SoundEvent splashSound(Entity splashing, Vec3d splashPos, Random random) {
		return FlowableFluidExtensions.super.splashSound(splashing, splashPos, random);
	}
	
	@Override
	public SoundEvent highSpeedSplashSound(Entity splashing, Vec3d splashPos, Random random) {
		return FlowableFluidExtensions.super.highSpeedSplashSound(splashing, splashPos, random);
	}
	
	@Override
	public ParticleEffect splashParticle(Entity splashing, Vec3d splashPos, Random random) {
		return FlowableFluidExtensions.super.splashParticle(splashing, splashPos, random);
	}
	
	@Override
	public ParticleEffect bubbleParticle(Entity splashing, Vec3d splashPos, Random random) {
		return FlowableFluidExtensions.super.bubbleParticle(splashing, splashPos, random);
	}
	
	@Override
	public GameEvent splashGameEvent(Entity splashing, Vec3d splashPos, Random random) {
		return FlowableFluidExtensions.super.splashGameEvent(splashing, splashPos, random);
	}
	
	@Override
	public void spawnSplashParticles(Entity splashing, Vec3d splashPos, Random random) {
		FlowableFluidExtensions.super.spawnSplashParticles(splashing, splashPos, random);
	}
	
	@Override
	public void spawnBubbleParticles(Entity splashing, Vec3d splashPos, Random random) {
		FlowableFluidExtensions.super.spawnBubbleParticles(splashing, splashPos, random);
	}
	
	@Override
	public void onSplash(World world, Vec3d pos, Entity splashing, Random random) {
		FlowableFluidExtensions.super.onSplash(world, pos, splashing, random);
	}
	
	@Override
	public float[] customEnchantmentEffects(Vec3d movementInput, LivingEntity entity, float horizontalViscosity, float speed) {
		return FlowableFluidExtensions.super.customEnchantmentEffects(movementInput, entity, horizontalViscosity, speed);
	}
}
