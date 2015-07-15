package net.bridgesapi.core.database;

import net.bridgesapi.core.APIPlugin;
import org.bukkit.Bukkit;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class JedisWhitelistRefresher implements Runnable {

	protected APIPlugin                 plugin;
	protected JedisDatabaseConnector databaseConnector;
	protected boolean hasMain = false;
	protected boolean hasCache = false;

	protected JedisWhitelistRefresher(APIPlugin plugin, JedisDatabaseConnector connector) {
		this.plugin = plugin;
		this.databaseConnector = connector;
	}

	public void run() {
		try {
			Jedis jedis = databaseConnector.getResource();
			Set<String> whitelist = jedis.smembers("proxys");
			jedis.close();

			if (!hasMain) {
				hasMain = true;
				Bukkit.getLogger().info("[Database] Successfully connected to master !");
			}

			plugin.refreshIps(whitelist);
		} catch (Exception e) {
			if (hasMain) {
				hasMain = false;
				Bukkit.getLogger().severe("[Database] Disconnected from master !");
			}
		}

		try {
			Jedis jedis = databaseConnector.getBungeeResource();
			jedis.ping();
			jedis.close();

			if (!hasCache) {
				hasCache = true;
				Bukkit.getLogger().info("[Database] Successfully connected to cache !");
			}
		} catch (Exception e) {
			if (hasCache) {
				hasCache = false;
				Bukkit.getLogger().severe("[Database] Disconnected from cache !");
			}
		}
	}

}

