package net.fabricmc.example.mixin;

import com.mojang.authlib.GameProfile;
import net.fabricmc.example.interfaces.CustomFluidInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.JumpingMount;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
	
	@Shadow
	public int ticksSinceSprintingChanged;
	@Shadow
	public Input input;
	@Shadow
	@Final
	public ClientPlayNetworkHandler networkHandler;
	@Shadow
	protected int ticksLeftToDoubleTapSprint;
	@Shadow
	@Final
	protected MinecraftClient client;
	@Shadow
	private boolean inSneakingPose;
	@Shadow
	private int ticksToNextAutojump;
	@Shadow
	private boolean falling;
	@Shadow
	private int underwaterVisibilityTicks;
	@Shadow
	private int field_3938;
	@Shadow
	private float mountJumpStrength;
	
	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}
	
	@Shadow
	protected abstract void updateNausea();
	
	@Shadow
	protected abstract boolean isWalking();
	
	@Shadow
	public abstract boolean shouldSlowDown();
	
	@Shadow
	protected abstract void pushOutOfBlocks(double x, double z);
	
	@Shadow
	protected abstract boolean isCamera();
	
	@Shadow
	public abstract boolean hasJumpingMount();
	
	@Shadow
	public abstract float getMountJumpStrength();
	
	@Shadow
	protected abstract void startRidingJump();
	
	/**
	 * @author
	 */
	@Overwrite
	public void tickMovement() {
		++this.ticksSinceSprintingChanged;
		if (this.ticksLeftToDoubleTapSprint > 0) {
			--this.ticksLeftToDoubleTapSprint;
		}
		
		this.updateNausea();
		boolean bl = this.input.jumping;
		boolean bl2 = this.input.sneaking;
		boolean bl3 = this.isWalking();
		this.inSneakingPose = !this.getAbilities().flying && !this.isSwimming() && this.wouldPoseNotCollide(EntityPose.CROUCHING) && (this.isSneaking() || !this.isSleeping() && !this.wouldPoseNotCollide(EntityPose.STANDING));
		this.input.tick(this.shouldSlowDown());
		this.client.getTutorialManager().onMovement(this.input);
		if (this.isUsingItem() && !this.hasVehicle()) {
			Input var10000 = this.input;
			var10000.movementSideways *= 0.2F;
			var10000 = this.input;
			var10000.movementForward *= 0.2F;
			this.ticksLeftToDoubleTapSprint = 0;
		}
		
		boolean bl4 = false;
		if (this.ticksToNextAutojump > 0) {
			--this.ticksToNextAutojump;
			bl4 = true;
			this.input.jumping = true;
		}
		
		if (!this.noClip) {
			this.pushOutOfBlocks(this.getX() - (double) this.getWidth() * 0.35D, this.getZ() + (double) this.getWidth() * 0.35D);
			this.pushOutOfBlocks(this.getX() - (double) this.getWidth() * 0.35D, this.getZ() - (double) this.getWidth() * 0.35D);
			this.pushOutOfBlocks(this.getX() + (double) this.getWidth() * 0.35D, this.getZ() - (double) this.getWidth() * 0.35D);
			this.pushOutOfBlocks(this.getX() + (double) this.getWidth() * 0.35D, this.getZ() + (double) this.getWidth() * 0.35D);
		}
		
		if (bl2) {
			this.ticksLeftToDoubleTapSprint = 0;
		}
		
		//custom boolean
		boolean submergedInFluid = this.isSubmergedInWater() || ((CustomFluidInterface) this).isSubmergedInCustomFluid();
		//custom boolean
		boolean touchingFluid = this.isTouchingWater() || ((CustomFluidInterface) this).isInCustomFluid();
		//modified if statement
		boolean bl5 = (float) this.getHungerManager().getFoodLevel() > 6.0F || this.getAbilities().allowFlying;
		if ((this.onGround || submergedInFluid) && !bl2 && !bl3 && this.isWalking() && !this.isSprinting() && bl5 && !this.isUsingItem() && !this.hasStatusEffect(StatusEffects.BLINDNESS)) {
			if (this.ticksLeftToDoubleTapSprint <= 0 && !this.client.options.keySprint.isPressed()) {
				this.ticksLeftToDoubleTapSprint = 7;
			} else {
				this.setSprinting(true);
			}
		}
		//modified if statement
		if (!this.isSprinting() && (!touchingFluid || submergedInFluid) && this.isWalking() && bl5 && !this.isUsingItem() && !this.hasStatusEffect(StatusEffects.BLINDNESS) && this.client.options.keySprint.isPressed()) {
			this.setSprinting(true);
		}
		//modified if statement
		boolean bl8;
		if (this.isSprinting()) {
			bl8 = !this.input.hasForwardMovement() || !bl5;
			boolean bl7 = bl8 || this.horizontalCollision || touchingFluid && !submergedInFluid;
			if (this.isSwimming()) {
				if (!this.onGround && !this.input.sneaking && bl8 || !touchingFluid) {
					this.setSprinting(false);
				}
			} else if (bl7) {
				this.setSprinting(false);
			}
		}
		
		
		bl8 = false;
		if (this.getAbilities().allowFlying) {
			if (this.client.interactionManager.isFlyingLocked()) {
				if (!this.getAbilities().flying) {
					this.getAbilities().flying = true;
					bl8 = true;
					this.sendAbilitiesUpdate();
				}
			} else if (!bl && this.input.jumping && !bl4) {
				if (this.abilityResyncCountdown == 0) {
					this.abilityResyncCountdown = 7;
				} else if (!this.isSwimming()) {
					this.getAbilities().flying = !this.getAbilities().flying;
					bl8 = true;
					this.sendAbilitiesUpdate();
					this.abilityResyncCountdown = 0;
				}
			}
		}
		
		if (this.input.jumping && !bl8 && !bl && !this.getAbilities().flying && !this.hasVehicle() && !this.isClimbing()) {
			ItemStack itemStack = this.getEquippedStack(EquipmentSlot.CHEST);
			if (itemStack.isOf(Items.ELYTRA) && ElytraItem.isUsable(itemStack) && this.checkFallFlying()) {
				this.networkHandler.sendPacket(new ClientCommandC2SPacket(this, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
			}
		}
		
		this.falling = this.isFallFlying();
		if (this.isTouchingWater() && this.input.sneaking && this.shouldSwimInFluids()) {
			this.knockDownwards();
		}
		
		int j;
		if (this.isSubmergedIn(FluidTags.WATER)) {
			j = this.isSpectator() ? 10 : 1;
			this.underwaterVisibilityTicks = MathHelper.clamp(this.underwaterVisibilityTicks + j, 0, 600);
		} else if (this.underwaterVisibilityTicks > 0) {
			this.isSubmergedIn(FluidTags.WATER);
			this.underwaterVisibilityTicks = MathHelper.clamp(this.underwaterVisibilityTicks - 10, 0, 600);
		}
		
		if (this.getAbilities().flying && this.isCamera()) {
			j = 0;
			if (this.input.sneaking) {
				--j;
			}
			
			if (this.input.jumping) {
				++j;
			}
			
			if (j != 0) {
				this.setVelocity(this.getVelocity().add(0.0D, (float) j * this.getAbilities().getFlySpeed() * 3.0F, 0.0D));
			}
		}
		
		if (this.hasJumpingMount()) {
			JumpingMount jumpingMount = (JumpingMount) this.getVehicle();
			if (this.field_3938 < 0) {
				++this.field_3938;
				if (this.field_3938 == 0) {
					this.mountJumpStrength = 0.0F;
				}
			}
			
			if (bl && !this.input.jumping) {
				this.field_3938 = -10;
				jumpingMount.setJumpStrength(MathHelper.floor(this.getMountJumpStrength() * 100.0F));
				this.startRidingJump();
			} else if (!bl && this.input.jumping) {
				this.field_3938 = 0;
				this.mountJumpStrength = 0.0F;
			} else if (bl) {
				++this.field_3938;
				if (this.field_3938 < 10) {
					this.mountJumpStrength = (float) this.field_3938 * 0.1F;
				} else {
					this.mountJumpStrength = 0.8F + 2.0F / (float) (this.field_3938 - 9) * 0.1F;
				}
			}
		} else {
			this.mountJumpStrength = 0.0F;
		}
		
		super.tickMovement();
		if (this.onGround && this.getAbilities().flying && !this.client.interactionManager.isFlyingLocked()) {
			this.getAbilities().flying = false;
			this.sendAbilitiesUpdate();
		}
		
	}
	
}
