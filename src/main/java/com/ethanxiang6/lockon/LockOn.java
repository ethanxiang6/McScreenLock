package com.ethanxiang6.lockon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public final class LockOn {
	private static LivingEntity target;

	private LockOn() {
	}

	public static boolean isLocked() {
		return target != null;
	}

	static void toggle(Minecraft mc) {
		if (target != null) {
			unlock(mc, "Unlocked");
			return;
		}

		LocalPlayer player = mc.player;
		if (player == null || mc.level == null) {
			return;
		}

		LivingEntity found = findTarget(mc, player);
		if (found == null) {
			status(mc, "Lock On: no player under your crosshair");
			return;
		}

		target = found;
		status(mc, "Locked on " + found.getName().getString());
		aim(mc, 1.0f);
	}

	/** Runs every tick: drops the lock when the target is gone, and keeps aiming even if the mouse isn't moving. */
	static void tick(Minecraft mc) {
		if (target == null) {
			return;
		}

		LocalPlayer player = mc.player;
		if (player == null || mc.level == null) {
			target = null;
			return;
		}

		LivingEntity t = target;
		if (!t.isAlive() || t.isRemoved() || t.level() != player.level()) {
			unlock(mc, "Target lost, unlocked");
			return;
		}
		if (player.distanceTo(t) > LockOnConfig.get().maxRange * 1.5) {
			unlock(mc, "Target out of range, unlocked");
			return;
		}

		aim(mc, 1.0f);
	}

	/** Points the view at the target, using positions interpolated to the current frame. */
	public static void aim(Minecraft mc, float partialTick) {
		LocalPlayer player = mc.player;
		LivingEntity t = target;
		if (player == null || t == null) {
			return;
		}

		double eyeX = Mth.lerp(partialTick, player.xOld, player.getX());
		double eyeY = Mth.lerp(partialTick, player.yOld, player.getY()) + player.getEyeHeight();
		double eyeZ = Mth.lerp(partialTick, player.zOld, player.getZ());

		double aimX = Mth.lerp(partialTick, t.xOld, t.getX());
		double aimY = Mth.lerp(partialTick, t.yOld, t.getY()) + t.getBbHeight() * LockOnConfig.get().aimHeight;
		double aimZ = Mth.lerp(partialTick, t.zOld, t.getZ());

		double dx = aimX - eyeX;
		double dy = aimY - eyeY;
		double dz = aimZ - eyeZ;
		double horizontal = Math.sqrt(dx * dx + dz * dz);

		float wantYaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
		float wantPitch = Mth.clamp((float) -Math.toDegrees(Math.atan2(dy, horizontal)), -90.0f, 90.0f);

		// Move both the current and previous-tick rotation by the same amount, the way mouse
		// turning does, so the camera lands exactly on the target with no interpolation lag.
		float yawDelta = Mth.wrapDegrees(wantYaw - player.getYRot());
		float pitchDelta = wantPitch - player.getXRot();

		player.setYRot(player.getYRot() + yawDelta);
		player.setXRot(player.getXRot() + pitchDelta);
		player.yRotO += yawDelta;
		player.xRotO += pitchDelta;
	}

	private static void unlock(Minecraft mc, String message) {
		target = null;
		status(mc, "Lock On: " + message);
	}

	private static LivingEntity findTarget(Minecraft mc, LocalPlayer player) {
		LockOnConfig cfg = LockOnConfig.get();
		float yaw = player.getYRot();
		float pitch = player.getXRot();

		LivingEntity best = null;
		double bestAngle = Double.MAX_VALUE;

		for (Entity e : mc.level.entitiesForRendering()) {
			if (e == player || !(e instanceof LivingEntity living)) {
				continue;
			}
			if (!(e instanceof Player || (cfg.lockMobs && e instanceof Mob))) {
				continue;
			}
			if (!living.isAlive() || living.isRemoved() || living.isSpectator()) {
				continue;
			}
			if (player.distanceTo(living) > cfg.maxRange) {
				continue;
			}
			if (cfg.requireLineOfSight && !player.hasLineOfSight(living)) {
				continue;
			}

			double dx = living.getX() - player.getX();
			double dy = living.getY() + living.getBbHeight() * 0.5 - player.getEyeY();
			double dz = living.getZ() - player.getZ();
			float toYaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
			float toPitch = (float) -Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)));

			double yawOff = Mth.wrapDegrees(toYaw - yaw);
			double pitchOff = toPitch - pitch;
			double angle = Math.sqrt(yawOff * yawOff + pitchOff * pitchOff);

			if (angle <= cfg.targetCone && angle < bestAngle) {
				bestAngle = angle;
				best = living;
			}
		}

		return best;
	}

	private static void status(Minecraft mc, String text) {
		if (LockOnConfig.get().showStatus) {
			mc.gui.hud.setOverlayMessage(Component.literal(text), false);
		}
	}
}
