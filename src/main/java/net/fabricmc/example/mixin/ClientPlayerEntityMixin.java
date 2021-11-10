package net.fabricmc.example.mixin;

import com.mojang.authlib.GameProfile;
import net.fabricmc.example.interfaces.CustomFluidInteracting;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements CustomFluidInteracting {
	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSubmergedInWater()Z"))
	private boolean redirectSubmergedInWater(ClientPlayerEntity instance) {
		return this.isSubmergedInWater() || this.isSubmergedInCustomFluid();
	}
	
	@Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isTouchingWater()Z"))
	private boolean redirectTouchingWater(ClientPlayerEntity instance) {
		return this.isTouchingWater() || this.isInCustomFluid();
	}
}
