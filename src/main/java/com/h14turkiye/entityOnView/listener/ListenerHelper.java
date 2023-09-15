package com.h14turkiye.entityOnView.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.h14turkiye.entityOnView.Config;
import com.h14turkiye.entityOnView.ListenerUtilities;
import com.h14turkiye.entityOnView.object.ViewTrackPoint;
import com.h14turkiye.entityOnView.object.ViewTrackPoint.Status;

public class ListenerHelper {
	private static Cache<Integer, ViewTrackPoint> vtpCache = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES)
			.maximumSize(100).build();

	private static AtomicInteger keyGenerator = new AtomicInteger(0);

	private static int generateKey() {
		return keyGenerator.incrementAndGet();
	}

	private static void add(final Status status, final EntityType type, final Location loc) {
		final int key = generateKey();
		final ViewTrackPoint vtp = new ViewTrackPoint(status, type.name(), loc.getX(), loc.getY(), loc.getZ(),
				loc.getWorld().getName());

		vtpCache.put(key, vtp);
	}

	public static boolean callTracedEntity(final EntityType type, final Location location) {
		if (Config.cancelSpawn.contains(type)) {
			Player nearestQualifiedPlayer;
			nearestQualifiedPlayer = ListenerUtilities.getClosestTracedPlayer(location, Config.radius,
					Config.transparentBlocks, Config.yDistanceLimit, Config.inSight, Config.traceClosestPlayerLimit);

			if (nearestQualifiedPlayer == null) {
				add(Status.CANCELLED, type, location);
				return true;
			}
			add(Status.PASS, type, location);
			return false;
		}
		add(Status.IGNORED, type, location);
		return false;
	}

	public static List<ViewTrackPoint> getVtp() {
		return new ArrayList<>(vtpCache.asMap().values());
	}
}
