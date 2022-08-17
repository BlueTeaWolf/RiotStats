package de.blueteawolf.riotstats;

import de.blueteawolf.riotstats.api.Region;
import de.blueteawolf.riotstats.api.Summoner;
import de.blueteawolf.riotstats.summoner.SummonerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * @author BlueTeaWolf
 */

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws IOException {

        SpringApplication.run(Main.class, args);
        System.out.println("Hello, executed main application with Springboot");



//        Summoner summoner = new Summoner("butterfling", Region.EUW1);
//        summoner.getProfile();
//
//        System.out.println(summoner.getSummonerLevel());
//        System.out.println(summoner.getSummonerName());
//        System.out.println(summoner.getPuuID());
//        System.out.println(summoner.getServer());
//        System.out.println(summoner.getId() + " - ID");
//
//        Match matches = new Match(Region.EUROPE,summoner.getPuuID());
//        matches.getMatches(10);
//        System.out.println(matches.getMatchDetails(1));
//
//        MatchAnalyzer matchAnalyzer = new MatchAnalyzer();
//        matchAnalyzer.analyzeMatch(matches.getMatchDetails(1));
    }


}
