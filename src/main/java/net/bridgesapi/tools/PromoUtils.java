package net.bridgesapi.tools;

import net.bridgesapi.api.BukkitBridge;
import net.bridgesapi.core.api.player.Promo;
import redis.clients.jedis.Jedis;

public class PromoUtils
{
    public static Promo getCurrentPromo()
    {
        Jedis jedis = BukkitBridge.get().getResource();
        String prom = jedis.get("coins:currentpromo");
        jedis.close();

        if (prom == null)
            return null;
        else
            return new Promo(prom);
    }
}
