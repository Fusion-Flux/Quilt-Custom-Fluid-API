package net.fabricmc.example.fluid;

import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;

public interface FlowableFluidExtensions {

    float getHorizontalViscosity(Entity entity);

    float getVerticalViscosity(Entity entity);

    float getPushStrength(Entity entity);

    boolean canSwimIn(Entity entity);

    boolean enableDepthStrider(Entity entity);

    boolean enableDolphinsGrace(Entity entity);

    int getFogColor(Entity entity);

    float getFogStart(Entity entity);

    float getFogEnd(Entity entity);

    Optional<SoundEvent> getSplashSound();

    void onSplash(World world, Vec3d pos, Entity entity);
}


