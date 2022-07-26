package de.blueteawolf.riotstats;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

/**
 * @author BlueTeaWolf
 */
public class MatchAnalyzer {

    private StringBuilder matchAnalyzeJson;
    private String summonerName;
    private String role;
    private int profileIcon;

    private String gameMode;
    private String championName;

    private int[] items = new int[6];

    private int quadraKills;
    private int tripleKills;
    private int assists;
    private double kda;
    private double killParticipation;
    private int killingSprees;
    private int acesBefore15Minutes;

    private double timeCCingOthers;
    private int totalDamageTaken;
    private int totalDamageDealt;
    private int totalHeal;

    private int visionScore;
    private double visionScorePerMinute;
    private int wardsKilled;
    private int wardsPlaced;
    private int controlWardsPlaced;

    private boolean win;
    private boolean teamEarlySurrendered;
    private int turretsLost;
    private int turretKills;
    private int hadAfkTeammate; //boolean or showing how many?

    private int baronKills;
    private int dragonKills;
    private double earliestDragonTakedown;
    private int enemyJungleMonsterKills;

    private int dancedWithRiftHerald;
    private double teamDamagePercentage;
    private int objectivesStolen;

    public void analyzeMatch(StringBuilder detailedMatchInformationJson) throws ParseException {

        matchAnalyzeJson = detailedMatchInformationJson;



//        for (int i = 0; i < 9; i++) {
            analyzeMatch(1);
//        }
    }

    private void analyzeMatch(int participantNumber) throws ParseException {

        JSONObject matchJson = new JSONObject(matchAnalyzeJson.toString());

        JSONObject metadata = matchJson.getJSONObject("metadata");
        JSONObject info = matchJson.getJSONObject("info");

        JSONArray participants = info.getJSONArray("participants");

        JSONObject participant = participants.getJSONObject(participantNumber);

        JSONObject challenges = participant.getJSONObject("challenges");

        role = participant.getString("individualPosition");
        quadraKills = participant.getInt("quadraKills");
        tripleKills = participant.getInt("tripleKills");
        assists = participant.getInt("assists");
        kda = challenges.getDouble("kda");
        killParticipation = challenges.getDouble("killParticipation");
        killingSprees = participant.getInt("killingSprees");
        acesBefore15Minutes = challenges.getInt("acesBefore15Minutes");
        teamDamagePercentage = challenges.getDouble("teamDamagePercentage");

        timeCCingOthers = participant.getDouble("timeCCingOthers");
        totalDamageTaken = participant.getInt("totalDamageTaken");
        totalDamageDealt = participant.getInt("totalDamageDealt");
        totalHeal = participant.getInt("totalHeal");

        visionScore = participant.getInt("visionScore");
        visionScorePerMinute = challenges.getDouble("visionScorePerMinute");
        wardsKilled = participant.getInt("wardsKilled");
        wardsPlaced = participant.getInt("wardsPlaced");
        controlWardsPlaced = challenges.getInt("controlWardsPlaced");

        win = participant.getBoolean("win");
        teamEarlySurrendered = participant.getBoolean("teamEarlySurrendered");
        turretsLost = participant.getInt("turretsLost");
        turretKills = participant.getInt("turretKills");
//        hadAfkTeammate = participant.getInt("hadAfkTeammate");

        baronKills = participant.getInt("baronKills");
        dragonKills = participant.getInt("dragonKills");
        earliestDragonTakedown = challenges.getDouble("earliestDragonTakedown");
        enemyJungleMonsterKills = challenges.getInt("enemyJungleMonsterKills");
        objectivesStolen = participant.getInt("objectivesStolen");

        dancedWithRiftHerald = challenges.getInt("dancedWithRiftHerald");

        for (int i = 0; i < 6; i++) {
            items[i] = participant.getInt("item" + i);
        }

    }

}
