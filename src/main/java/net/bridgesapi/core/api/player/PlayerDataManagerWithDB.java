package net.bridgesapi.core.api.player;

import net.bridgesapi.api.BukkitBridge;
import net.bridgesapi.api.player.PlayerData;
import net.bridgesapi.api.player.PlayerDataManager;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class PlayerDataManagerWithDB implements PlayerDataManager {

	public PlayerDataManagerWithDB(BukkitBridge api) {
		this.api = api;
		coinsManager = new CoinsManager();
		starsManager = new StarsManager();
	}

	protected BukkitBridge api;
	protected ConcurrentHashMap<UUID, PlayerDataDB> cachedData = new ConcurrentHashMap<>();
	protected CoinsManager coinsManager;
	protected StarsManager starsManager;

	CoinsManager getCoinsManager() {
		return coinsManager;
	}

	StarsManager getStarsManager() {
		return starsManager;
	}

	@Override
	public PlayerData getPlayerData(UUID player) {
		return getPlayerData(player, false);
	}

	@Override
	public PlayerData getPlayerData(UUID player, boolean forceRefresh) {
		if (!cachedData.containsKey(player)) {
			PlayerDataDB data = new PlayerDataDB(player, api, this);
			cachedData.put(player, data);
			return data;
		}

		PlayerDataDB data = cachedData.get(player);

		if (forceRefresh) {
			data.updateData();
			return data;
		}

		data.refreshIfNeeded();
		return data;
	}

	public void update(UUID player) {
		if (!cachedData.containsKey(player)) {
			PlayerDataDB data = new PlayerDataDB(player, api, this);
			cachedData.put(player, data);
			return;
		}

		PlayerDataDB data = cachedData.get(player);
		data.updateData();
	}

	@Override
	public void unload(UUID player) {
		cachedData.remove(player);
	}
}
