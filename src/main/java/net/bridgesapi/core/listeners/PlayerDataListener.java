package net.bridgesapi.core.listeners;

import net.bridgesapi.core.APIPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class PlayerDataListener extends APIListener {

	public PlayerDataListener(APIPlugin plugin) {
		super(plugin);
	}

	@EventHandler
	public void onLogout(PlayerQuitEvent event) {
		api.getPlayerManager().unload(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onLogout(PlayerKickEvent event) {
		api.getPlayerManager().unload(event.getPlayer().getUniqueId());
	}
}
