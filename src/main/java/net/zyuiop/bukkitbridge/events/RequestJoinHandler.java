package net.zyuiop.bukkitbridge.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.zyuiop.bridgeconnector.api.BridgeConnector;
import net.zyuiop.bridgeconnector.api.pubsub.PubSubPacketHandler;
import net.zyuiop.bridgeconnector.api.pubsub.packets.players.ServerJoinRequest;
import net.zyuiop.bukkitbridge.BukkitBridge;
import org.bukkit.Bukkit;

public class RequestJoinHandler {
  private final BridgeConnector connector;

  public RequestJoinHandler(BridgeConnector connector) {
    this.connector = connector;
  }

  @PubSubPacketHandler
  public void onRequestJoin(ServerJoinRequest request) {
    var playerName = this.connector.uuidCache().getUsernameFromUUID(request.targetPlayer()).orElse("<unknown_player>");
    var event = new RequestJoinEvent(request.targetPlayer(), playerName);

    if (!connector.currentServer().currentStatus().acceptingPlayers()) {
      event.setCancelled(true);
      event.cancelMessage(Component.text("Ce serveur n'est pas encore prêt.", NamedTextColor.RED));
    }

    Bukkit.getPluginManager().callEvent(event);

    if (!event.isCancelled()) {
      BukkitBridge.logger().info("[JoinHandler] Player " + playerName + "/" + request.targetPlayer() + " join request accepted, moving the player.");
      connector.utils().movePlayerToServer(request.targetPlayer(), null);
    } else {
      BukkitBridge.logger().info("[JoinHandler] Player " + playerName + "/" + request.targetPlayer() + " join request was rejected.");
      var cancelMessage = event.cancelMessage();

      if (cancelMessage == null) {
        cancelMessage = Component.text("Le serveur n'a pas accepté votre demande de connexion, merci de réessayer dans quelques instants.",
            NamedTextColor.RED);
      }

      connector.utils().sendMessageToPlayer(request.targetPlayer(), cancelMessage);
    }
  }
}
