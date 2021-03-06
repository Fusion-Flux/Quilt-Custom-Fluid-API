package org.quiltmc.qsl.fluid.mixin;

import org.quiltmc.qsl.fluid.QuiltFluidAPI;
import org.quiltmc.qsl.fluid.fluid.FlowableFluidExtensions;
import org.quiltmc.qsl.fluid.interfaces.CustomFluidInteracting;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements CustomFluidInteracting {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Shadow
	public abstract boolean hasStatusEffect(StatusEffect effect);
	
	@Shadow
	protected abstract boolean shouldSwimInFluids();

	@Shadow
	public abstract boolean isClimbing();
	
	@Shadow
	public abstract Vec3d method_26317(double d, boolean bl, Vec3d vec3d);

	@Shadow
	public abstract Random getRandom();


	@Shadow protected abstract void swimUpward(TagKey<Fluid> fluid);
	
	@Shadow public abstract void updateLimbs(LivingEntity entity, boolean flutter);

	@Shadow public abstract boolean canWalkOnFluid(FluidState fluidState);

	@Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isFallFlying()Z"))
	private boolean redirectFallFlyingToAddCase(LivingEntity instance, Vec3d movementInput) {
		FluidState fluidState = world.getFluidState(getBlockPos());
		if (this.isInCustomFluid() && this.shouldSwimInFluids() && !this.canWalkOnFluid(fluidState)) {
			double fallSpeed = 0.08;
			boolean falling = this.getVelocity().y <= 0.0;
			if (falling && this.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
				fallSpeed = 0.01;
				this.onLanding();
			}
			
			double y = this.getY();
			float horizVisc = 0.8f;
			float vertVisc = 0.8f;
			float speed = 0.02F;
			
			if ((fluidState.getFluid() instanceof FlowableFluidExtensions fluid)) {
				horizVisc = this.isSprinting() ? 0.9f : fluid.getHorizontalViscosity(fluidState, this);
				vertVisc = fluid.getVerticalViscosity(fluidState, this);
			
				float[] values = fluid.customEnchantmentEffects(movementInput, ((LivingEntity) (Object) this), horizVisc, speed);
				horizVisc = values[0];
				speed = values[1];
				
				horizVisc = fluid.modifyHorizontalViscosity(((LivingEntity) (Object) this), horizVisc);
			}
			//
			this.updateVelocity(speed, movementInput);
			this.move(MovementType.SELF, this.getVelocity());
			Vec3d vec3d = this.getVelocity();
			if (this.horizontalCollision && this.isClimbing()) {
				vec3d = new Vec3d(vec3d.x, 0.2, vec3d.z);
			}
			
			this.setVelocity(vec3d.multiply(horizVisc, vertVisc, horizVisc));
			Vec3d vec3d2 = this.method_26317(fallSpeed, falling, this.getVelocity());
			this.setVelocity(vec3d2);
			if (this.horizontalCollision && this.doesNotCollide(vec3d2.x, vec3d2.y + 0.6 - this.getY() + y, vec3d2.z)) {
				this.setVelocity(vec3d2.x, 0.3, vec3d2.z);
			}
			return false;
		}
		return instance.isFallFlying();
	}
	
	@Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getVelocityAffectingPos()Lnet/minecraft/util/math/BlockPos;"), cancellable = true)
	private void cancelIfCustomFluid(Vec3d movementInput, CallbackInfo ci) {
		if (this.isInCustomFluid() && this.shouldSwimInFluids() && !this.canWalkOnFluid(world.getFluidState(getBlockPos()))) {
			this.updateLimbs((LivingEntity) (Object) this, this instanceof Flutterer);
			ci.cancel();
		}
	}
	
	@Redirect(method = "tickMovement",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getFluidHeight(Lnet/minecraft/tag/TagKey;)D", ordinal = 1))
	private double redirectGetFluidHeight(LivingEntity instance, TagKey<Fluid> tag) {
		if (isInCustomFluid()) {
			return getFluidHeight(QuiltFluidAPI.QUILT_FLUIDS);
		}
		return getFluidHeight(FluidTags.WATER);
	}
	
	@Redirect(method = "tickMovement",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isTouchingWater()Z"))
	private boolean redirectTouchingWaterToCheckIfSwim(LivingEntity instance) {
		if (isInCustomFluid()) {
			FluidState fluidState = this.world.getFluidState(getBlockPos());
			if (fluidState.getFluid() instanceof FlowableFluidExtensions fluid) {
				return fluid.enableSpacebarSwimming(fluidState, instance);
			}
		}
		return isTouchingWater();
	}
	
	@Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;swimUpward(Lnet/minecraft/tag/TagKey;)V"))
	private void redirectSwimUpward(LivingEntity instance, TagKey<Fluid> fluid) {
		swimUpward(isInCustomFluid() ? QuiltFluidAPI.QUILT_FLUIDS : fluid);
	}

	@Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getAir()I", ordinal = 2))
	private int baseTick(LivingEntity instance) {
		if (isSubmergedInCustomFluid(QuiltFluidAPI.QUILT_FLUIDS)) {
			FluidState fluidState = this.world.getFluidState(getBlockPos());
			if (fluidState.getFluid() instanceof FlowableFluidExtensions fluid) {
				fluid.drownEffects(fluidState, instance, getRandom());
				return getMaxAir(); // false
			}
		}
		
		return getAir();
	}
}

