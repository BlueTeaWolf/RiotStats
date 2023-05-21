package de.blueteawolf.riotstats.api;

import de.blueteawolf.riotstats.ApiKey;
import jakarta.persistence.Transient;
import lombok.Getter;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

/**
 * @author BlueTeaWolf
 */
@Getter
public class Match {

    @Transient
    private HashMap<Integer, String> matches = new HashMap<>();
    @Transient
    private HashMap<Integer, String> matchDetails = new HashMap<>();
    @Transient
    private Region region;
    @Transient
    private String puuID;

    public Match(Region region, String puuID) {
        this.region = region;
        if (region == null) {
            this.region = Region.EUROPE;
        }
        this.puuID = puuID;
    }

    public Match() {

    }

    public void retrieveMatches(int matchCount) throws IOException {
        if (matchCount > 100 || matchCount < 1) {
            return;
        }
        URL matchSite = new URL("https://"
                + region
                + ".api.riotgames.com/lol/match/v5/matches/by-puuid/"
                + puuID
                + "/ids?start=0&count=" + matchCount + new ApiKey().getMATCH_API_KEY());
        BufferedReader in = new BufferedReader(new InputStreamReader(matchSite.openStream()));
        JSONArray matches = new JSONArray(in.readLine());
        System.out.println(matchSite);

        for (int i = 0; i < matchCount; i++) {
            this.matches.put(i,matches.getString(i));
        }

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
        StringBuilder detailedMatchInformation = new StringBuilder(in.readLine());
        in.close();

        matchDetails.put(matchNumber, String.valueOf(detailedMatchInformation));
        return detailedMatchInformation;
    }

    public String getMatchAt(int position) {
        return matches.get(position);
    }

}
