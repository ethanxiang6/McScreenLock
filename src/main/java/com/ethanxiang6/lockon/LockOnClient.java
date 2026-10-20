package com.ethanxiang6.lockon;

import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

public final class LockOnClient implements ClientModInitializer {
	public static final String MOD_ID = "lockon";

	private static KeyMapping toggleKey;

	@Override
	public void onInitializeClient() {
		LockOnConfig.load();

		KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"));
		toggleKey = KeyMappingHelper.registerKeyMapping(
				new KeyMapping("key.lockon.toggle", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, category));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (toggleKey.consumeClick()) {
				LockOn.toggle(client);
			}
			LockOn.tick(client);
		});

		System.out.println("[lockon] Loaded. Toggle key defaults to G (Controls > Lock On).");
	}
}
