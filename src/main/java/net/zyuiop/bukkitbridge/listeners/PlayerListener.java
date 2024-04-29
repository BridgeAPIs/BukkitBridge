package net.zyuiop.bukkitbridge.listeners;

import net.zyuiop.bridgeconnector.api.BridgeConnector;
import net.zyuiop.bridgeconnector.api.ServerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
  private final BridgeConnector manager;

  public PlayerListener(BridgeConnector manager) {
    this.manager = manager;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onJoin(PlayerJoinEvent event) {
    manager.currentServer().setCurrentPlayers(Bukkit.getOnlinePlayers().size() + 1);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onQuit(PlayerQuitEvent event) {
    manager.currentServer().setCurrentPlayers(Bukkit.getOnlinePlayers().size() - 1);
  }
}
