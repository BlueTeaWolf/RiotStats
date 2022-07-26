package de.blueteawolf.riotstats;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author BlueTeaWolf
 */
public class Summoner {
    private final String summonerName;
    private final Region server;
    private int summonerLevel;
    private int profileIconID;
    private String puuID;
    private String accountID;
    private String id;


    public Summoner(String summonerName, Region server){
        this.summonerName = summonerName;
        this.server = server;
    }

    public void getProfile() throws IOException {
        URL summonerProfile = new URL("https://" + server + ".api.riotgames.com/lol/summoner/v4/summoners/by-name/" + summonerName + new ApiKey().getAPI_KEY());
        BufferedReader in = new BufferedReader(new InputStreamReader(summonerProfile.openStream()));
        String lolAPIJson = in.readLine();
        in.close();
        System.out.println(lolAPIJson);

        JSONObject lolAPI = new JSONObject(lolAPIJson);

        summonerLevel = lolAPI.getInt("summonerLevel");
        profileIconID = lolAPI.getInt("profileIconId");
        puuID = lolAPI.getString("puuid");
        accountID = lolAPI.getString("accountId");
        id = lolAPI.getString("id");
    }

    public String getSummonerName() {
        return summonerName;
    }

    public Region getServer() {
        return server;
    }

    public int getSummonerLevel() {
        return summonerLevel;
    }

    public int getProfileIconID() {
        return profileIconID;
    }

    public String getPuuID() {
        return puuID;
    }

    public String getAccountID() {
        return accountID;
    }

    public String getId() {
        return id;
    }
}
