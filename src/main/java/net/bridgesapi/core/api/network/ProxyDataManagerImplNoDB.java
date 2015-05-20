package net.bridgesapi.core.api.network;

import net.bridgesapi.api.network.ProxiedPlayer;
import net.bridgesapi.api.network.ProxyDataManager;

import java.util.*;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class ProxyDataManagerImplNoDB implements ProxyDataManager {
	@Override
	public Set<UUID> getPlayersOnServer(String server) {
		return new HashSet<>();
	}

	@Override
	public Set<UUID> getPlayersOnProxy(String server) {
		return new HashSet<>();
	}

	@Override
	public ProxiedPlayer getProxiedPlayer(UUID uuid) {
		return new ProxiedPlayerNoDB();
	}

	@Override
	public void apiexec(String command, String... args) {

	}

	@Override
	public Map<String, String> getServers() {
		return new HashMap<>();
	}
}
