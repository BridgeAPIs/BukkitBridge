package net.zyuiop.bukkitbridge.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.zyuiop.bridgeconnector.api.BridgeConnector;
import net.zyuiop.bridgeconnector.api.BridgeUtils;
import net.zyuiop.bridgetools.commands.AutoRegisteringCommand;
import net.zyuiop.bukkitbridge.BukkitBridge;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class LobbyCommand extends AutoRegisteringCommand {
  private final BridgeConnector connector;

  public LobbyCommand(BridgeConnector connector) {
    super("lobby", BukkitBridge.instance(), "hub");
    this.connector = connector;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player p)) {
      return false;
    }

    this.connector.utils().sendPlayerToLobby(p.getUniqueId());
    sender.sendMessage(Component.text("Renvoi au lobby...", NamedTextColor.YELLOW));
    return true;
  }
}
