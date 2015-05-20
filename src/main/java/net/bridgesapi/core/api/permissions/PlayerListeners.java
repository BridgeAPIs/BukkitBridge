package net.bridgesapi.core.api.permissions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.UUID;

/**
 * Created by zyuiop on 27/08/14.
 */
public class PlayerListeners implements Listener {

    protected BasicPermissionManager plugin;

    public PlayerListeners(BasicPermissionManager plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(final AsyncPlayerPreLoginEvent ev) {
        plugin.api.getManager().getUser(ev.getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(final PlayerJoinEvent ev) {
        if (!plugin.isLobby())
            plugin.api.getManager().refreshPerms(ev.getPlayer().getUniqueId());
        else
            plugin.runAsync(() -> plugin.api.getManager().refreshPerms(ev.getPlayer().getUniqueId()));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLeave(PlayerQuitEvent ev) {
        disconnect(ev.getPlayer().getUniqueId());
    }

    public void disconnect(UUID player) {
        plugin.players.remove(player);
    }

    @EventHandler
    public void onKick(PlayerKickEvent ev) {
        disconnect(ev.getPlayer().getUniqueId());
    }

}
