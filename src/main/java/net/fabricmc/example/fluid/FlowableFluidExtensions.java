package net.fabricmc.example.fluid;

import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;

public interface FlowableFluidExtensions {

    float getHorizontalViscosity(FluidState state, Entity entity);

    float getVerticalViscosity(FluidState state, Entity entity);

    float getPushStrength(FluidState state, Entity entity);

    boolean canSwimIn(FluidState state, Entity entity);

    boolean enableDepthStrider(FluidState state, Entity entity);

    boolean enableDolphinsGrace(FluidState state, Entity entity);

}
