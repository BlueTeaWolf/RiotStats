package de.blueteawolf.riotstats;

import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author BlueTeaWolf
 */
public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        Summoner summoner = new Summoner("butterfling", Region.EUW1);
        summoner.getProfile();

        System.out.println(summoner.getSummonerLevel());
        System.out.println(summoner.getSummonerName());
        System.out.println(summoner.getPuuID());
        System.out.println(summoner.getServer());

        Match matches = new Match(Region.EUROPE,summoner.getPuuID());
        matches.getMatches(10);
        System.out.println(matches.getMatchDetails(1));

        MatchAnalyzer matchAnalyzer = new MatchAnalyzer();
        matchAnalyzer.analyzeMatch(matches.getMatchDetails(1));
////        matches.getDetailedMatchInformation();
//
//        TestWithGSON testWithGSON = new TestWithGSON();
//        URL url = new URL("https://europe.api.riotgames.com/lol/match/v5/matches/EUW1_5905796867?api_key=RGAPI-fac0e5a3-6288-4cda-9294-6685f41b4396");
//        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
//        JSONObject jsonObject = new JSONObject(in.readLine());
//        testWithGSON.setTheJSONObject(jsonObject);
//        testWithGSON.Test();


    }


}
