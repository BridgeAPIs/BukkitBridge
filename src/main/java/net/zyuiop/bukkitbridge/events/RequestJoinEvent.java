package net.zyuiop.bukkitbridge.events;

import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RequestJoinEvent extends Event implements Cancellable {
  private static final HandlerList handlers = new HandlerList();
  private final UUID playerJoiningUUID;
  private final String playerJoiningName;
  private boolean canceled = false;
  private Component cancelMessage = null;

  public RequestJoinEvent(UUID playerJoiningUUID, String playerJoiningName) {
    super(true);

    this.playerJoiningUUID = playerJoiningUUID;
    this.playerJoiningName = playerJoiningName;
  }

  public UUID playerJoiningUUID() {
    return playerJoiningUUID;
  }

  public String playerJoiningName() {
    return playerJoiningName;
  }

  public Component cancelMessage() {
    return cancelMessage;
  }

  public void cancelMessage(Component cancelMessage) {
    this.cancelMessage = cancelMessage;
  }

  @Override
  public boolean isCancelled() {
    return canceled;
  }

  @Override
  public void setCancelled(boolean cancel) {
    this.canceled = cancel;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
