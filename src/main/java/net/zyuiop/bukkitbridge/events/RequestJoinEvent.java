package net.zyuiop.bukkitbridge.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RequestJoinEvent extends Event implements Cancellable {
  private static final HandlerList handlers = new HandlerList();

  @Override
  public boolean isCancelled() {
    return false;
  }

  @Override
  public void setCancelled(boolean cancel) {

  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
