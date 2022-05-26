package de.blueteawolf.riotstats;

import java.io.IOException;

/**
 * @author BlueTeaWolf
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Summoner summoner = new Summoner("butterfling", Region.EUW1);
        summoner.getProfile();

        System.out.println(summoner.getSummonerLevel());
        System.out.println(summoner.getSummonerName());
        System.out.println(summoner.getPuuID());
        System.out.println(summoner.getServer());

        Match matches = new Match(Region.EUROPE,summoner.getPuuID());
        matches.getMatches(10);
        System.out.println(matches.getMatchDetails(1));

    }


}
