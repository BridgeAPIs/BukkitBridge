package net.bridgesapi.core.api.player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.bridgesapi.api.BukkitBridge;
import net.bridgesapi.core.APIPlugin;
import net.zyuiop.crosspermissions.api.permissions.PermissionUser;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.UUID;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
class CoinsManager {

	protected Promo currentPromo;
	protected Date promoNextCheck = null;
	protected BukkitBridge api;

	public CoinsManager() {
		api = APIPlugin.getApi();
	}

	public Multiplier getCurrentMultiplier(UUID joueur) {
		Date current = new Date();
		Multiplier ret = new Multiplier();

		if (promoNextCheck == null || current.after(promoNextCheck)) {
			Jedis jedis = api.getResource();
			String prom = jedis.get("coins:currentpromo"); // On get la promo
			jedis.close();

			if (prom == null) {
				currentPromo = null;
			} else {
				currentPromo = new Promo(prom);
			}

			promoNextCheck = new Date();
			promoNextCheck.setTime(promoNextCheck.getTime() + (60 * 1000));
		}

		if (currentPromo != null && current.before(currentPromo.end)) {
			ret.globalAmount = currentPromo.multiply;
			ret.infos.put(currentPromo.message, currentPromo.multiply);
		}

		PermissionUser user = BukkitBridge.get().getPermissionsManager().getApi().getUser(joueur);
		int multiply = (user != null && user.getProperty("multiplier") != null) ? Integer.decode(user.getProperty("multiplier")) : 0;

		multiply = (multiply < 1) ? 1 : multiply;

		ret.globalAmount *= multiply;
		return ret;
	}

	public TextComponent getCreditMessage(long amount, String reason, Multiplier multiplier) {
		TextComponent gain = new TextComponent("+" + amount + " Coins ");
		gain.setColor(ChatColor.GOLD);
		gain.addExtra(new ComponentBuilder("(" + reason + ")").color(ChatColor.AQUA).create()[0]);

		if (multiplier != null)
			for (String multCause : multiplier.infos.keySet()) {
				TextComponent line = new TextComponent(multCause);
				line.setColor(ChatColor.GREEN);
				TextComponent details = new TextComponent(" *" + multiplier.infos.get(multCause));
				details.setColor(ChatColor.AQUA);
				line.addExtra(details);

				TextComponent toAdd = new TextComponent(" [");
				toAdd.setColor(ChatColor.GOLD);
				toAdd.addExtra(line);
				toAdd.addExtra(ChatColor.GOLD + "]");

				gain.addExtra(line);
			}


		return gain;
	}

	public TextComponent getCreditMessage(long amount, String reason) {
		TextComponent gain = new TextComponent("+" + amount + " Coins ");
		gain.setColor(ChatColor.GOLD);
		gain.addExtra(new ComponentBuilder("(" + reason + ")").color(ChatColor.AQUA).create()[0]);

		return gain;
	}

	public TextComponent getCreditMessage(long amount) {
		return new TextComponent("+" + amount + " Coins ");
	}
}
