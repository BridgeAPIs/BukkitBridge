package net.bridgesapi.core.api.network;

import net.md_5.bungee.api.chat.TextComponent;
import net.bridgesapi.api.BukkitBridge;
import net.bridgesapi.api.network.ProxiedPlayer;
import com.google.gson.Gson;

import java.util.UUID;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class ProxiedPlayerDB implements ProxiedPlayer {

	private final UUID playerId;

	public ProxiedPlayerDB(UUID playerId) {
		this.playerId = playerId;
	}

	@Override
	public String getServer() {
		return BukkitBridge.get().getPlayerManager().getPlayerData(playerId).get("currentserver", "Inconnu");
	}

	@Override
	public String getProxy() {
		return BukkitBridge.get().getPlayerManager().getPlayerData(playerId).get("currentproxy", "Inconnu");
	}

	@Override
	public void disconnect(TextComponent reason) {
		BukkitBridge.get().getPubSub().send("apiexec.kick", playerId + " " + new Gson().toJson(reason));
	}

	@Override
	public void connect(String server) {
		BukkitBridge.get().getPubSub().send("apiexec.connect", playerId + " " + server);
	}

	@Override
	public void connectGame(String game) {
		BukkitBridge.get().getPubSub().send("join." + game, playerId + "");
	}

	@Override
	public void sendMessage(TextComponent component) {
		BukkitBridge.get().getPubSub().send("apiexec.message", playerId + " " + new Gson().toJson(component));
	}
}
