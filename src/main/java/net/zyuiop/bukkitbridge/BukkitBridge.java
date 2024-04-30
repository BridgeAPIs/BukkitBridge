package net.zyuiop.bukkitbridge;

import net.zyuiop.bridgeconnector.api.BridgeConnector;
import net.zyuiop.bukkitbridge.commands.LobbyCommand;
import org.bukkit.plugin.ServicePriority;
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
  }

  public BridgeConnector connector() {
    return this.connector;
  }

  @Override
  public void onEnable() {
    this.connector = new BridgeImpl(this);
    this.getServer().getServicesManager().register(BridgeConnector.class, this.connector, this, ServicePriority.High);

    // Commands
    new LobbyCommand(this.connector);
  }
}
