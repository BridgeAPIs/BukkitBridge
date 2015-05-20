package net.bridgesapi.tools;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.bridgesapi.core.APIPlugin;
import org.bukkit.entity.Player;

public class BungeeUtils
{
    public static void sendPlayerToServer(Player player, String server)
    {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(player.getName());
        out.writeUTF(server);

        APIPlugin.getInstance().getServer().sendPluginMessage(APIPlugin.getInstance(), "BungeeCord", out.toByteArray());
    }
}
