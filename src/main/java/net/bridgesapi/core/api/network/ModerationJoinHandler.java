package net.bridgesapi.core.api.network;

import net.bridgesapi.api.BukkitBridge;
import net.bridgesapi.api.channels.PacketsReceiver;
import net.bridgesapi.api.network.JoinHandler;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by vialarl on 09/03/2015.
 */
public class ModerationJoinHandler implements JoinHandler, PacketsReceiver {

    protected HashMap<UUID, UUID> teleportTargets = new HashMap<>();
    protected JoinManagerImplement manager;

    public ModerationJoinHandler(JoinManagerImplement manager) {
        this.manager = manager;
    }

    @Override
    public void onModerationJoin(Player player) {
        player.sendMessage(ChatColor.GOLD + "Vous avez rejoint cette arène en mode modération.");
        if (teleportTargets.containsKey(player.getUniqueId())) {
            UUID target = teleportTargets.get(player.getUniqueId());
            Player tar = Bukkit.getPlayer(target);
            if (tar != null)
                player.teleport(tar);
            teleportTargets.remove(player.getUniqueId());
        }
    }

    @Override
    public void receive(String channel, String packet) {
        String[] args = StringUtils.split(packet, " ");
        String id = args[0];
        UUID uuid = UUID.fromString(id);

        if (BukkitBridge.get().getPermissionsManager().hasPermission(uuid, "games.modjoin"))
            manager.moderatorsExpected.add(uuid);

        if (packet.startsWith("teleport")) {
            try  {
                UUID target = UUID.fromString(args[2]);
                if (BukkitBridge.get().getPermissionsManager().hasPermission(uuid, "games.modjoin")) {
                    teleportTargets.put(uuid, target);
                }
            } catch (Exception ignored) {
            }
        }

        BukkitBridge.get().getProxyDataManager().getProxiedPlayer(uuid).connect(BukkitBridge.get().getServerName());
    }
}
