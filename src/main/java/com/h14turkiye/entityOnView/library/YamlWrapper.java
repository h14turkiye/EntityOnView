package com.h14turkiye.entityOnView.library;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class YamlWrapper {

	protected final boolean createIfNotExist, resource;
	protected final JavaPlugin plugin;

	protected FileConfiguration config;
	protected File file, path;
	protected String name;

	public YamlWrapper(final JavaPlugin instance, final File path, final String name, final boolean createIfNotExist,
			final boolean resource) {
		plugin = instance;
		this.path = path;
		this.name = name + ".yml";
		this.createIfNotExist = createIfNotExist;
		this.resource = resource;
		create();
	}

	public YamlWrapper(final JavaPlugin instance, final String path, final String name, final boolean createIfNotExist,
			final boolean resource) {
		this(instance, new File(path), name, createIfNotExist, resource);
	}

	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		return config;
	}

	public void save() {
		try {
			config.save(file);
		} catch (final Exception exc) {
			exc.printStackTrace();
		}
	}

	public File reloadFile() {
		file = new File(path, name);
		return file;
	}

	public FileConfiguration reloadConfig() {
		config = YamlConfiguration.loadConfiguration(file);
		return config;
	}

	public void reload() {
		reloadFile();
		reloadConfig();
	}

	public void create() {
		if (file == null) {
			reloadFile();
		}
		if (!createIfNotExist || file.exists()) {
			return;
		}
		file.getParentFile().mkdirs();
		if (resource) {
			plugin.saveResource(name, false);
		} else {
			try {
				file.createNewFile();
			} catch (final Exception exc) {
				exc.printStackTrace();
			}
		}
		if (config == null) {
			reloadConfig();
		}
	}
}
