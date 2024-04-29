package net.zyuiop.bukkitbridge;

import com.google.common.base.Preconditions;
import net.zyuiop.bridgeconnector.impl.common.JedisConfiguration;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigUtils {
  public static JedisConfiguration parseJedisConfig(ConfigurationSection section) {
    return new JedisConfiguration(
        Preconditions.checkNotNull(section.getString("host")),
        section.getInt("port", 6667),
        section.getString("user"),
        section.getString("password")
    );
  }
}
