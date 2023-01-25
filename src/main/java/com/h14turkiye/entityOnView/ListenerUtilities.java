package com.h14turkiye.entityOnView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.google.common.collect.Sets;

public class ListenerUtilities {
	
	private ListenerUtilities() {
	    throw new IllegalStateException("Utility class");
	  }

	
	/**
	 * @see CraftLivingEntity.java
	 * 
	 * @param transparent
	 * @param maxDistance
	 * @param origin The {@link Location} representing the origin
	 * @param target The {@link Location} representing the target
	 * @return Blocks between locations.
	 */
	public static List<Block> getLineOfSight(Set<Material> transparent, int maxDistance, Location origin, Location target) {
		if (transparent == null) {
			transparent = Sets.newHashSet(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR);
		}
		if (maxDistance > 120) {
			maxDistance = 120;
		}
		ArrayList<Block> blocks = new ArrayList<>();
		Iterator<Block> itr = new BlockIterator(target.getWorld(), origin.clone().add(0, -2, 0).toVector(), target.clone().toVector().subtract(origin.clone().add(0, -2, 0).toVector()).normalize(), 2, maxDistance);
		while (itr.hasNext()) {
			Block block = itr.next();
            Material material = block.getType();
            if (transparent.contains(material)) {
                continue;
            }
            blocks.add(block);
			break;
		}
		return blocks;
	}
	
	public static boolean isLookingTowards(Location origin, Location target, float yawLimit, float pitchLimit) {
		Vector rel = target.toVector().subtract(origin.toVector()).normalize();
		float yaw = normalizeYaw(origin.getYaw());
		float yawHelp = getYaw(rel);
		if (!(Math.abs(yawHelp - yaw) < yawLimit ||
				Math.abs(yawHelp + 360 - yaw) < yawLimit ||
				Math.abs(yaw + 360 - yawHelp) < yawLimit)) {
			return false;
		}
		float pitch = origin.getPitch();
		float pitchHelp = getPitch(rel);
		if (!(Math.abs(pitchHelp - pitch) < pitchLimit)) {
			return false;
		}
		return true;
	}

	public static float normalizeYaw(float yaw) {
		yaw = yaw % 360;
		if (yaw < 0) {
			yaw += 360.0;
		}
		return yaw;
	}

	/**
	 * Gets the pitch angle value (in degrees) for a normalized vector.
	 */
	public static float getPitch(Vector vector) {
		double dx = vector.getX();
		double dy = vector.getY();
		double dz = vector.getZ();
		double forward = Math.sqrt((dx * dx) + (dz * dz));
		double pitch = Math.atan2(dy, forward) * (180.0 / Math.PI);
		return (float) pitch;
	}

	/**
	 * Gets the yaw angle value (in degrees) for a normalized vector.
	 */
	public static float getYaw(Vector vector) {
		double dx = vector.getX();
		double dz = vector.getZ();
		double yaw = 0;
		// Set yaw
		if (dx != 0) {
			// Set yaw start value based on dx
			if (dx < 0) {
				yaw = 1.5 * Math.PI;
			}
			else {
				yaw = 0.5 * Math.PI;
			}
			yaw -= Math.atan(dz / dx); // or atan2?
		}
		else if (dz < 0) {
			yaw = Math.PI;
		}
		return (float) (-yaw * (180.0 / Math.PI));
	}
}
