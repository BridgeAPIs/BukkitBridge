package net.bridgesapi.core;

import net.md_5.bungee.api.ChatColor;
import net.bridgesapi.api.channels.PatternReceiver;
import net.bridgesapi.api.network.JoinHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class DebugListener implements PatternReceiver, JoinHandler {

	private CopyOnWriteArraySet<UUID> debugs = new CopyOnWriteArraySet<>();
	private boolean console = false;

	public void toggle(CommandSender sender) {
		if (sender instanceof Player) {
			UUID id = ((Player) sender).getUniqueId();
			if (debugs.contains(id))
				debugs.add(id);
			else
				debugs.remove(id);
		} else {
			console = !console;
		}
	}

	@Override
	public void onLogout(Player player) {
		debugs.remove(player.getUniqueId());
	}

	@Override
	public void receive(String pattern, String channel, String packet) {
		if (channel.equals("__sentinel__:hello"))
			return;

		String send = ChatColor.AQUA + "[BukkitDebug : " + channel +"] " + packet;
		for (UUID debug : debugs) {
			Player player = Bukkit.getPlayer(debug);
			if (player != null)
				player.sendMessage(send);
		}

		if (console)
			Bukkit.getConsoleSender().sendMessage(send);
	}
}
