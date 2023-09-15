package com.h14turkiye.entityOnView.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent;

public class PreCreatureSpawnListener implements Listener {
	@EventHandler
	public void spawnEventNatural(final PreCreatureSpawnEvent event) {
		if (event.getReason().equals(SpawnReason.NATURAL) && !event.isCancelled()) {
			event.setCancelled(ListenerHelper.callTracedEntity(event.getType(), event.getSpawnLocation()));
		}
	}
}
