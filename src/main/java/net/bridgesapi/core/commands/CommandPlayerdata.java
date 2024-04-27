package net.bridgesapi.core.commands;

import net.bridgesapi.api.BukkitBridge;
import net.bridgesapi.api.player.PlayerData;
import net.bridgesapi.core.APIPlugin;
import net.bridgesapi.core.i18n.I18n;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class CommandPlayerdata extends AbstractCommand {

	public CommandPlayerdata(APIPlugin plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, String label, String[] arguments) {
		if (!hasPermission(sender, "playerdata.show"))
			return true;

		if (arguments.length == 0) {
			sender.sendMessage(ChatColor.RED + "Usage : /playerdata <name>");
			return true;
		}

		if (arguments.length >= 3 && arguments[0].equalsIgnoreCase("set")) {
			if (!hasPermission(sender, "playerdata.set"))
				return true;

			final String playerName = arguments[1];
			final String key = arguments[2];
			final String value = String.join(" ", Arrays.copyOfRange(arguments, 3, arguments.length));

			new Thread(() -> {
				UUID playerId = BukkitBridge.get().getUUIDTranslator().getUUID(playerName, true);
				PlayerData data = BukkitBridge.get().getPlayerManager().getPlayerData(playerId);
				data.set(key, value);
				sender.sendMessage(I18n.getCommandMessage("playerdata", "updated"));
			}, "CommandPlayerDataSet").start();
			return true;
		}

		if (arguments.length >= 3 && arguments[0].equalsIgnoreCase("del")) {
			if (!hasPermission(sender, "playerdata.del"))
				return true;

			final String playerName = arguments[1];
			final String key = arguments[2];

			new Thread(() -> {
				UUID playerId = BukkitBridge.get().getUUIDTranslator().getUUID(playerName, true);
				PlayerData data = BukkitBridge.get().getPlayerManager().getPlayerData(playerId);
				data.remove(key);
				sender.sendMessage(I18n.getCommandMessage("playerdata", "removed"));
			}, "CommandPlayerDataSet").start();
			return true;
		}

		final String playerName = arguments[0];
			new Thread(() -> {
				UUID playerId = BukkitBridge.get().getUUIDTranslator().getUUID(playerName, true);
				PlayerData data = BukkitBridge.get().getPlayerManager().getPlayerData(playerId);
				sender.sendMessage(ChatColor.YELLOW + "Data for " + ChatColor.GREEN + playerName + ChatColor.YELLOW + " / " + ChatColor.AQUA + playerId);
				for (Map.Entry<String, String> entry : data.getValues().entrySet()) {
					sender.sendMessage(ChatColor.YELLOW + " - " + entry.getKey() + " : " + entry.getValue());
				}
			}, "CommandPlayerData").start();

		return true;
	}
}
