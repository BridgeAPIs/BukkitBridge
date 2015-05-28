package net.bridgesapi.utils;

import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R2.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;


/**
 * @author created by ThisIsMac on 26/05/2015.
 */
public class TitlesUtils {


    private static int DEFAULT_FADE_OUT = 20;
    private static int DEFAULT_STAY = 20;
    private static int DEFAULT_FADE_IN = 20;


    /**
     * Render header and footer on all player's screen
     * @param header header that will be render
     * @param footer footer that willl be render
     */
    public static void sendHeaderAndFooterToAllPlayer(String header, String footer){
        for(Player p : Bukkit.getOnlinePlayers()){
            sendTabHeaderAndFooterToPlayer(p, header, footer);
        }
    }


    /**
     * Render title on all player screen with default fadeout, stay and fadein
     * @param title title that will be render on player's screen
     * @param subtitle sub-tittle that will be render on player's screen
     */
    public static void sendTitleToAllPlayer(String title, String subtitle) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            sendTitleToPlayer(p, title, subtitle);
        }
    }


    /**
     * Render title on player screen with default fadeout, stay and fadein
     * @param player player will receive title
     * @param title title that will be render on player's screen
     * @param subtitle sub-tittle that will be render on player's screen
     */
    public static void sendTitleToPlayer(Player player, String title, String subtitle){
        sendTitleToPlayer(player, DEFAULT_FADE_IN, DEFAULT_STAY, DEFAULT_FADE_OUT, title, subtitle);
    }


    /**
     * Render title on player screen
     * @param player player will receive title
     * @param fadeIn time in tick that make text to appear
     * @param stay time in tick that text stay on player's screen
     * @param fadeOut time in tick that make text to disappear
     * @param title title that will be render on player's screen
     * @param subtitle sub-tittle that will be render on player's screen
     */
    public static void sendTitleToPlayer(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle)
    {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

        PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);
        connection.sendPacket(packetPlayOutTimes);

        if (subtitle != null)
        {
            subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
            IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
            PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);
            connection.sendPacket(packetPlayOutSubTitle);
        }

        if (title != null)
        {
            title = title.replaceAll("%player%", player.getDisplayName());
            title = ChatColor.translateAlternateColorCodes('&', title);
            IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
            PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);
            connection.sendPacket(packetPlayOutTitle);
        }
    }

    /**
     * Render header and footer on the player's tablist
     * @param player player who receive header & footer
     * @param header header that will be render
     * @param footer footer that willl be render
     */
    public static void sendTabHeaderAndFooterToPlayer(Player player, String header, String footer)
    {
        if (header == null) header = "";
        header = ChatColor.translateAlternateColorCodes('&', header);

        if (footer == null) footer = "";
        footer = ChatColor.translateAlternateColorCodes('&', footer);

        header = header.replaceAll("%player%", player.getDisplayName());
        footer = footer.replaceAll("%player%", player.getDisplayName());

        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        IChatBaseComponent tabTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + header + "\"}");
        IChatBaseComponent tabFoot = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");
        PacketPlayOutPlayerListHeaderFooter headerPacket = new PacketPlayOutPlayerListHeaderFooter(tabTitle);

        try
        {
            Field field = headerPacket.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(headerPacket, tabFoot);
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        finally
        {
            connection.sendPacket(headerPacket);
        }
    }
}
