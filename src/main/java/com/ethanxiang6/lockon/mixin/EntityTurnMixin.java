package com.ethanxiang6.lockon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

import com.ethanxiang6.lockon.LockOn;

/**
 * The game calls turn() on your player every frame to apply mouse movement.
 * While locked, that mouse movement is dropped and the view is aimed at the target instead,
 * so the lock updates every frame rather than 20 times a second.
 */
@Mixin(Entity.class)
public abstract class EntityTurnMixin {
	@Inject(method = "turn", at = @At("HEAD"), cancellable = true)
	private void lockon$replaceMouseTurn(double yRot, double xRot, CallbackInfo ci) {
		Minecraft mc = Minecraft.getInstance();
		if ((Object) this != mc.player || !LockOn.isLocked()) {
			return;
		}

		LockOn.aim(mc, mc.getDeltaTracker().getGameTimeDeltaPartialTick(true));
		ci.cancel();
	}
}
