package net.bridgesapi.core.api.network;

import net.md_5.bungee.api.ChatColor;
import net.bridgesapi.api.BukkitBridge;
import net.bridgesapi.api.network.JoinHandler;
import net.bridgesapi.api.network.JoinManager;
import net.bridgesapi.api.network.JoinResponse;
import net.bridgesapi.core.APIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * How does that work ?
 *
 * A. The proxy send a request OR the client connect without request
 * -> requestJoin is called
 * -> the system checks if the player is or isn't in a party
 *    -> He's in party : call partyJoin
 *    -> He's not in party : call soloJoin
 * -> if the player is already connected :
 *    -> Connexion refused : he's kicked
 * -> if the player just sent a request
 *    -> Connection refused : the system sends him a message
 *    -> Connection accepted : the system request the proxy to moove him
 */

public class JoinManagerImplement implements JoinManager, Listener {

    protected TreeMap<Integer, JoinHandler> handlerTreeMap = new TreeMap<>();
    protected HashSet<UUID> moderatorsExpected = new HashSet<>();
    protected HashSet<UUID> playersExpected = new HashSet<>();
    protected boolean isPartyLimited;

    public JoinManagerImplement() {
        isPartyLimited = ! BukkitBridge.get().getServerName().startsWith("Lobby");
    }

    public void setPartyLimited(boolean value) {
        this.isPartyLimited = value;
    }

    public boolean isPartyLimited() { // This method can be overrided
        return isPartyLimited;
    }

    @Override
    public void registerHandler(JoinHandler handler, int priority) {
        this.handlerTreeMap.put(priority, handler);
    }

    @Override
    public int countExpectedPlayers() {
        return getExpectedPlayers().size();
    }

    @Override
    public HashSet<UUID> getExpectedPlayers() {
        return playersExpected;
    }


    JoinResponse requestSoloJoin(UUID player) {
        JoinResponse response = new JoinResponse();
        for (JoinHandler handler : handlerTreeMap.values())
            response = handler.requestJoin(player, response);

        if (response.isAllowed()) {
            playersExpected.add(player);
            Bukkit.getScheduler().runTaskLater(APIPlugin.getInstance(), () -> playersExpected.remove(player), 20 * 15L);
        }
        return response;
    }

    JoinResponse requestPartyJoin(UUID partyID, HashSet<UUID> dontMove) {
        UUID leader = BukkitBridge.get().getPartiesManager().getLeader(partyID);
        Set<UUID> members = BukkitBridge.get().getPartiesManager().getPlayersInParty(partyID).keySet();

        JoinResponse response = new JoinResponse();
        for (JoinHandler handler : handlerTreeMap.values())
            response = handler.requestPartyJoin(leader, members, response);

        if (response.isAllowed()) {
            members.removeAll(dontMove);
            for (UUID player : members) {
                playersExpected.add(player);
                Bukkit.getScheduler().runTaskLater(APIPlugin.getInstance(), () -> playersExpected.remove(player), 20 * 15L);
                BukkitBridge.get().getProxyDataManager().getProxiedPlayer(player).connect(BukkitBridge.get().getServerName());
            }

            new Thread(() -> {
                Jedis jedis = BukkitBridge.get().getBungeeResource();
                jedis.set("party:" + partyID + ":server", BukkitBridge.get().getServerName());
                jedis.close();
            }, "PartyUpdater").start();
        }

        return response;
    }

    JoinResponse requestPartyJoin(UUID partyID) {
        return requestPartyJoin(partyID, new HashSet<>());
    }

    public JoinResponse requestJoin(UUID player) {
        return requestJoin(player, false);
    }

    public JoinResponse requestJoin(UUID player, boolean alreadyConnected) {
        return requestSoloJoin(player);
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        UUID player = event.getUniqueId();

        if (moderatorsExpected.contains(player)) // On traite après
            return;

        if (!playersExpected.contains(player)) {
            JoinResponse response = requestJoin(event.getUniqueId(), true);
            if (!response.isAllowed()) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + response.getReason());
                return;
            }
        }

        playersExpected.remove(player);

        for (JoinHandler handler : handlerTreeMap.values())
            handler.onLogin(player);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (moderatorsExpected.contains(player.getUniqueId())) {
            for (JoinHandler handler : handlerTreeMap.values())
                handler.onModerationJoin(player);

            return;
        }

        for (JoinHandler handler : handlerTreeMap.values())
            handler.finishJoin(player);

		// Enregistrement du joueur
		APIPlugin.getInstance().getExecutor().addTask(() -> {
            Jedis jedis = BukkitBridge.get().getBungeeResource();
            jedis.sadd("connectedonserv:" + APIPlugin.getInstance().getServerName(), player.getUniqueId().toString());
            jedis.close();
        });
    }

    @EventHandler
    public void onLogout(final PlayerQuitEvent event) {
        if (moderatorsExpected.contains(event.getPlayer().getUniqueId()))
            moderatorsExpected.remove(event.getPlayer().getUniqueId());

        for (JoinHandler handler : handlerTreeMap.values())
            handler.onLogout(event.getPlayer());

		APIPlugin.getInstance().getExecutor().addTask(() -> {
            Jedis jedis = BukkitBridge.get().getBungeeResource();
            jedis.srem("connectedonserv:" + APIPlugin.getInstance().getServerName(), event.getPlayer().getUniqueId().toString());
            jedis.close();
        });
    }

    public void addModerator(UUID moderator) {
        moderatorsExpected.add(moderator);
    }
}
