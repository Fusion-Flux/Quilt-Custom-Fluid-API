package org.quiltmc.qsl.fluid.mixin;


import org.quiltmc.qsl.fluid.QuiltFluidAPI;
import org.quiltmc.qsl.fluid.fluid.FlowableFluidExtensions;
import org.quiltmc.qsl.fluid.interfaces.CustomFluidInteracting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(Entity.class)
public abstract class EntityMixin implements CustomFluidInteracting {
	@Shadow
	public float fallDistance;
	@Shadow
	public World world;
	@Shadow
	protected boolean firstUpdate;
	@Shadow
	@Final
	protected Random random;
	protected boolean inCustomFluid;
	protected boolean submergedInCustomFluid;
	protected TagKey<Fluid> submergedCustomFluidTag;
	@Shadow
	private BlockPos blockPos;
	
	@Shadow
	public abstract boolean equals(Object o);
	
	@Shadow
	@Nullable
	public abstract Entity getVehicle();
	
	@Shadow
	public abstract boolean updateMovementInFluid(TagKey<Fluid> tag, double d);

	@Shadow
	public abstract void extinguish();
	
	@Shadow
	public abstract double getEyeY();
	
	@Shadow
	public abstract double getX();
	
	@Shadow
	public abstract double getZ();
	
	@Shadow
	public abstract BlockPos getBlockPos();
	
	@Shadow
	public abstract boolean isSprinting();
	
	@Shadow
	public abstract boolean isSwimming();
	
	@Shadow
	public abstract void setSwimming(boolean swimming);

	@Shadow
	public abstract boolean hasVehicle();

	@Shadow
	public abstract double getY();
	
	@Shadow public abstract void setOnFireFromLava();
	
	@Inject(method = "baseTick", at = @At("TAIL"))
	public void baseTick(CallbackInfo ci) {
		this.checkCustomFluidState();
		this.updateSubmergedInCustomFluidState();
	}
	
	@Override
	public boolean isInCustomFluid() {
		return this.inCustomFluid;
	}
	
	@Override
	public boolean isSubmergedInCustomFluid() {
		return this.submergedInCustomFluid && this.isInCustomFluid();
	}
	
	void checkCustomFluidState() {
		FluidState fluidState = this.world.getFluidState(this.getBlockPos());
		if (fluidState.getFluid() instanceof FlowableFluidExtensions fluid &&
				!(getVehicle() instanceof BoatEntity) &&
				updateMovementInFluid(QuiltFluidAPI.QUILT_FLUIDS, fluid.getPushStrength(fluidState, (Entity) (Object) this))) {
			if (!inCustomFluid && !firstUpdate) {
				customSplashEffects();
			}
			fallDistance = fluid.fallDamageReduction(((Entity) (Object) this));
			inCustomFluid = true;
			
			if (fluid.canExtinguish(fluidState, (Entity) (Object) this)) {
				extinguish();
			}
			if (fluid.canIgnite(fluidState, (Entity) (Object) this)) {
				setOnFireFromLava();
			}
			return;
		}
		this.inCustomFluid = false;
	}
	
	private void updateSubmergedInCustomFluidState() {
		this.submergedInCustomFluid = this.isSubmergedInCustomFluid(QuiltFluidAPI.QUILT_FLUIDS);
		this.submergedCustomFluidTag = null;
		double d = this.getEyeY() - 0.1111111119389534D;
		Entity entity = this.getVehicle();
		if (entity instanceof BoatEntity boatEntity) {
			if (!boatEntity.isSubmergedInWater() && boatEntity.getBoundingBox().maxY >= d && boatEntity.getBoundingBox().minY <= d) {
				return;
			}
		}
		
		BlockPos blockPos = new BlockPos(this.getX(), d, this.getZ());
		FluidState fluidState = this.world.getFluidState(blockPos);
		
		
		double e = (float) blockPos.getY() + fluidState.getHeight(this.world, blockPos);
		if (e > d) {
			this.submergedCustomFluidTag = QuiltFluidAPI.QUILT_FLUIDS;
		}
		
	}
	
	public boolean isSubmergedInCustomFluid(TagKey<Fluid> fluidTag) {
		return this.submergedCustomFluidTag == fluidTag;
	}
	
	
	@Inject(method = "updateSwimming", at = @At("TAIL"))
	public void updateSwimming(CallbackInfo ci) {
		boolean canSwimIn = false;
		if (this.isInCustomFluid()) {
			FluidState fluidState = this.world.getFluidState(this.getBlockPos());
			if (fluidState.getFluid() instanceof FlowableFluidExtensions fluid) {
				canSwimIn = fluid.canSprintSwim(fluidState, (Entity) (Object) this);
			}
			if (this.isSwimming()) {
				this.setSwimming(this.isSprinting() && canSwimIn && this.isInCustomFluid() && !this.hasVehicle());
			} else {
				this.setSwimming(this.isSprinting() && this.isSubmergedInCustomFluid() && canSwimIn && !this.hasVehicle() && this.world.getFluidState(this.blockPos).isIn(QuiltFluidAPI.QUILT_FLUIDS));
			}
			
		}
	}
	
	private void customSplashEffects() {
		FluidState fluidState = this.world.getFluidState(this.blockPos);
		if (fluidState.getFluid() instanceof FlowableFluidExtensions fluid) {
			//Execute the onSplash event
			fluid.onSplash(this.world, new Vec3d(this.getX(), this.getY(), this.getZ()), (Entity) (Object) this, this.random);
		}
	}
}
