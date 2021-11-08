package net.fabricmc.example.fluid;

import net.fabricmc.example.ExampleMod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class OilFluid extends TutorialFluid implements FlowableFluidExtensions {
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
    public float getHorizontalViscosity(Entity entity) {
        return 0.5f;
    }

    @Override
    public float getVerticalViscosity(Entity entity) {
        return 0.3f; //the default value for this is 0.800000011920929D its highly reccomended you dont change this
    }

    @Override
    public float getPushStrength(Entity entity) {
        return 0.0014f;
    }

    @Override
    public boolean canSwimIn(Entity entity) {
        return true;
    }

    @Override
    public boolean enableDepthStrider(Entity entity) {
        return false;
    }

    @Override
    public boolean enableDolphinsGrace(Entity entity) {
        return false;
    }

    @Override
    public int getFogColor(Entity focusedEntity) {
        //Set the fog color to #99ff33 for a light green acid.
        return 0x99ff33;
    }

    @Override
    public float getFogStart(Entity focusedEntity) {
        //You can use focusedEntity to get the effects, just comment for now.
        //if (entity instanceof LivingEntity && ((LivingEntity)entity).hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) return 0.0f;
        return 0.25f;
    }

    @Override
    public float getFogEnd(Entity focusedEntity) {
        //You can use focusedEntity to get the effects, just comment for now.
        //if (entity instanceof LivingEntity && ((LivingEntity)entity).hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) return 3.0f
        return 1.0f;
    }

    @Override
    public Optional<SoundEvent> getSplashSound() {
        //For this example we will use the strider step sound in lava.
        return Optional.of(SoundEvents.ENTITY_STRIDER_STEP_LAVA);
    }

    @Override
    public void onSplash(World world, Vec3d pos, Entity entity) {
        //You can use the parameters in this method to add the particles you want.
        //This is an example that will show a smoke particle when hitting the fluid (or jumping on it).
        //pos is the position where the player hitted the fluid.
        //entity is the entity that caused the splash event.
        world.addParticle(ParticleTypes.SMOKE, pos.getX(), pos.getY(), pos.getZ(), 0.02d, 0.02d, 0.02d);
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
