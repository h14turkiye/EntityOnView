package com.h14turkiye.entityOnView.object;

public class ViewTrackPoint {
	private final String type;
	private final double x;
	private final double y;
	private final double z;
	private final Status status;
	private final String world;

	public ViewTrackPoint(final Status status, final String type, final double x, final double y, final double z,
			final String string) {
		this.status = status;
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		world = string;
	}

	public Status getStatus() {
		return status;
	}

	public String getType() {
		return type;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public String getWorld() {
		return world;
	}

	public enum Status {
		PASS, CANCELLED, IGNORED
	}
}
