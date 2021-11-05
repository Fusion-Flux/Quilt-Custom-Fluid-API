package net.fabricmc.example.fluid;

import net.fabricmc.example.ExampleMod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import org.jetbrains.annotations.Nullable;

public class OilFluid extends TutorialFluid {
    @Override
    public Fluid getStill() {
        return ExampleMod.STILL_OIL;
    }

    @Override
    public int getLevel(FluidState state) {
        return 0;
    }

    @Override
    public Fluid getFlowing() {
        return ExampleMod.FLOWING_OIL;
    }

    @Override
    public Item getBucketItem() {
        return ExampleMod.OIL_BUCKET;
    }

    @Override
    protected BlockState toBlockState(FluidState fluidState) {
        return ExampleMod.OIL.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }

    @Override
    public boolean isStill(FluidState state) {
        return false;
    }

    @Override
    public float getHorizontalViscosity(FluidState state, Entity entity) {
        return 0.5f;
    }

    @Override
    public float getVerticalViscosity(FluidState state, Entity entity) {
        return 0.3f; //the default value for this is 0.800000011920929D its highly reccomended you dont change this
    }

    @Override
    public float getPushStrength(FluidState state, Entity entity) {
        return 0.0014f;
    }

    @Override
    public boolean canSwimIn(FluidState state, Entity entity) {
        return true;
    }

    @Override
    public boolean enableDepthStrider(FluidState state, Entity entity) {
        return false;
    }

    @Override
    public boolean enableDolphinsGrace(FluidState state, Entity entity) {
        return false;
    }

    @Nullable
    public ParticleEffect getParticle() {
        return ParticleTypes.DRIPPING_HONEY;
    }

    public static class Flowing extends OilFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return fluidState.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return false;
        }
    }

    public static class Still extends OilFluid {
        @Override
        public int getLevel(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return true;
        }
    }
}
