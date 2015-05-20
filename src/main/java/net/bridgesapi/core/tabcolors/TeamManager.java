package net.bridgesapi.core.tabcolors;

import net.bridgesapi.api.BukkitBridge;
import net.bridgesapi.api.permissions.PermissionsManager;
import net.bridgesapi.core.APIPlugin;
import net.zyuiop.crosspermissions.api.permissions.PermissionGroup;
import net.zyuiop.crosspermissions.api.permissions.PermissionUser;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class TeamManager {

    /**
     * The escape sequence for minecraft special chat codes
     */
    public static final char ESCAPE = '\u00A7';
	private final APIPlugin plugin;
    public List<PermissionGroup> groups = new ArrayList<>();
    public TeamHandler teamHandler;
    public final PermissionsManager manager;

    public Executor executor = Executors.newSingleThreadExecutor();

    public TeamManager(APIPlugin pl) {
        plugin = pl;
        manager = BukkitBridge.get().getPermissionsManager();

        teamHandler = new TeamHandler();

        if (!pl.isDatabaseEnabled())
            return;

        groups.addAll(manager.getApi().getManager().getGroupsCache().values().stream().collect(Collectors.toList()));

        for (PermissionGroup pg : groups) {
            String teamName = pg.getProperty("team-name");
            if (teamName == null)
                teamName = pg.getGroupName();

            if (teamHandler.getTeamByName(teamName) != null)
                continue;

            TeamHandler.VTeam vt = teamHandler.createNewTeam(teamName, "");

            if (manager.getDisplay(pg) != null)
                vt.setPrefix(manager.getDisplay(pg));
            if (manager.getDisplay(pg) != null)
                vt.setDisplayName(manager.getDisplay(pg));
            if (manager.getSuffix(pg) != null)
                vt.setSuffix(manager.getSuffix(pg));

            teamHandler.addTeam(vt);
            APIPlugin.log("[TeamRegister] Team " + teamName + " ajoutée  --> " +  vt.getPrefix() + " / " + vt);
        }
    }

    /**
     * Replace all of the color codes (prepended with &) with the corresponding color code.
     * This uses raw char arrays so it can be considered to be extremely fast.
     *
     * @param text
     * @return
     */
    public static String replaceColors(String text) {
        char[] chrarray = text.toCharArray();

        for (int index = 0; index < chrarray.length; index++) {
            char chr = chrarray[index];

            // Ignore anything that we don't want
            if (chr != '&') {
                continue;
            }

            if ((index + 1) == chrarray.length) {
                // we are at the end of the array
                break;
            }

            // get the forward char
            char forward = chrarray[index + 1];

            // is it in range?
            if ((forward >= '0' && forward <= '9') || (forward >= 'a' && forward <= 'f') || (forward >= 'k' && forward <= 'r')) {
                // It is! Replace the char we are at now with the escape sequence
                chrarray[index] = ESCAPE;
            }
        }

        // Rebuild the string and return it
        return new String(chrarray);
    }

    public static List<Player> getOnline() {
        List<Player> list = new ArrayList<>();

        for (World world : Bukkit.getWorlds()) {
            list.addAll(world.getPlayers());
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * Takes a string and replaces &# color codes with ChatColors
     *
     *
     * @return
     */

    public void playerLeave(final Player p) {
        executor.execute(() -> teamHandler.removeReceiver(p));

    }

    public void playerJoin(final Player p) {
        executor.execute(() -> {
            teamHandler.addReceiver(p);

            final PermissionUser user = manager.getApi().getUser(p.getUniqueId());
            final String prefix = user.getProperty("team-name");

            TeamHandler.VTeam vtt = teamHandler.getTeamByName(prefix);
            if (vtt == null) {
                vtt = teamHandler.getTeamByName("zzjoueur");
            }

            teamHandler.addPlayerToTeam(p, vtt);
        });
    }

}
