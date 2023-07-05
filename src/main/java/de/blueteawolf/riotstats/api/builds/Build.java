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
    private double kda;

    public Build(String championName, int[] build, double kda) {
        this.championName = championName;
        this.build = build;
        this.kda = kda;
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

    public double getKda() {
        return kda;
    }

    public void addKda(double kda) {
        this.kda = (this.kda + kda) / 2;
    }
}
