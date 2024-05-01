package net.zyuiop.bukkitbridge.events;

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
    Bukkit.getPluginManager().callEvent(event);

    if (!event.isCancelled()) {
      BukkitBridge.logger().info("[JoinHandler] Player " + playerName + "/" + request.targetPlayer() + " join request accepted, moving the player.");
      connector.utils().movePlayerToServer(request.targetPlayer(), null);
    } else {
      BukkitBridge.logger().info("[JoinHandler] Player " + playerName + "/" + request.targetPlayer() + " join request was rejected.");
      // TODO: send a message to the player with the rejection reason?
    }
  }
}
