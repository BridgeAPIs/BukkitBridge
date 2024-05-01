package net.zyuiop.bukkitbridge.commands;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import javax.naming.Name;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.zyuiop.bridgeconnector.api.BridgeConnector;
import net.zyuiop.bridgetools.commands.AutoRegisteringCommand;
import net.zyuiop.bukkitbridge.BukkitBridge;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageCommand extends AutoRegisteringCommand {
  private final BridgeConnector connector;

  public MessageCommand(BridgeConnector connector) {
    super("message", BukkitBridge.instance(), "msg", "t", "tell", "whisper", "w");
    this.connector = connector;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player p)) {
      return false;
    }

    // TODO: block player from sending messages to themselves
    if (args.length < 2) {
      sender.sendMessage(Component.text("Format: /" + label + " <joueur> <message>", NamedTextColor.RED));
      return false;
    }

    var uuid = args[0].matches("^[0-9A-Za-z_-]{1,25}$") ? connector.uuidCache().getUUIDFromUsername(args[0]) : Optional.<UUID>empty();
    if (uuid.isEmpty()) {
      sender.sendMessage(Component.text("Ce joueur n'a pas été trouvé.", NamedTextColor.RED));
      return false;
    }

    if (uuid.get().equals(p.getUniqueId())) {
      sender.sendMessage(Component.text("Vous ne pouvez pas vous écrire à vous même.", NamedTextColor.RED));
      return false;
    }

    var textMessage =
        Component.text()
            .append(
                Component.text("[", NamedTextColor.AQUA),
                p.displayName().color(NamedTextColor.YELLOW),
                Component.text(" -> ", NamedTextColor.AQUA),
                Component.text(args[0]).color(NamedTextColor.YELLOW),
                Component.text("] ", NamedTextColor.AQUA),
                Component.text(String.join(" ", Arrays.copyOfRange(args, 1, args.length)), NamedTextColor.GRAY)
            ).build();

    if (!this.connector.utils().sendMessageToPlayer(uuid.get(), textMessage)) {
      sender.sendMessage(Component.text("Ce joueur n'a pas été trouvé, peut être est il hors ligne ?", NamedTextColor.RED));
      return false;
    }

    sender.sendMessage(textMessage);
    return true;
  }
}
