package com.h14turkiye.entityOnView.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.h14turkiye.entityOnView.Config;
import com.h14turkiye.entityOnView.ListenerUtilities;

public class ListenerHelper {
	@SuppressWarnings("deprecation")
	public static boolean callTracedEntity(final EntityType type, final Location location) {
		if (Config.cancelSpawn.contains(type)) {
			Player nearestQualifiedPlayer;
			nearestQualifiedPlayer = ListenerUtilities.getClosestTracedPlayer(location, Config.radius,
					Config.transparentBlocks, Config.yDistanceLimit, Config.inSight);

			if (nearestQualifiedPlayer == null) {
				if (Config.debug)
					Bukkit.broadcastMessage(location.toString() + "-cancelled");
				return true;
			}
		}
		return false;
	}
}
