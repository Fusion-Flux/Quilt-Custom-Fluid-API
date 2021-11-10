package net.fabricmc.example.mixin;

import net.fabricmc.example.fluid.FlowableFluidExtensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(WaterFluid.class)
public abstract class WaterFluidMixin extends FlowableFluid implements FlowableFluidExtensions {
	@Override
	public float defaultTemperature(World world, BlockPos blockPos) {
		return WATER_TEMPERATURE;
	}
	
	@Override
	public float defaultDensity(World world, BlockPos blockPos) {
		return WATER_DENSITY;
	}
	
	@Override
	public float getHorizontalViscosity(FluidState state, Entity effected) {
		return WATER_VISCOSITY;
	}
	
	@Override
	public float getVerticalViscosity(FluidState state, Entity effected) {
		return WATER_VISCOSITY;
	}
	
	@Override
	public float getPushStrength(FluidState state, Entity effected) {
		return WATER_PUSH_STRENGTH;
	}
	
	@Override
	public int getColor(FluidState state, World world, BlockPos pos) {
		return WATER_COLOR;
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
	public boolean canExtinguish(FluidState state, Entity affected) {
		return FlowableFluidExtensions.super.canExtinguish(state, affected);
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
	public float fallDamageReduction(Entity entity) {
		return FlowableFluidExtensions.super.fallDamageReduction(entity);
	}
	
	@Override
	public int getFogColor(FluidState state, Entity affected) {
		return FlowableFluidExtensions.super.getFogColor(state, affected);
	}
	
	@Override
	public float getFogStart(FluidState state, Entity affected, float viewDistance) {
		return FlowableFluidExtensions.super.getFogStart(state, affected, viewDistance);
	}
	
	@Override
	public float getFogEnd(FluidState state, Entity affected, float viewDistance) {
		return FlowableFluidExtensions.super.getFogEnd(state, affected, viewDistance);
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
