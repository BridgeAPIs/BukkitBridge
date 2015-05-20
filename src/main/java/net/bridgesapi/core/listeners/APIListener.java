package net.bridgesapi.core.listeners;

import net.bridgesapi.api.BukkitBridge;
import net.bridgesapi.core.APIPlugin;
import org.bukkit.event.Listener;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public abstract class APIListener implements Listener {
	protected APIPlugin plugin;
	protected BukkitBridge api;

	public APIListener(APIPlugin plugin) {
		this.plugin = plugin;
		this.api = APIPlugin.getApi();
	}
}
