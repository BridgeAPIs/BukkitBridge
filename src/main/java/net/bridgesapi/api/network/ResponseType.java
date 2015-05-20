package net.bridgesapi.api.network;

import org.bukkit.ChatColor;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public enum ResponseType {
	ALLOW,
	DENY_OTHER,
	DENY_FULL(ChatColor.RED + "La partie est pleine."),
	DENY_VIPONLY(ChatColor.RED + "La partie est pleine. Devenez "+ChatColor.GREEN+"VIP"+ChatColor.RED + " pour rejoindre."),
	DENY_NOT_READY(ChatColor.RED + "Cette arène n'est pas prête. Merci de patienter."),
	DENY_IN_GAME(ChatColor.RED + "La partie est déjà en cours.");

	ResponseType() {

	}

	private String message = null;

	ResponseType(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
