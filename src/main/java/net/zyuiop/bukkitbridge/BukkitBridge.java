package net.zyuiop.bukkitbridge;

import net.zyuiop.bridgeconnector.api.BridgeConnector;
import net.zyuiop.bukkitbridge.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitBridge extends JavaPlugin {
  private static BukkitBridge instance;
  private BridgeImpl connector;

  public static BukkitBridge instance() {
    return instance;
  }

  public BukkitBridge() {
    super();
    instance = this;
  }

  @Override
  public void onLoad() {
    super.onLoad();
    this.saveDefaultConfig();

    this.connector = new BridgeImpl(this);

  }

  public BridgeConnector connector() {
    return this.connector;
  }

  @Override
  public void onEnable() {
    this.getServer().getPluginManager().registerEvents(new PlayerListener(this.connector), this);
  }
}
