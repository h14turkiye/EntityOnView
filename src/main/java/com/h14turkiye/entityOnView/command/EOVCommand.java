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

public class EOVCommand
implements CommandExecutor, TabCompleter
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage("/entityonview pass/ignored/cancelled");
			return true;
		} 
		List<ViewTrackPoint> original = ListenerHelper.getVtp();
		List<ViewTrackPoint> vtp;
		@NotNull
		TextComponent text = Component.text("TrackPointTrace:");
		switch(args[0]) {
		case "pass":
			vtp = find(original, Status.PASS);
			Component textComponent = text.appendNewline();
			for(ViewTrackPoint v : vtp) {
				textComponent.append(Component.text(v.getType())
						.append(Component.text(v.getX())).appendSpace()
						.append(Component.text(v.getY())).appendSpace()
						.append(Component.text(v.getZ())).appendSpace()
						.append(Component.text(v.getWorld())).appendNewline());
			}
			sender.sendMessage(textComponent);
			return true;
		case "ignored":
			vtp = find(original, Status.IGNORED);
			textComponent = text.appendNewline();
			for(ViewTrackPoint v : vtp) {
				textComponent.append(Component.text(v.getType())
						.append(Component.text(v.getX())).appendSpace()
						.append(Component.text(v.getY())).appendSpace()
						.append(Component.text(v.getZ())).appendSpace()
						.append(Component.text(v.getWorld())).appendNewline());
			}
			sender.sendMessage(textComponent);
			return true;
		case "cancelled":
			vtp = find(original, Status.CANCELLED);
			textComponent = text.appendNewline();
			for(ViewTrackPoint v : vtp) {
				textComponent.append(Component.text(v.getType())
						.append(Component.text(v.getX())).appendSpace()
						.append(Component.text(v.getY())).appendSpace()
						.append(Component.text(v.getZ())).appendSpace()
						.append(Component.text(v.getWorld())).appendNewline());
			}
			sender.sendMessage(textComponent);
			return true;
		default:
			return false;
		}
	}

	private List<ViewTrackPoint> find(List<ViewTrackPoint> list, Status status){
		return list.stream().filter(vtp -> vtp.getStatus().equals(status)).toList();
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			completions.add("pass");
			completions.add("ignored");
			completions.add("cancelled");
			return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>());
		} 
		return null;
	}
}
