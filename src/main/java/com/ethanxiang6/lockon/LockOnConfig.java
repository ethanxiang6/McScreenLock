package com.ethanxiang6.lockon;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.loader.api.FabricLoader;

/**
 * Settings stored in config/lockon.json. Edit the file and restart the game.
 */
public final class LockOnConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	/** Furthest a player can be when you lock on, in blocks. The lock drops at 1.5x this. */
	public double maxRange = 64.0;

	/** How close to your crosshair (in degrees) a player must be to get picked. */
	public float targetCone = 15.0f;

	/** Where on their body to aim, from 0.0 (feet) to 1.0 (top of head). */
	public double aimHeight = 0.85;

	/** Only lock onto players you can actually see (not through walls). */
	public boolean requireLineOfSight = true;

	/** Also allow locking onto mobs, not just players. */
	public boolean lockMobs = false;

	/** Show lock and unlock messages in the action bar. */
	public boolean showStatus = true;

	private static LockOnConfig instance = new LockOnConfig();

	public static LockOnConfig get() {
		return instance;
	}

	static void load() {
		Path path = FabricLoader.getInstance().getConfigDir().resolve("lockon.json");

		if (Files.exists(path)) {
			try (Reader reader = Files.newBufferedReader(path)) {
				LockOnConfig loaded = GSON.fromJson(reader, LockOnConfig.class);
				if (loaded != null) {
					instance = loaded;
				}
			} catch (Exception e) {
				System.err.println("[lockon] Could not read " + path + ", using defaults: " + e);
			}
		}

		instance.maxRange = Math.max(1.0, Math.min(512.0, instance.maxRange));
		instance.targetCone = Math.max(1.0f, Math.min(180.0f, instance.targetCone));
		instance.aimHeight = Math.max(0.0, Math.min(1.0, instance.aimHeight));

		try {
			Files.createDirectories(path.getParent());
			try (Writer writer = Files.newBufferedWriter(path)) {
				GSON.toJson(instance, writer);
			}
		} catch (IOException e) {
			System.err.println("[lockon] Could not write " + path + ": " + e);
		}
	}
}
