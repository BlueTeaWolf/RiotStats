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
    private Region region;
    private final String puuID;

    public Match(Region region, String puuID) {
        this.region = region;
        if(region == null) {
            this.region = Region.EUROPE;
        }
        this.puuID = puuID;
    }

    public String getMatches(int matches) throws IOException {
        if (matches > 100 || matches < 1) {
            return null;
        }
        URL matchSite = new URL("https://"
                + region
                + ".api.riotgames.com/lol/match/v5/matches/by-puuid/"
                + puuID
                + "/ids?start=0&count=" + matches + new ApiKey().getMATCH_API_KEY());
        BufferedReader in = new BufferedReader(new InputStreamReader(matchSite.openStream()));
        matchLogJson = new StringBuilder(in.readLine());
        in.close();

        for (int i = 1; i <= matches; i++) {
            int lengthOfCode = matchLogJson.indexOf("E");
            this.matches.put(i, matchLogJson.substring(lengthOfCode, lengthOfCode + 15));
            matchLogJson.deleteCharAt(lengthOfCode);
        }
        return matchLogJson.toString();
    }

    public StringBuilder getMatchDetails(int matchNumber) throws IOException {
        if (matches.size() < 1) {
            return null;
        }
        URL matchDetailsUrl = new URL("https://"
                + region
                + ".api.riotgames.com/lol/match/v5/matches/"
                + matches.get(matchNumber)
                + new ApiKey()
                .getAPI_KEY());
        BufferedReader in = new BufferedReader(new InputStreamReader(matchDetailsUrl.openStream()));
        detailedMatchInformation = new StringBuilder(in.readLine());
        in.close();
        matcheDetails.put(matchNumber, String.valueOf(detailedMatchInformation));
        return detailedMatchInformation;
    }

}
