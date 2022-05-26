package de.blueteawolf.riotstats;

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
    private StringBuilder matchLogJson;
    private final Region region;
    private final String puuID;

    public Match(Region region, String puuID) {
        this.region = region;
        this.puuID = puuID;
    }

    public StringBuilder getMatches(int countMatch) throws IOException {
        if (countMatch > 100 || countMatch < 1) {
            return null;
        }
        URL matchSite = new URL("https://" + region + ".api.riotgames.com/lol/match/v5/matches/by-puuid/" + puuID + "/ids?start=0&count=" + countMatch + new ApiKey().getMATCH_API_KEY());
        BufferedReader in = new BufferedReader(new InputStreamReader(matchSite.openStream()));
        matchLogJson = new StringBuilder(in.readLine());

        for (int i = 1; i <= countMatch; i++) {
            int lengthOfCode = matchLogJson.indexOf("E");
            matches.put(i, matchLogJson.substring(lengthOfCode, lengthOfCode + 15));
            matchLogJson.deleteCharAt(lengthOfCode);
        }
        return matchLogJson;
    }
    public StringBuilder getMatchDetails(int matchNumber) throws IOException {
        if (matches.size() < 1) {
            return null;
        }
        URL matchDetails = new URL("https://" + region + ".api.riotgames.com/lol/match/v5/matches/" + matches.get(matchNumber) + new ApiKey().getAPI_KEY());
        BufferedReader in = new BufferedReader(new InputStreamReader(matchDetails.openStream()));
        StringBuilder detailedMatchInformation = new StringBuilder(in.readLine());
        return detailedMatchInformation;
    }



}
