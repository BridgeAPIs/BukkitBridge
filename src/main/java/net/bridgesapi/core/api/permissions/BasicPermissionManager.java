package net.bridgesapi.core.api.permissions;

import net.bridgesapi.api.BukkitBridge;
import net.bridgesapi.api.permissions.PermissionsManager;
import net.bridgesapi.core.APIPlugin;
import net.zyuiop.crosspermissions.api.PermissionsAPI;
import net.zyuiop.crosspermissions.api.rawtypes.RawPlayer;
import net.zyuiop.crosspermissions.api.rawtypes.RawPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public abstract class BasicPermissionManager implements RawPlugin, PermissionsManager {

	private final boolean isLobby;

	public BasicPermissionManager() {
		isLobby = BukkitBridge.get().getServerName().startsWith("Lobby");
		Bukkit.getLogger().info("Lobby mode was set to : " + isLobby);

		logInfo(">> LOADING PERMISSIONS API !");
		api = new PermissionsAPI(this, "Joueur", () -> BukkitBridge.get().getResource());
		api.getManager().refreshGroups();
		logInfo(">> LOADED PERMISSIONS API !");

		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListeners(this), APIPlugin.getInstance());
	}

	public void disable() {
		tasks.stream().filter(Objects::nonNull).forEach(org.bukkit.scheduler.BukkitTask::cancel);
		logInfo("Cancelled tasks successfully.");
	}

	public boolean isLobby() {
		return isLobby;
	}

	protected PermissionsAPI api = null;
	protected HashMap<UUID, VirtualPlayer> players = new HashMap<>();
	protected ArrayList<BukkitTask> tasks = new ArrayList<>();

	public PermissionsAPI getApi() {
		return api;
	}

	@Override
	public void logSevere(String log) {
		APIPlugin.log(Level.SEVERE, "[PERM] " + log);
	}

	@Override
	public void logWarning(String log) {
		APIPlugin.log(Level.WARNING, "[PERM] " + log);
	}

	@Override
	public void logInfo(String log) {
		APIPlugin.log(Level.INFO, "[PERM] " + log);
	}

	@Override
	public void runRepeatedTaskAsync(Runnable task, long delay, long timeBeforeRun) {
		tasks.add(Bukkit.getScheduler().runTaskTimerAsynchronously(APIPlugin.getInstance(), task, timeBeforeRun, delay));
	}

	@Override
	public void runAsync(Runnable task) {
		Bukkit.getScheduler().runTaskAsynchronously(APIPlugin.getInstance(), task);
	}

	@Override
	public boolean isOnline(UUID player) {
		Player p = Bukkit.getPlayer(player);
		return (p != null && p.isOnline());
	}

	@Override
	public RawPlayer getPlayer(UUID player) {
		Player p = Bukkit.getPlayer(player);
		if (p == null)
			return null;
		if (players.containsKey(player))
			return players.get(player);

		VirtualPlayer pl = new VirtualPlayer(p);
		players.put(player, pl);
		return pl;
	}

	@Override
	public UUID getPlayerId(String s) {
		return BukkitBridge.get().getUUIDTranslator().getUUID(s);
	}

	@Override
	public String getPlayerName(UUID uuid) {
		return BukkitBridge.get().getUUIDTranslator().getName(uuid);
	}

	@Override
	public boolean hasPermission(Player player, String permission) {
		return hasPermission(player.getUniqueId(), permission);
	}

	@Override
	public boolean hasPermission(CommandSender sender, String permission) {
		if (sender instanceof ConsoleCommandSender)
			return true;
		if (sender instanceof Player)
			return hasPermission((Player) sender, permission);
		return false;
	}
}
