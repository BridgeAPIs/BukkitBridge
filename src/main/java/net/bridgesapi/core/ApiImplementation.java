package net.bridgesapi.core;

import net.bridgesapi.api.BukkitBridge;
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
import net.bridgesapi.core.api.names.UUIDTranslatorDB;
import net.bridgesapi.core.api.names.UUIDTranslatorNODB;
import net.bridgesapi.core.api.network.*;
import net.bridgesapi.core.api.parties.PartiesManagerNoDb;
import net.bridgesapi.core.api.parties.PartiesManagerWithDB;
import net.bridgesapi.core.api.permissions.BasicPermissionManager;
import net.bridgesapi.core.api.permissions.PermissionsManagerDB;
import net.bridgesapi.core.api.permissions.PermissionsManagerNoDB;
import net.bridgesapi.core.api.player.PlayerDataManagerNoDB;
import net.bridgesapi.core.api.player.PlayerDataManagerWithDB;
import net.bridgesapi.core.api.pubsub.PubSubAPIDB;
import net.bridgesapi.core.api.pubsub.PubSubNoDB;
import net.bridgesapi.core.api.resourcepacks.ResourcePacksManagerImpl;
import net.bridgesapi.core.api.settings.SettingsManagerDB;
import net.bridgesapi.core.api.settings.SettingsManagerNoDB;
import net.bridgesapi.core.api.shops.ShopsManagerDB;
import net.bridgesapi.core.api.shops.ShopsManagerNoDB;
import net.bridgesapi.core.api.stats.StatsManagerDB;
import net.bridgesapi.core.api.stats.StatsManagerNoDB;
import net.bridgesapi.core.database.DatabaseConnector;
import net.bridgesapi.core.listeners.GlobalChannelHandler;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class ApiImplementation extends BukkitBridge
{

	protected APIPlugin plugin;
	protected boolean database;
	protected SettingsManager settingsManager;
	protected PlayerDataManager playerDataManager;
	protected PubSubAPI pubSub;
	protected UUIDTranslator uuidTranslator;
	protected JoinManager joinManager;
	protected ProxyDataManager proxyDataManager;
	protected PartiesManager partiesManager;
	protected ResourcePacksManager resourcePacksManager;
	protected BasicPermissionManager permissionsManager;

	public ApiImplementation(APIPlugin plugin, boolean database) {
		this.plugin = plugin;
		this.database = database;

		JoinManagerImplement implement = new JoinManagerImplement();
		Bukkit.getServer().getPluginManager().registerEvents(implement, plugin);
		this.joinManager = implement;
		resourcePacksManager = new ResourcePacksManagerImpl();

		if (database) {
			settingsManager = new SettingsManagerDB(this);
			playerDataManager = new PlayerDataManagerWithDB(this);
			pubSub = new PubSubAPIDB(this);
			pubSub.subscribe("global", new GlobalChannelHandler(plugin));
			pubSub.subscribe(plugin.getServerName(), new GlobalChannelHandler(plugin));

			pubSub.subscribe("commands.servers." + getServerName(), new RemoteCommandsHandler());
			pubSub.subscribe("commands.servers.all", new RemoteCommandsHandler());

			ModerationJoinHandler moderationJoinHandler = new ModerationJoinHandler(implement);
			implement.registerHandler(moderationJoinHandler, - 1);
			BukkitBridge.get().getPubSub().subscribe(plugin.getServerName(), moderationJoinHandler);
			pubSub.subscribe("partyjoin." + getServerName(), new PartiesPubSub(implement));
			pubSub.subscribe("join." + getServerName(), new RegularJoinHandler(implement));

			uuidTranslator = new UUIDTranslatorDB(plugin, this);
			proxyDataManager = new ProxyDataManagerImplDB(this);
			partiesManager = new PartiesManagerWithDB(this);
			permissionsManager = new PermissionsManagerDB();
		} else {
			settingsManager = new SettingsManagerNoDB();
			playerDataManager = new PlayerDataManagerNoDB();
			pubSub = new PubSubNoDB();
			uuidTranslator = new UUIDTranslatorNODB();
			ModerationJoinHandler moderationJoinHandler = new ModerationJoinHandler(implement);
			implement.registerHandler(moderationJoinHandler, - 1);
			BukkitBridge.get().getPubSub().subscribe(plugin.getServerName(), moderationJoinHandler);
			proxyDataManager = new ProxyDataManagerImplNoDB();
			partiesManager = new PartiesManagerNoDb();
			permissionsManager = new PermissionsManagerNoDB();
		}
	}

	@Override
	public PermissionsManager getPermissionsManager() {
		return permissionsManager;
	}

	@Override
	public ResourcePacksManager getResourcePacksManager() {
		return resourcePacksManager;
	}


	public APIPlugin getPlugin() {
		return plugin;
	}

	public ProxyDataManager getProxyDataManager() {
		return proxyDataManager;
	}

	public void replaceJoinManager(JoinManager manager) {
		this.joinManager = manager;
	}

	@Override
	public PartiesManager getPartiesManager() {
		return partiesManager;
	}

	public JoinManager getJoinManager() {
		return joinManager;
	}

	public Jedis getResource() {
		return plugin.databaseConnector.getResource();
	}

	public StatsManager getStatsManager(String game) {
		if (database)
			return new StatsManagerDB(game, plugin);
		else
			return new StatsManagerNoDB(game, plugin);
	}

	@Override
	public ShopsManager getShopsManager(String game) {
		if (database)
			return new ShopsManagerDB(game, this);
		else
			return new ShopsManagerNoDB(game, this);
	}

	@Override
	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	@Override
	public PlayerDataManager getPlayerManager() {
		return playerDataManager;
	}

	@Override
	public PubSubAPI getPubSub() {
		return pubSub;
	}

	@Override
	public UUIDTranslator getUUIDTranslator() {
		return uuidTranslator;
	}

	public Jedis getBungeeResource() {
		return plugin.databaseConnector.getBungeeResource();
	}

	@Override
	public String getServerName() {
		return plugin.getServerName();
	}

	public DatabaseConnector getDatabase() {
		return plugin.databaseConnector;
	}

	protected void disable() {
		if (database) {
			((PubSubAPIDB) pubSub).disable();
			plugin.databaseConnector.killConnections();
		}
	}


}
