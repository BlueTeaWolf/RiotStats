package de.blueteawolf.riotstats.controller;

import de.blueteawolf.riotstats.api.Match;
import de.blueteawolf.riotstats.api.MatchAnalyzer;
import de.blueteawolf.riotstats.api.Region;
import de.blueteawolf.riotstats.api.Summoner;
import de.blueteawolf.riotstats.Repos.MatchAnalyzeRepository;
import org.json.JSONException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;

/**
 * @author BlueTeaWolf (Ole)
 */
@Controller
@Component
public class MatchAnalyzeController {

    private final MatchAnalyzeRepository matchAnalyzeRepository;

    private MatchAnalyzer matchAnalyzer;
    private Match match;
    private Summoner summoner;

    public MatchAnalyzeController(MatchAnalyzeRepository matchAnalyzeRepository) {
        this.matchAnalyzeRepository = matchAnalyzeRepository;
    }

    public void analyzeMatches(Summoner summoner) {
        this.summoner = summoner;
        try {
            match = new Match(Region.EUROPE, summoner.getPuuID());
            int matchCount = 10;
            match.retrieveMatches(matchCount);
            System.out.println("RETRIEVING MATCHES");
            for (int i = 0; i < matchCount; i++) {

                try {
                    if (matchAnalyzeRepository.findByKey(match.getMatchAt(i) + "_" + summoner.getPuuID()).isEmpty()) {
                        System.out.println("Match not found in database");
                        matchAnalyzer = new MatchAnalyzer();
                        matchAnalyzer.analyzeMatch(match.getMatchDetails(i), summoner.getPuuID());
                        matchAnalyzeRepository.save(matchAnalyzer);

//                    calculateForEverySummoner(i);
                        calculateForThisSummoner(i, summoner.getPuuID());
                }
                } catch (Exception e) {
                    System.err.println("Problem with analyzing the Match " + e);
                }
            }
        } catch (JSONException | IOException jsonException) {
            System.err.println(jsonException + " in" + this);
        }
    }

    private void calculateForEverySummoner(int i) throws IOException {
        for (String puuID : matchAnalyzer.getPlayersByPuuIDHashMap().keySet()) {
            if (!(puuID.equals(summoner.getPuuID()))) {
                MatchAnalyzer matchAnalysis = new MatchAnalyzer();
                matchAnalysis.analyzeMatch(match.getMatchDetails(i), puuID);
                matchAnalyzeRepository.save(matchAnalysis);
            }
        }
    }

    private void calculateForThisSummoner(int i, String puuID) throws IOException {
        MatchAnalyzer matchAnalysis = new MatchAnalyzer();
        matchAnalysis.analyzeMatch(match.getMatchDetails(i), puuID);
        matchAnalyzeRepository.save(matchAnalysis);
    }

}
