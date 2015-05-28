package net.bridgesapi.utils.scoreboard;

import org.bukkit.scoreboard.Team;

import java.util.ArrayList;

/**
 * @autour Created by Mac' on 28/05/2015.
 */
public class VirtualTeam {

    private String name;
    private Team team;
    private ArrayList<String> playersName;
    private String prefix;
    private String suffix;

    /**
     * Create a virtual team
     * @param teamName team name
     * @param prefix team prefix
     * @param suffix team suffix
     * @param players team players
     */
    public VirtualTeam(String teamName, String prefix, String suffix, String ... players) {
        this.name = teamName;
        this.prefix = prefix;
        this.suffix = suffix;
    }


    public VirtualTeam(String teamName, String prefix) {

    }

    /**
     * Add players for this
     * @param players
     */
    public void addPlayers(String ... players) {
        for(String player : players) {
            if(!playersName.contains(player)) {
                playersName.add(player);
            }
        }
    }
}
