package net.bridgesapi.core.api.player;

import net.bridgesapi.core.APIPlugin;
import net.md_5.bungee.api.chat.TextComponent;
import net.bridgesapi.api.BukkitBridge;
import net.bridgesapi.api.player.FinancialCallback;
import net.bridgesapi.api.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class PlayerDataDB extends PlayerData {

	protected final BukkitBridge api;
	protected final PlayerDataManagerWithDB manager;

	protected PlayerDataDB(UUID player, BukkitBridge api, PlayerDataManagerWithDB manager) {
		super(player);
		this.api = api;
		this.manager = manager;
		updateData();
	}

	protected void updateData() {
		Jedis jedis = api.getResource();
		Map<String, String> data = jedis.hgetAll("player:" + playerID);
		jedis.close();

		this.playerData = data;
		this.lastRefresh = new Date();
	}

	protected void refreshIfNeeded() {
		if ((lastRefresh.getTime() + (1000 * 60 * 5)) < System.currentTimeMillis()) {
			APIPlugin.getInstance().getExecutor().addTask(this::updateData);
		}
	}

	@Override
	public String get(String key) {
		refreshIfNeeded();
		return super.get(key);
	}

	@Override
	public Set<String> getKeys() {
		refreshIfNeeded();
		return super.getKeys();
	}

	@Override
	public Map<String, String> getValues() {
		refreshIfNeeded();
		return super.getValues();
	}

	@Override
	public boolean contains(String key) {
		refreshIfNeeded();
		return super.contains(key);
	}

	@Override
	public void set(String key, String value) {
		this.playerData.put(key, value);

		Jedis jedis = api.getResource();
		jedis.hset("player:" + playerID, key, value);
		jedis.close();
	}

	@Override
	public void remove(String key) {
		playerData.remove(key);

		Jedis jedis = api.getResource();
		jedis.hdel("player:" + playerID, key);
		jedis.close();
	}

	@Override
	public void setInt(String key, int value) {
		set(key, String.valueOf(value));
	}

	@Override
	public void setBoolean(String key, boolean value) {
		set(key, String.valueOf(value));
	}

	@Override
	public void setDouble(String key, double value) {
		set(key, String.valueOf(value));
	}

	@Override
	public void setLong(String key, long value) {
		set(key, String.valueOf(value));
	}

	@Override
	public long increaseCoins(long incrBy) {
		Jedis jedis = api.getResource();
		long newValue = jedis.hincrBy("player:" + playerID, "coins", incrBy);
		jedis.close();

		playerData.put("coins", String.valueOf(newValue));
		return newValue;
	}

	@Override
	public long decreaseCoins(long decrBy) {
		return increaseCoins(-decrBy);
	}

	void displayMessage(TextComponent message) {
		Player player = Bukkit.getPlayer(playerID);
		if (player != null)
			player.sendMessage(message.toLegacyText());
	}

	@Override
	public void creditCoins(final long famount, final String reason, final boolean applyMultiplier, final FinancialCallback<Long> financialCallback) {
		new Thread(() -> {
			long amount = famount;
			TextComponent message = null;
			if (applyMultiplier) {
				Multiplier multiplier = manager.getCoinsManager().getCurrentMultiplier(playerID);
				amount *= multiplier.globalAmount;

				message = ((reason != null) ? manager.getCoinsManager().getCreditMessage(amount, reason, multiplier) : manager.getCoinsManager().getCreditMessage(amount));
			} else {
				message = ((reason != null) ? manager.getCoinsManager().getCreditMessage(amount, reason) : manager.getCoinsManager().getCreditMessage(amount));
			}

			displayMessage(message);

			long result = increaseCoins(amount);

			if (financialCallback != null)
				financialCallback.done(result, amount, null);

		}, "CreditCoinsThread").start();
	}

	@Override
	public void withdrawCoins(final long famount, final FinancialCallback<Long> financialCallback) {
		new Thread(() -> {
			long result = decreaseCoins(famount);
			if (financialCallback != null)
				financialCallback.done(result, -famount, null);

		}, "WithdrawCoinsThread").start();
	}

	@Override
	public long increaseStars(long incrBy) {
		Jedis jedis = api.getResource();
		long newValue = jedis.hincrBy("player:" + playerID, "stars", incrBy);
		jedis.close();

		playerData.put("stars", String.valueOf(newValue));
		return newValue;
	}

	@Override
	public long decreaseStars(long decrBy) {
		return increaseStars(-decrBy);
	}

	@Override
	public void creditStars(final long famount, final String reason, final boolean applyMultiplier, FinancialCallback<Long> financialCallback) {
		new Thread(() -> {
			long amount = famount;
			TextComponent message = null;
			if (applyMultiplier) {
				Multiplier multiplier = manager.getStarsManager().getCurrentMultiplier(playerID);
				amount *= multiplier.globalAmount;

				message = ((reason != null) ? manager.getStarsManager().getCreditMessage(amount, reason, multiplier) : manager.getStarsManager().getCreditMessage(amount));
			} else {
				message = ((reason != null) ? manager.getStarsManager().getCreditMessage(amount, reason) : manager.getStarsManager().getCreditMessage(amount));
			}

			displayMessage(message);

			long result = increaseStars(amount);

			if (financialCallback != null)
				financialCallback.done(result, amount, null);

		}, "CreditStarsThread").start();
	}

	@Override
	public void withdrawStars(final long amount, FinancialCallback<Long> financialCallback) {
		new Thread(() -> {
			long result = decreaseStars(amount);

			if (financialCallback != null)
				financialCallback.done(result, -amount, null);

		}, "WithdrawStarsThread").start();
	}

}
