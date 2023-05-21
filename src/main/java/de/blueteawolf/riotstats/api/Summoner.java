package de.blueteawolf.riotstats.api;

import de.blueteawolf.riotstats.ApiKey;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author BlueTeaWolf
 */
@Entity
@Table
@Getter
public class Summoner {
    @Id
    @jakarta.persistence.Id
    private String summonerName;
    @Enumerated(EnumType.STRING)
    private Region server;
    private int summonerLevel;
    private int profileIconID;
    private String puuID;
    private String accountID;
    private String id;
    private String leagueID;
    @Enumerated(EnumType.STRING)
    private Rank soloRank;
    @Enumerated(EnumType.STRING)
    private Rank flexRank;
    @Enumerated(EnumType.STRING)
    private Division flexDivision;
    @Enumerated(EnumType.STRING)
    private Division soloDivision;
    private int flexWins;
    private int flexLosses;
    private int soloWins;
    private int soloLosses;


    public Summoner(String summonerName, Region server) {
        this.summonerName = summonerName;
        this.server = server;
    }

    public Summoner() {

    }

    public void getProfile() {
        try {
            URL summonerProfile = new URL("https://"
                    + server
                    + ".api.riotgames.com/lol/summoner/v4/summoners/by-name/"
                    + summonerName
                    + new ApiKey().getAPI_KEY());
            HttpURLConnection connection = (HttpURLConnection) summonerProfile.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lolAPIJson = in.readLine();
            in.close();
            System.out.println(lolAPIJson);

            JSONObject lolAPI = new JSONObject(lolAPIJson);

            summonerLevel = lolAPI.getInt("summonerLevel");
            profileIconID = lolAPI.getInt("profileIconId");
            puuID = lolAPI.getString("puuid");
            accountID = lolAPI.getString("accountId");
            id = lolAPI.getString("id");

            JSONArray encryptedSummoner = getEncryptedSummoner();

            System.out.println("Getting encrypted summoner");

            try {
                JSONObject flex = encryptedSummoner.getJSONObject(0);
                flexRank = Rank.valueOf(flex.getString("tier"));
                flexDivision = Division.valueOf(flex.getString("rank"));

                flexWins = flex.getInt("wins");
                flexLosses = flex.getInt("losses");

                leagueID = flex.getString("leagueId");
            } catch (JSONException e) {
                System.out.println("No flex rank");
            }

            try {
                JSONObject solo = encryptedSummoner.getJSONObject(1);
                soloRank = Rank.valueOf(solo.getString("tier"));
                soloDivision = Division.valueOf(solo.getString("rank"));

                soloWins = solo.getInt("wins");
                soloLosses = solo.getInt("losses");
                if (leagueID != null) leagueID = solo.getString("leagueId");
            } catch (JSONException e) {
                System.out.println("No solo rank found");
            }

        } catch (java.net.SocketTimeoutException e) {
            System.err.println("Riot-API down");
        } catch (Exception e) {
            System.err.println(e + " no summoner with the input name found or the API-Key expired");
        }
    }

    private JSONArray getEncryptedSummoner() throws IOException {
        URL encryptedSummoner = new URL("https://"
                + server
                + ".api.riotgames.com/lol/league/v4/entries/by-summoner/"
                + id
                + new ApiKey().getAPI_KEY());
        BufferedReader in = new BufferedReader(new InputStreamReader(encryptedSummoner.openStream()));
        StringBuilder encryptedSummonerBuilder = new StringBuilder(in.readLine());
        in.close();
        System.out.println(encryptedSummonerBuilder + "-----");
        String test = encryptedSummonerBuilder.toString();
        return new JSONArray(test);
    }

}
