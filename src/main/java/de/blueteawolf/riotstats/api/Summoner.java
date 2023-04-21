package de.blueteawolf.riotstats.api;

import de.blueteawolf.riotstats.ApiKey;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author BlueTeaWolf
 */
@Entity
@Table
@Getter
//@Builder
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
        } catch (java.net.SocketTimeoutException e) {
            System.err.println("Riot-API down");
        } catch (Exception e) {
            System.err.println(e + " no summoner with the input name found or the API-Key expired");
        }
    }
}
