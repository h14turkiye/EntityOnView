package com.h14turkiye.entityOnView;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.h14turkiye.entityOnView.library.YamlWrapper;
import com.h14turkiye.entityOnView.listener.CreatureSpawnListener;
import com.h14turkiye.entityOnView.listener.PreCreatureSpawnListener;

public class EntityOnView extends JavaPlugin {
	private FileConfiguration config;

	@Override
	public void onEnable() {
		final YamlWrapper yamlWrapper = new YamlWrapper(this, getDataFolder(), "config", true, true);
		config = yamlWrapper.getConfig();
		new Config(config);

		if (Config.usePaperPreCreatureSpawnEvent)
			getServer().getPluginManager().registerEvents(new PreCreatureSpawnListener(), this);
		else
			getServer().getPluginManager().registerEvents(new CreatureSpawnListener(), this);
	}

	@Override
	public FileConfiguration getConfig() {
		return config;
	}
}
