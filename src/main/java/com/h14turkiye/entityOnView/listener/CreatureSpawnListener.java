package com.h14turkiye.entityOnView.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.h14turkiye.entityOnView.Config;
import com.h14turkiye.entityOnView.ListenerUtilities;

public class CreatureSpawnListener implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void spawnEventNatural(final CreatureSpawnEvent event) {
		if (event.getSpawnReason().equals(SpawnReason.NATURAL)) {
			if (Config.cancelSpawn.contains(event.getEntityType())) {
				final Location location = event.getLocation();
				Player nearestQualifiedPlayer;
				nearestQualifiedPlayer = ListenerUtilities.getClosestTracedPlayer(location, Config.radius,
					Config.transparentBlocks, Config.yDistanceLimit, Config.inSight);
	
				if (nearestQualifiedPlayer == null) {
					event.setCancelled(true);
					if (Config.debug)
						Bukkit.broadcastMessage(location.toString() + "-cancelled");
				}
			}
		}
	}
}
