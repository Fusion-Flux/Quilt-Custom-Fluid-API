package net.fabricmc.example.mixin;

import net.fabricmc.example.fluid.FlowableFluidExtensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
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
	// just need to implement the interface, defaults are based on water
    private static final int waterColor = 0x991900;

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
    public float fallDamageReduction(Entity entity) {
        return FULL_FALL_DAMAGE_REDUCTION;
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
    public boolean canExtinguish(FluidState state, Entity effected) {
        return false;
    }

    @Override
    public int getColor(FluidState state, World world, BlockPos pos) {
        return waterColor;
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
        return WATER_FOG_COLOR;
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
