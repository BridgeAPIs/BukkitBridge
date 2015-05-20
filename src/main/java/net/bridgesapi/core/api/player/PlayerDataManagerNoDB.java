package net.bridgesapi.core.api.player;

import net.bridgesapi.api.player.PlayerData;
import net.bridgesapi.api.player.PlayerDataManager;
import java.util.HashMap;
import java.util.UUID;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class PlayerDataManagerNoDB implements PlayerDataManager {

	protected HashMap<UUID, PlayerDataNoDB> dataHashMap;

	@Override
	public PlayerData getPlayerData(UUID player) {
		if (!dataHashMap.containsKey(player)) {
			PlayerDataNoDB data = new PlayerDataNoDB(player);
			dataHashMap.put(player, data);
		}

		return dataHashMap.get(player);
	}

	@Override
	public PlayerData getPlayerData(UUID player, boolean forceRefresh) {
		return getPlayerData(player);
	}

	@Override
	public void unload(UUID player) {
		dataHashMap.remove(player);
	}
}
