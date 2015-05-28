package net.bridgesapi.utils.scoreboard;

/**
 * @author created by ThisIsMac on 28/05/2015.
 */
public class VirtualScore {
    private String player;
    private int score;

    /**
     * Build a new score for given playerName
     * @param playerName
     * @param score
     */
    public VirtualScore(String playerName, int score){
        this.player = playerName;
        this.score = score;
    }

    /**
     * Get score for given VirtualScore
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     * Get player name for given VirtualScore
     * @return name
     */
    public String getPlayerName() {
        return player;
    }

    /**
     * Replace player name of VirtualScore by
     * @param newone
     */
    public void setPlayerName(String newone) {
        this.player = newone;
    }

    /**
     * Add score for given VirtualScore
     * @param playerName name of player
     * @param toadd score to add
     */
    public void addScore(String playerName, int toadd) {
        this.score = score + toadd;
    }

    /**
     * Increment score by one for given VirtualScore
     * @param playerName name
     */
    public void incrementScore(String playerName) {
        this.score = score + 1;
    }

    /**
     * Decrement score by one for given VirtualScore
     * @param playerName name
     */
    public void decrementScore(String playerName) {
        this.score = score - 1;
    }

    /**
     * Remove score for given VirtualScore
     * @param playerName name
     * @param toremove score to remove
     */
    public void delScore(String playerName, int toremove) {
        this.score = score + toremove;
    }

    /**
     * Set score for given VirtualScore
     * @param playerName name
     * @param toset score to set
     */
    public void setScore(String playerName, int toset) {
        this.score = toset;
    }
}