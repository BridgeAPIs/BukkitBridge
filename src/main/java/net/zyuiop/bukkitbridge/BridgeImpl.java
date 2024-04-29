package net.zyuiop.bukkitbridge;

import net.zyuiop.bridgeconnector.api.BridgeConnector;
import net.zyuiop.bridgeconnector.api.Context;
import net.zyuiop.bridgeconnector.api.Context.ServerType;
import net.zyuiop.bridgeconnector.api.ServerManager;
import net.zyuiop.bridgeconnector.api.cache.PlayerInfoCache;
import net.zyuiop.bridgeconnector.api.data.ServerKind;
import net.zyuiop.bridgeconnector.api.pubsub.PubSubConnection;
import net.zyuiop.bridgeconnector.impl.GameServerManagerImpl;
import net.zyuiop.bridgeconnector.impl.ServerManagerImpl;
import net.zyuiop.bridgeconnector.impl.cache.jedis.JedisPlayerInfoCache;
import net.zyuiop.bridgeconnector.impl.pubsub.jedis.JedisPubSubConnection;

class BridgeImpl implements BridgeConnector {
  private final Context context;
  private final BukkitBridge plugin;
  private final JedisPlayerInfoCache playerCache;
  private final JedisPubSubConnection pubSub;
  private ServerManager serverManager;

  BridgeImpl(BukkitBridge plugin) {
    var config = ConfigUtils.parseJedisConfig(plugin.getConfig().getConfigurationSection("redis"));

    this.context = new Context(
        ServerType.SERVER,
        plugin.getServer().getName(),
        new SchedulerWrapper(plugin),
        plugin.getSLF4JLogger()
    );
    this.plugin = plugin;
    this.serverManager = new ServerManagerImpl(this, context, plugin.getServer().getIp(), plugin.getServer().getPort(), ServerKind.UNMANAGED);
    this.playerCache = new JedisPlayerInfoCache(context, config);
    this.pubSub = new JedisPubSubConnection(context, config);
  }

  void asGameServer(String game, String variant) {
    this.serverManager = new GameServerManagerImpl(
        this, context, plugin.getServer().getIp(), plugin.getServer().getPort(), game, variant
    );
  }

  @Override
  public PubSubConnection pubSub() {
    return this.pubSub;
  }

  @Override
  public PlayerInfoCache playerCache() {
    return this.playerCache;
  }

  @Override
  public ServerManager currentServer() {
    return this.serverManager;
  }
}
