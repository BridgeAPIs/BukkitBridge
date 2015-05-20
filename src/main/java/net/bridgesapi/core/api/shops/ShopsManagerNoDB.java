package net.bridgesapi.core.api.shops;

import net.bridgesapi.api.BukkitBridge;
import net.bridgesapi.api.shops.ShopsManager;

import java.util.List;
import java.util.UUID;

/**
 * This file is a part of the SamaGames Project CodeBase
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2014 & 2015
 * All rights reserved.
 */
public class ShopsManagerNoDB extends ShopsManager {

	public ShopsManagerNoDB(String gameType, BukkitBridge api) {
		super(gameType, api);
	}

	@Override
	public String getItemLevelForPlayer(UUID player, String item) {
		return null;
	}

	@Override
	public List<String> getOwnedLevels(UUID player, String item) {
		return null;
	}

	@Override
	public void addOwnedLevel(UUID player, String item, String itemLevel) {

	}

	@Override
	public void setCurrentLevel(UUID player, String item, String level) {

	}


}
