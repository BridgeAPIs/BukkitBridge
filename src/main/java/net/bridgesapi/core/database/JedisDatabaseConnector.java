package net.bridgesapi.core.database;

import net.bridgesapi.core.APIPlugin;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import redis.clients.jedis.*;

import java.util.Set;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class JedisDatabaseConnector implements DatabaseConnector {

	protected JedisPool          mainPool;
	protected JedisPool          cachePool;

	protected APIPlugin          plugin;
	protected String             password;

	protected HostAndPort mainIp;
	protected HostAndPort cacheIp;

	private   JedisWhitelistRefresher keeper;
	private   BukkitTask         keepTask;

	public JedisDatabaseConnector(APIPlugin plugin, String main, String cache, String auth) {
		this.plugin = plugin;
		this.mainIp = get(main);
		this.cacheIp = get(cache);
		this.password = auth;

		initiateConnections();
	}

	private HostAndPort get(String ip) {
		String[] parts = StringUtils.split(ip, ":");
		if (parts.length >= 2)
			return new HostAndPort(parts[0], Integer.parseInt(parts[1]));
		return new HostAndPort(parts[0], 6379);
	}

	public Jedis getResource() {
		return mainPool.getResource();
	}

	public Jedis getBungeeResource() {
		return cachePool.getResource();
	}

	public void killConnections() {
		cachePool.destroy();
		mainPool.destroy();
		keepTask.cancel();
	}

	public void initiateConnections() {
		// Préparation de la connexion
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(1024);
		config.setMaxWaitMillis(5000);

		if (password.length() == 0) {
			this.mainPool = new JedisPool(config, mainIp.getHost(), mainIp.getPort(), 5000);
			this.mainPool = new JedisPool(config, cacheIp.getHost(), cacheIp.getPort(), 5000);
		} else {
			this.mainPool = new JedisPool(config, mainIp.getHost(), mainIp.getPort(), 5000, password);
			this.mainPool = new JedisPool(config, cacheIp.getHost(), cacheIp.getPort(), 5000, password);
		}

		// Init du thread

		if (keeper == null) {
			keeper = new JedisWhitelistRefresher(plugin, this);
			keepTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, keeper, 3*20, 30*20);
		}
	}

	protected String fastGet(String key) {
		Jedis jedis = getResource();
		String val = jedis.get(key);
		jedis.close();
		return val;
	}

}
