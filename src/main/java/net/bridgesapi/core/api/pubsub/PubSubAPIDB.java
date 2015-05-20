package net.bridgesapi.core.api.pubsub;

import net.bridgesapi.api.BukkitBridge;
import net.bridgesapi.api.channels.PacketsReceiver;
import net.bridgesapi.api.channels.PatternReceiver;
import net.bridgesapi.api.channels.PendingMessage;
import net.bridgesapi.api.channels.PubSubAPI;
import net.bridgesapi.core.APIPlugin;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class PubSubAPIDB implements PubSubAPI {

	private BukkitBridge api;
	private Subscriber subscriber;
	private Sender sender;
	private boolean continueSub = true;

	public PubSubAPIDB(BukkitBridge api) {
		this.api = api;
		subscriber = new Subscriber();
		new Thread(() -> {
			while (continueSub) {
				Jedis jedis = api.getResource();
				try {
					jedis.psubscribe(subscriber, "*");
					subscriber.registerPattern("*", APIPlugin.getInstance().getDebugListener());
				} catch (Exception e) {
					e.printStackTrace();
				}

				Bukkit.getLogger().info("Disconnected from master.");
				jedis.close();
			}
		}).start();

		Bukkit.getLogger().info("Waiting for subscribing...");
		while (!subscriber.isSubscribed())
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		Bukkit.getLogger().info("Correctly subscribed.");

		sender = new Sender(api);
		new Thread(sender, "SenderThread").start();
	}

	@Override
	public void subscribe(String channel, PacketsReceiver receiver) {
		subscriber.registerReceiver(channel, receiver);
	}

	@Override
	public void subscribe(String pattern, PatternReceiver receiver) {
		subscriber.registerPattern(pattern, receiver);
	}

	@Override
	public void send(String channel, String message) {
		sender.publish(new PendingMessage(channel, message));
	}

	@Override
	public void send(PendingMessage message) {
		sender.publish(message);
	}

	@Override
	public net.bridgesapi.api.channels.Sender getSender() {
		return sender;
	}

	public void disable() {
		continueSub = false;
		subscriber.unsubscribe();
		subscriber.punsubscribe();
		try {
			Thread.sleep(500);
		} catch (Exception ignored) {}
	}
}
