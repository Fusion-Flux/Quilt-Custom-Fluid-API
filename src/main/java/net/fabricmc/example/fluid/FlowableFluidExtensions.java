package net.fabricmc.example.fluid;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
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

    int getFogColor(Entity entity);

    float getFogStart(Entity entity);

    float getFogEnd(Entity entity);

    Optional<SoundEvent> getSplashSound();

    void onSplash(World world, Vec3d pos, Entity entity);

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


