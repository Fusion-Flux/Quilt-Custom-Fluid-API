package net.fabricmc.example.fluid;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Optional;
import java.util.Random;

public interface FlowableFluidExtensions {

    default float getHorizontalViscosity(Entity entity){
        //0.8F is the default for water
        //0.5F is the default for lava
        return 0.8F;
    }

    default float getVerticalViscosity(Entity entity){
        //Its not reccomended you change this
        return 0.800000011920929F;
    }

    default float getPushStrength(Entity entity){
        //0.014F is the default for water
        //0.0023333333333333335F is the default for lava in the overworld
        //0.007F is the default for lava in the nether
        return 0.014F;
    }

    default boolean canSwimIn(Entity entity){
        //Toggles weather or not a player can sprint swim in your fluid
        return true;
    }

    int getFogColor(Entity entity);

    float getFogStart(Entity entity);

    float getFogEnd(Entity entity);

    default void onSplash(World world, Vec3d pos, Entity entity, Random random){
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

    default float[] customEnchantmentEffects(Vec3d movementInput, LivingEntity entity,float horizVisc,float g){
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

    default float customPotionEffects(LivingEntity entity,float horizVisc){
        if (entity.hasStatusEffect(StatusEffects.DOLPHINS_GRACE)) {
            horizVisc = 0.96F;
        }
        return horizVisc;
    }
}


