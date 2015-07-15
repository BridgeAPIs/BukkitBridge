package net.bridgesapi.api;

import net.bridgesapi.api.channels.PubSubAPI;
import net.bridgesapi.api.names.UUIDTranslator;
import net.bridgesapi.api.network.JoinManager;
import net.bridgesapi.api.network.ProxyDataManager;
import net.bridgesapi.api.parties.PartiesManager;
import net.bridgesapi.api.permissions.PermissionsManager;
import net.bridgesapi.api.player.PlayerDataManager;
import net.bridgesapi.api.resourcepacks.ResourcePacksManager;
import net.bridgesapi.api.settings.SettingsManager;
import net.bridgesapi.api.shops.ShopsManager;
import net.bridgesapi.api.stats.StatsManager;
import redis.clients.jedis.Jedis;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public abstract class BukkitBridge {

	private static BukkitBridge instance;

	protected BukkitBridge() {
		instance = this;
	}

	public static BukkitBridge get() {
		return instance;
	}

	/**
	 * This method returns a ShardedJedis resource, which allows the user to do operations into the database.
	 * The connexion returned MUST be closed using <code>ShardedJedis.close()</code>
	 * @return A shardedJedis database connexion
	 */
	public abstract Jedis getResource();

	/**
	 * This method returns a Jedis object, representing a connexion to the proxies database. This database mainly contains data from redisbungee.
	 * Don't forget to close the connexion after use
	 * @return A database connexion
	 */
	public abstract Jedis getBungeeResource();

	public abstract String getServerName();

	public abstract StatsManager getStatsManager(String game);
	public abstract ShopsManager getShopsManager(String game);
	public abstract SettingsManager getSettingsManager();
	public abstract PlayerDataManager getPlayerManager();
	public abstract PubSubAPI getPubSub();
	public abstract UUIDTranslator getUUIDTranslator();
	public abstract ResourcePacksManager getResourcePacksManager();

	public abstract ProxyDataManager getProxyDataManager();
	public abstract PartiesManager getPartiesManager();

	public abstract PermissionsManager getPermissionsManager();

	public abstract JoinManager getJoinManager();

}
