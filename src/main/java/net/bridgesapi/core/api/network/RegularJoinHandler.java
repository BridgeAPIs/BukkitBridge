package net.bridgesapi.core.api.network;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.bridgesapi.api.BukkitBridge;
import net.bridgesapi.api.channels.PacketsReceiver;
import net.bridgesapi.api.network.JoinResponse;

import java.util.UUID;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class RegularJoinHandler implements PacketsReceiver {

	private final JoinManagerImplement manager;

	public RegularJoinHandler(JoinManagerImplement manager) {
		this.manager = manager;
	}

	@Override
	public void receive(String channel, String packet) {
		UUID player = UUID.fromString(packet);
		JoinResponse response = manager.requestJoin(player);
		if (!response.isAllowed()) {
			TextComponent component = new TextComponent(response.getReason());
			component.setColor(ChatColor.RED);
			BukkitBridge.get().getProxyDataManager().getProxiedPlayer(player).sendMessage(component);
		} else {
			BukkitBridge.get().getProxyDataManager().getProxiedPlayer(player).connect(BukkitBridge.get().getServerName());
		}
	}
}
