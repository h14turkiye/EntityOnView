package com.h14turkiye.entityOnView.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import com.h14turkiye.entityOnView.listener.ListenerHelper;
import com.h14turkiye.entityOnView.object.ViewTrackPoint;
import com.h14turkiye.entityOnView.object.ViewTrackPoint.Status;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class EOVCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		if (args.length == 0) {
			sender.sendMessage("/entityonview pass/ignored/cancelled");
			return true;
		}
		final List<ViewTrackPoint> original = ListenerHelper.getVtp();
		List<ViewTrackPoint> vtp;
		@NotNull
		final TextComponent text = Component.text("TrackPointTrace:");
		switch (args[0]) {
		case "pass":
			vtp = find(original, Status.PASS);
			Component textComponent = text.appendNewline();
			for (final ViewTrackPoint v : vtp) {
				textComponent.append(trace(v));
			}
			sender.sendMessage(textComponent);
			return true;
		case "ignored":
			vtp = find(original, Status.IGNORED);
			textComponent = text.appendNewline();
			for (final ViewTrackPoint v : vtp) {
				textComponent.append(trace(v));
			}
			sender.sendMessage(textComponent);
			return true;
		case "cancelled":
			vtp = find(original, Status.CANCELLED);
			textComponent = text.appendNewline();
			for (final ViewTrackPoint v : vtp) {
				textComponent.append(trace(v));
			}
			sender.sendMessage(textComponent);
			return true;
		default:
			return false;
		}
	}
	
	private Component trace(ViewTrackPoint v) {
		return Component.text(v.getType()).appendSpace().append(Component.text(v.getX())).appendSpace()
				.append(Component.text(v.getY())).appendSpace().append(Component.text(v.getZ())).appendSpace()
				.append(Component.text(v.getWorld())).appendNewline();
	}

	private List<ViewTrackPoint> find(final List<ViewTrackPoint> list, final Status status) {
		return list.stream().filter(vtp -> vtp.getStatus().equals(status)).toList();
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias,
			final String[] args) {
		final List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			completions.add("pass");
			completions.add("ignored");
			completions.add("cancelled");
			return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>());
		}
		return null;
	}
}
