package com.h14turkiye.entityOnView;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

public class Config {

	public static boolean debug;
	public static boolean useTransparencyCheck;
	public static boolean inSight;

	public static int yDistanceLimit;
	public static int radius;

	public static Set<EntityType> cancelSpawn = new HashSet<>();
	public static Set<Material> transparentBlocks = new HashSet<>();
	public static boolean usePaperPreCreatureSpawnEvent;

	public Config(final FileConfiguration config) {
		debug = config.getBoolean("debug");
		inSight = config.getBoolean("inSight");
		useTransparencyCheck = config.getBoolean("raytracing.enabled");
		yDistanceLimit = config.getInt("yDistanceLimit");
		radius = config.getInt("radius");
		radius = radius * radius;
		usePaperPreCreatureSpawnEvent = config.getBoolean("usePaperPreCreatureSpawnEvent");

		config.getStringList("cancelSpawn").forEach(string -> cancelSpawn.add(EntityType.valueOf(string)));
		if (useTransparencyCheck)
			config.getStringList("raytracing.ignoreBlocks")
					.forEach(string -> transparentBlocks.add(Material.valueOf(string)));
	}

}
