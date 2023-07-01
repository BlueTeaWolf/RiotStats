package de.blueteawolf.riotstats.api.builds;

import lombok.Getter;

/**
 * @author BlueTeaWolf (Ole)
 */
@Getter
public class Build {

    private final String championName;
    private final int[] build;
    private int losses = 0;
    private int wins = 0;

    public Build(String championName, int[] build) {
        this.championName = championName;
        this.build = build;
    }

    public void addLosses(int losses) {
        this.losses += losses;
    }

    public void addWins(int wins) {
        this.wins += wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getWins() {
        return wins;
    }
}
