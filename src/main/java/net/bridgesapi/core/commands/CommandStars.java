package net.bridgesapi.core.commands;

import net.bridgesapi.api.BukkitBridge;
import net.bridgesapi.api.player.PlayerData;
import net.bridgesapi.core.APIPlugin;
import net.bridgesapi.core.i18n.I18n;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class CommandStars extends AbstractCommand {

	public CommandStars(APIPlugin plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, String label, String[] arguments) {
		if (arguments.length == 0) {
			if (sender instanceof Player) {
				new Thread(() -> {
					Player player = (Player) sender;
					PlayerData data = BukkitBridge.get().getPlayerManager().getPlayerData(player.getUniqueId());
					if (data != null) {
						player.sendMessage(I18n.getCommandMessage("stars", "account").replace("%STARS%", data.getStars() + ""));
					} else {
						player.sendMessage(ChatColor.RED + I18n.getError("unknown"));
					}
				}, "CommandStarsGet").start();
			} else {
				sender.sendMessage(ChatColor.RED + "Command reserved to players.");
			}

			return true;
		}

		String operation = arguments[0];
		if (operation.equalsIgnoreCase("get")) {
			if (arguments.length < 2)
				return false;

			if (!hasPermission(sender, "stars.getother"))
				return true;

			final String playerName = arguments[1];
			new Thread(() -> {
				UUID playerId = BukkitBridge.get().getUUIDTranslator().getUUID(playerName, true);
				PlayerData data = BukkitBridge.get().getPlayerManager().getPlayerData(playerId);

				if (data != null) {
					sender.sendMessage(I18n.getCommandMessage("stars", "account_other").replace("%PLAYER%", playerName).replace("%STARS%", "" + data.getStars()));
				} else {
					sender.sendMessage(ChatColor.RED + I18n.getError("unknown"));
				}
			}, "CommandStarsGetOther").start();

			return true;
		} else if (operation.equalsIgnoreCase("credit")) {
			if (arguments.length < 3)
				return false;

			if (!hasPermission(sender, "stars.credit"))
				return true;

			final String playerName = arguments[1];
			final String amount = arguments[2];
			new Thread(() -> {
				UUID playerId = BukkitBridge.get().getUUIDTranslator().getUUID(playerName, true);
				PlayerData data = BukkitBridge.get().getPlayerManager().getPlayerData(playerId);

				try {
					long amt = Long.valueOf(amount);
					data.creditStars(amt, I18n.getCommandMessage("stars", "credit_player_message"), false, (newAmount, difference, error) -> sender.sendMessage(I18n.getCommandMessage("stars", "credit").replace("%PLAYER%", playerName).replace("%AMOUNT%", difference + "").replace("%STARS%", newAmount + "")));
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + I18n.getError("number_format"));
				}
			}, "CommandStarsCredit").start();

			return true;
		} else if (operation.equalsIgnoreCase("withdraw")) {
			if (arguments.length < 3)
				return false;

			if (!hasPermission(sender, "stars.withdraw"))
				return true;

			final String playerName = arguments[1];
			final String amount = arguments[2];

			new Thread(() -> {
				UUID playerId = BukkitBridge.get().getUUIDTranslator().getUUID(playerName, true);
				PlayerData data = BukkitBridge.get().getPlayerManager().getPlayerData(playerId);

				try {
					long amt = Long.valueOf(amount);
					data.withdrawStars(amt, (newAmount, difference, error) -> sender.sendMessage(I18n.getCommandMessage("stars", "withdraw").replace("%PLAYER%", playerName).replace("%AMOUNT%", difference + "").replace("%STARS%", newAmount + "")));
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + I18n.getError("number_format"));
				}
			}, "CommandStarsWithdraw").start();

			return true;
		}

		return false;
	}
}
