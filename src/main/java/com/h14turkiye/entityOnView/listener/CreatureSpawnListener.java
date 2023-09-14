package com.h14turkiye.entityOnView.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class CreatureSpawnListener implements Listener {
	@EventHandler
	public void spawnEventNatural(final CreatureSpawnEvent event) {
		if (event.getSpawnReason().equals(SpawnReason.NATURAL) && !event.isCancelled()) {
			event.setCancelled(ListenerHelper.callTracedEntity(event.getEntityType(), event.getLocation()));
		}
	}
}
