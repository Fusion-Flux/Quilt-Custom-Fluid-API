package net.fabricmc.example.fluid;

import com.google.common.base.Objects;
import net.fabricmc.example.ExampleMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;

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
        return 0.800000011920929f; //the default value for this is 0.800000011920929D its highly reccomended you dont change this
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
    public int getFogColor(Entity focusedEntity) {
        //Set the fog color to #99ff33 for a light green acid.
        return 0x99ff33;
    }

    @Override
    public float getFogStart(Entity focusedEntity) {
        //You can use focusedEntity to get the effects, just comment for now.
        //if (entity instanceof LivingEntity && ((LivingEntity)entity).hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) return 0.0f;
        return -8.0f;
    }

    @Override
    public float getFogEnd(Entity focusedEntity) {
        //You can use focusedEntity to get the effects, just comment for now.
        //if (entity instanceof LivingEntity && ((LivingEntity)entity).hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) return 3.0f

        return 96.0f;
    }

    @Override
    public void onSplash(World world, Vec3d pos, Entity entity, Random random) {
        Entity entity2 = entity.hasPassengers() && entity.getPrimaryPassenger() != null ? entity.getPrimaryPassenger() : entity;
        float f = entity2 == entity ? 0.2F : 0.9F;
        Vec3d vec3d = entity2.getVelocity();
        float g = Math.min(1.0F, (float)Math.sqrt(vec3d.x * vec3d.x * 0.20000000298023224D + vec3d.y * vec3d.y + vec3d.z * vec3d.z * 0.20000000298023224D) * f);
        if (g < 0.25F) {
            //A low velocity impact with a fluid
            entity.playSound(SoundEvents.ENTITY_PLAYER_SPLASH, g, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.4F);
        } else {
            //A high velocity impact with a fluid
            entity.playSound(SoundEvents.ENTITY_PLAYER_SPLASH_HIGH_SPEED, g, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.4F);
        }

        float h = (float) MathHelper.floor(entity.getY());

        int j;
        double k;
        double l;
        //bubble particles
        for(j = 0; (float)j < 1.0F + entity.getDimensions(entity.getPose()).width * 20.0F; ++j) {
            k = (random.nextDouble() * 2.0D - 1.0D) * (double)entity.getDimensions(entity.getPose()).width;
            l = (random.nextDouble() * 2.0D - 1.0D) * (double)entity.getDimensions(entity.getPose()).width;
            entity.world.addParticle(ParticleTypes.BUBBLE, entity.getX() + k, (double)(h + 1.0F), entity.getZ() + l, vec3d.x, vec3d.y - random.nextDouble() * 0.20000000298023224D, vec3d.z);
        }
        //water droplet splash particles
        for(j = 0; (float)j < 1.0F + entity.getDimensions(entity.getPose()).width * 20.0F; ++j) {
            k = (random.nextDouble() * 2.0D - 1.0D) * (double)entity.getDimensions(entity.getPose()).width;
            l = (random.nextDouble() * 2.0D - 1.0D) * (double)entity.getDimensions(entity.getPose()).width;
            entity.world.addParticle(ParticleTypes.SPLASH, entity.getX() + k, (double)(h + 1.0F), entity.getZ() + l, vec3d.x, vec3d.y, vec3d.z);
        }

        entity.emitGameEvent(GameEvent.SPLASH);
    }

    @Override
    public float[] customEnchantmentEffects(Vec3d movementInput, LivingEntity entity,float horizVisc,float g) {
        float[] j = new float[2];
        float h = (float) EnchantmentHelper.getDepthStrider(entity);
        if (h > 3.0F) {
            h = 3.0F;
        }

        if (!entity.isOnGround()) {
            h *= 0.5F;
        }

        if (h > 0.0F) {
            horizVisc += (0.54600006F - horizVisc) * h / 3.0F;
            g += (entity.getMovementSpeed() - g) * h / 3.0F;
        }
        j[0]=horizVisc;
        j[1]=g;
        return(j);
    }

    @Override
    public boolean enableSpacebarSwimming(Entity entity){
        return true;
    }

    @Override
    public void drownEffects(LivingEntity entity, Random random) {
        boolean bl = entity instanceof PlayerEntity;
        boolean bl2 = bl && ((PlayerEntity)entity).getAbilities().invulnerable;
                if (!entity.canBreatheInWater() && !StatusEffectUtil.hasWaterBreathing(entity) && !bl2) {
                    entity.setAir(getNextAirSubmerged(entity.getAir(),entity,random));
                    if (entity.getAir() == -20) {
                        entity.setAir(0);
                        Vec3d vec3d = entity.getVelocity();

                        for(int i = 0; i < 8; ++i) {
                            double f = random.nextDouble() - random.nextDouble();
                            double g = random.nextDouble() - random.nextDouble();
                            double h = random.nextDouble() - random.nextDouble();
                            entity.world.addParticle(ParticleTypes.BUBBLE, entity.getX() + f, entity.getY() + g, entity.getZ() + h, vec3d.x, vec3d.y, vec3d.z);
                        }

                        entity.damage(DamageSource.DROWN, 2.0F);
                    }
                }
                if (!entity.world.isClient && entity.hasVehicle() && entity.getVehicle() != null && !entity.getVehicle().canBeRiddenInWater()) {
                    entity.stopRiding();
                }
            }

            @Override
            public float defaultTemperature(World world, BlockPos blockpos) {
                return 300;
            }

    @Override
    public float defaultDensity(World world, BlockPos blockpos) {
        return 1000;
    }
    @Override
    public boolean canExtinguish(Entity entity) {
        return false;
    }
    @Override
    public int getNextAirSubmerged(int air,LivingEntity entity, Random random) {
        int i = EnchantmentHelper.getRespiration(entity);
        return i > 0 && random.nextInt(i + 1) > 0 ? air : air - 2;
    }


    @Override
    public float customPotionEffects(LivingEntity entity, float horizVisc) {
        if (entity.hasStatusEffect(StatusEffects.DOLPHINS_GRACE)) {
            horizVisc = 0.96F;
        }
        return horizVisc;
    }

    public Direction getFlowAxis(){
        return Direction.NORTH;
    };

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
