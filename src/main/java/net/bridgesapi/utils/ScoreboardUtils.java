package net.bridgesapi.utils;


import net.bridgesapi.utils.scoreboard.VirtualScore;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;

/**
 * @author created by ThisIsMac on 26/05/2015.
 */
public class ScoreboardUtils {


    public class UniqueScoreboard {

        private Scoreboard board;
        private ArrayList<VirtualScore> scores;

        /**
         * Create a new scoreboard object
         * @param name displayname
         */
        public UniqueScoreboard(String name) {
            board = Bukkit.getScoreboardManager().getNewScoreboard();
        }

        public void setDisplayName(String newone){

        }

    }

}
