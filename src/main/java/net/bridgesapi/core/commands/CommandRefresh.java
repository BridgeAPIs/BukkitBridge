package net.bridgesapi.core.commands;

import net.bridgesapi.core.APIPlugin;
import net.bridgesapi.core.i18n.I18n;
import net.zyuiop.crosspermissions.api.PermissionsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by zyuiop on 27/08/14.
 */
public class CommandRefresh implements CommandExecutor {

    protected PermissionsAPI api;

    public CommandRefresh(PermissionsAPI api) {
        this.api = api;
    }

    @Override
    public boolean onCommand(final CommandSender commandSender, Command command, String s, String[] strings) {
        Bukkit.getScheduler().runTaskAsynchronously(APIPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                api.getManager().refresh();
                commandSender.sendMessage(I18n.getCommandMessage("refresh", "cache_refreshed"));
            }
        });
        return true;
    }
}
