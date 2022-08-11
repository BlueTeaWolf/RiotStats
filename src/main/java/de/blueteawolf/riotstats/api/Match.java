package de.blueteawolf.riotstats.api;

import de.blueteawolf.riotstats.ApiKey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

/**
 * @author BlueTeaWolf
 */
public class Match {
    private HashMap<Integer, String> matches = new HashMap<>();
    private HashMap<Integer, String> matcheDetails = new HashMap<>();
    private StringBuilder matchLogJson;
    private StringBuilder detailedMatchInformation;
    private final Region region;
    private final String puuID;

    public Match(Region region, String puuID) {
        this.region = region;
        this.puuID = puuID;
    }

    public void getMatches(int countMatch) throws IOException {
        if (countMatch > 100 || countMatch < 1) {
            return;
        }
        URL matchSite = new URL("https://" + region + ".api.riotgames.com/lol/match/v5/matches/by-puuid/" + puuID + "/ids?start=0&count=" + countMatch + new ApiKey().getMATCH_API_KEY());
        BufferedReader in = new BufferedReader(new InputStreamReader(matchSite.openStream()));
        matchLogJson = new StringBuilder(in.readLine());
        in.close();

        for (int i = 1; i <= countMatch; i++) {
            int lengthOfCode = matchLogJson.indexOf("E");
            matches.put(i, matchLogJson.substring(lengthOfCode, lengthOfCode + 15));
            matchLogJson.deleteCharAt(lengthOfCode);
        }
    }

    public StringBuilder getMatchDetails(int matchNumber) throws IOException {
        if (matches.size() < 1) {
            return null;
        }
        URL matchDetailsUrl = new URL("https://" + region + ".api.riotgames.com/lol/match/v5/matches/" + matches.get(matchNumber) + new ApiKey().getAPI_KEY());
        BufferedReader in = new BufferedReader(new InputStreamReader(matchDetailsUrl.openStream()));
        detailedMatchInformation = new StringBuilder(in.readLine());
        in.close();
        matcheDetails.put(matchNumber, String.valueOf(detailedMatchInformation));
        return detailedMatchInformation;
    }

    public void getDetailedMatchInformation() {
        MatchAnalyzer matchAnalyzer = new MatchAnalyzer();
        matchAnalyzer.analyzeMatch(detailedMatchInformation);
    }
}
