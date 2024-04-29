package net.zyuiop.bukkitbridge;

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

  public void declareGameServer(String game, String variant) {
    this.connector.asGameServer(game, variant);
  }
}
