package net.zyuiop.bukkitbridge;

import java.time.Duration;
import net.zyuiop.bridgeconnector.api.ScheduledTask;
import net.zyuiop.bridgeconnector.api.TaskID;
import net.zyuiop.bridgeconnector.api.TaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class SchedulerWrapper implements TaskScheduler {
  private final BukkitBridge plugin;

  public SchedulerWrapper(BukkitBridge plugin) {
    this.plugin = plugin;
  }

  @Override
  public void runAsyncNow(Runnable runnable) {
    Bukkit.getScheduler().runTaskAsynchronously(this.plugin, runnable);
  }

  @Override
  public void runAsyncIn(Runnable runnable, Duration duration) {
    Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, runnable, asTicks(duration));
  }

  private static long asTicks(Duration duration) {
    return duration.toMillis() / 50;
  }

  @Override
  public TaskID schedule(ScheduledTask scheduledTask) {
    var task =
        scheduledTask.isAsync() ?
            Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, scheduledTask::run, asTicks(scheduledTask.initialDelay()), asTicks(scheduledTask.repeatingDelay()))
            : Bukkit.getScheduler().runTaskTimer(this.plugin, scheduledTask::run, asTicks(scheduledTask.initialDelay()), asTicks(scheduledTask.repeatingDelay()));

    return new BukkitTaskWrapper(task);
  }

  @Override
  public void cancel(TaskID taskID) {
    if (taskID instanceof BukkitTaskWrapper i) {
      i.wrapped.cancel();
    }
  }

  static class BukkitTaskWrapper implements TaskID {
    final BukkitTask wrapped;

    BukkitTaskWrapper(BukkitTask wrapped) {
      this.wrapped = wrapped;
    }
  }
}
