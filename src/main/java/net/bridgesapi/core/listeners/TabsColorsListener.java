package net.bridgesapi.core.listeners;

import net.bridgesapi.api.BukkitBridge;
import net.bridgesapi.core.APIPlugin;
import net.bridgesapi.core.i18n.I18n;
import net.bridgesapi.core.tabcolors.TeamManager;
import net.zyuiop.crosspermissions.api.permissions.PermissionUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class TabsColorsListener extends APIListener {

	protected final TeamManager manager;

	public TabsColorsListener(APIPlugin plugin) {
		super(plugin);

		manager = new TeamManager(plugin);
	}

	String replaceColors(String message) {
		String s = message;
		for (ChatColor color : ChatColor.values()) {
			s = s.replaceAll("(?i)&" + color.getChar(), "" + color);
		}
		return s;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		manager.playerJoin(p); // Passer ça en sync si crash //
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			if (plugin.isDatabaseEnabled()) {
				PermissionUser user = BukkitBridge.get().getPermissionsManager().getApi().getUser(p.getUniqueId());
				final String display = BukkitBridge.get().getPermissionsManager().getDisplay(user);
				String prefix = BukkitBridge.get().getPermissionsManager().getPrefix(user);

				final String displayn = replaceColors(display + "" + prefix) + p.getName();
				p.setDisplayName(displayn);
			} else {
				event.getPlayer().sendMessage(I18n.getError("database_disabled"));
			}
		});

		event.setJoinMessage("");
	}

	@EventHandler
	public void playerQuit(final PlayerQuitEvent event) {
		event.setQuitMessage("");
		manager.playerLeave(event.getPlayer());
		event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}

	@EventHandler
	public void playerKick(final PlayerKickEvent event)
	{
		event.setLeaveMessage("");
		manager.playerLeave(event.getPlayer());
		event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
}
