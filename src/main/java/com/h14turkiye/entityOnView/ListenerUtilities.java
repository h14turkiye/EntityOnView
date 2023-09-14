package com.h14turkiye.entityOnView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class ListenerUtilities {

	private ListenerUtilities() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Returns the closest qualified {@link Player} to a specific {@link Location}
	 *
	 * @param loc                The {@link Location} representing the origin to
	 *                           search from
	 * @param maxDistanceSquared The maximum distance squared for player selection
	 * @param transparentBlocks  The set of transparent blocks for line of sight
	 *                           checks
	 * @param yDistanceLimit     The maximum Y distance allowed
	 * @param requireLineOfSight Whether to require line of sight to the player
	 * @return The closest qualified {@link Player}, or {@code null}
	 */
	public static Player getClosestTracedPlayer(final Location loc, final int maxDistanceSquared,
			final Set<Material> transparentBlocks, final int yDistanceLimit, final boolean requireLineOfSight) {
		final World world = loc.getWorld();

		List<Player> playersInRange = world.getPlayers().stream()
				.filter(p -> Math.abs(p.getLocation().getY() - loc.getY()) < yDistanceLimit).toList();

		for (final Player player : playersInRange) {
			final double distanceSquared = player.getLocation().distanceSquared(loc);
			if ((transparentBlocks == null || transparentBlocks.isEmpty()) && !requireLineOfSight
					&& distanceSquared < 28 * 28) {
				return player; // Return immediately if within 28 blocks
			}
		}

		playersInRange = playersInRange.stream().filter(p -> p.getLocation().distanceSquared(loc) < maxDistanceSquared)
				.sorted(Comparator.comparingDouble(p -> p.getLocation().distanceSquared(loc))).limit(8).toList();

		final AtomicReference<Player> closestPlayer = new AtomicReference<>(null);

		playersInRange.parallelStream().forEach(player -> {
			if (closestPlayer.get() == null
					&& (!requireLineOfSight || isLookingTowards(player.getEyeLocation(), loc, 75, 155))
					&& getLineOfSight(transparentBlocks, player.getEyeLocation(), loc).isEmpty()) {
				closestPlayer.set(player);
			}
		});

		return closestPlayer.get();
	}

	/**
	 * @see CraftLivingEntity.java
	 *
	 * @param transparent
	 * @param origin      The {@link Location} representing the origin
	 * @param target      The {@link Location} representing the target
	 * @return Blocks between locations.
	 */
	public static List<Block> getLineOfSight(final Set<Material> transparent, final Location origin,
			final Location target) {
		final ArrayList<Block> blocks = new ArrayList<>();
		if (transparent == null || transparent.isEmpty()) {
			return blocks;
		}
		final Iterator<Block> itr = new BlockIterator(target.getWorld(), origin.clone().add(0, -2, 0).toVector(),
				target.clone().toVector().subtract(origin.clone().add(0, -2, 0).toVector()).normalize(), 2, 128);
		while (itr.hasNext()) {
			final Block block = itr.next();
			final Material material = block.getType();
			if (transparent.contains(material)) {
				continue;
			}
			blocks.add(block);
			break;
		}
		return blocks;
	}

	public static boolean isLookingTowards(final Location origin, final Location target, final float yawLimit,
			final float pitchLimit) {
		final Vector rel = target.toVector().subtract(origin.toVector()).normalize();
		final float yaw = normalizeYaw(origin.getYaw());
		final float yawHelp = getYaw(rel);
		if (!(Math.abs(yawHelp - yaw) < yawLimit || Math.abs(yawHelp + 360 - yaw) < yawLimit
				|| Math.abs(yaw + 360 - yawHelp) < yawLimit)) {
			return false;
		}
		final float pitch = origin.getPitch();
		final float pitchHelp = getPitch(rel);
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
	public static float getPitch(final Vector vector) {
		final double dx = vector.getX();
		final double dy = vector.getY();
		final double dz = vector.getZ();
		final double forward = Math.sqrt(dx * dx + dz * dz);
		final double pitch = Math.atan2(dy, forward) * (180.0 / Math.PI);
		return (float) pitch;
	}

	/**
	 * Gets the yaw angle value (in degrees) for a normalized vector.
	 */
	public static float getYaw(final Vector vector) {
		final double dx = vector.getX();
		final double dz = vector.getZ();
		double yaw = 0;
		// Set yaw
		if (dx != 0) {
			// Set yaw start value based on dx
			if (dx < 0) {
				yaw = 1.5 * Math.PI;
			} else {
				yaw = 0.5 * Math.PI;
			}
			yaw -= Math.atan(dz / dx); // or atan2?
		} else if (dz < 0) {
			yaw = Math.PI;
		}
		return (float) (-yaw * (180.0 / Math.PI));
	}
}
