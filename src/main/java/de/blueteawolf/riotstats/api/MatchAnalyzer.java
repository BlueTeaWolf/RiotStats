package de.blueteawolf.riotstats.api;

import jakarta.persistence.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author BlueTeaWolf
 */
@Entity
@Table
public class MatchAnalyzer {

    @Transient
    private String matchAnalyzeJson;
    @Id
    private String matchID;
    private Role role;

    private String gameMode;
    private String championName;

    private int[] items = new int[6];

//    @ElementCollection
//    @Size(max = 9)
    @Transient
    private HashMap<String, Integer> playersByPuuID = new HashMap<>();

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

    private int baronKills;
    private int dragonKills;
    private double earliestDragonTakedown;
    private int enemyJungleMonsterKills;

    private int dancedWithRiftHerald;
    private double teamDamagePercentage;
    private int objectivesStolen;
    private boolean firstBloodKill;
    private boolean firstTowerKill;
    private int goldEarned;
    private int totalHealsOnTeammates;
    private int trueDamageTaken;
    private int totalTimeSpentDead;

    public MatchAnalyzer() {

    }

    public void analyzeMatch(StringBuilder detailedMatchInformationJson, String playerPuuiD) {

        matchAnalyzeJson = detailedMatchInformationJson.toString();

        analyzeMatch(playerPuuiD);
    }

    private void analyzeMatch(String playerPuuID) {

        JSONObject matchJson = new JSONObject(matchAnalyzeJson);

        JSONObject metadata = matchJson.getJSONObject("metadata");
        JSONObject info = matchJson.getJSONObject("info");

        JSONArray participants = info.getJSONArray("participants");
        JSONArray allSummoners = metadata.getJSONArray("participants");

        matchID = metadata.getString("matchId"); //gameId is the matchID without the 'EUW1_' for example -> Maybe important later on


        for (int i = 0; i < allSummoners.length(); i++) {
            playersByPuuID.put(allSummoners.getString(i), i);
            System.out.println(allSummoners.getString(i));
        }
        System.out.println("--------------");

        JSONObject participant = participants.getJSONObject(playersByPuuID.get(playerPuuID));

        JSONObject challenges = participant.getJSONObject("challenges");

        gameMode = info.getString("gameMode");

        championName = participant.getString("championName");
        role = Role.valueOf(participant.getString("individualPosition"));
        quadraKills = participant.getInt("quadraKills");
        tripleKills = participant.getInt("tripleKills");
        assists = participant.getInt("assists");
        killingSprees = participant.getInt("killingSprees");
        acesBefore15Minutes = challenges.getInt("acesBefore15Minutes");
        timeCCingOthers = participant.getDouble("timeCCingOthers");
        totalDamageTaken = participant.getInt("totalDamageTaken");
        totalDamageDealt = participant.getInt("totalDamageDealt");
        totalHeal = participant.getInt("totalHeal");
        visionScore = participant.getInt("visionScore");
        wardsKilled = participant.getInt("wardsKilled");
        wardsPlaced = participant.getInt("wardsPlaced");
        turretsLost = participant.getInt("turretsLost");
        turretKills = participant.getInt("turretKills");
        baronKills = participant.getInt("baronKills");
        dragonKills = participant.getInt("dragonKills");
        objectivesStolen = participant.getInt("objectivesStolen");
        firstBloodKill = participant.getBoolean("firstBloodKill");
        firstTowerKill = participant.getBoolean("firstTowerKill");
        goldEarned = participant.getInt("goldEarned");
        totalHealsOnTeammates = participant.getInt("totalHealsOnTeammates");
        trueDamageTaken = participant.getInt("trueDamageTaken");
        totalTimeSpentDead = participant.getInt("totalTimeSpentDead");

        win = participant.getBoolean("win");
        teamEarlySurrendered = participant.getBoolean("teamEarlySurrendered");

        for (int i = 0; i < 6; i++) {
            items[i] = participant.getInt("item" + i);
        }

        kda = challenges.getDouble("kda");
        killParticipation = challenges.getDouble("killParticipation");
        visionScorePerMinute = challenges.getDouble("visionScorePerMinute");
        controlWardsPlaced = challenges.getInt("controlWardsPlaced");
        earliestDragonTakedown = challenges.getDouble("earliestDragonTakedown");
        enemyJungleMonsterKills = challenges.getInt("enemyJungleMonsterKills");
        dancedWithRiftHerald = challenges.getInt("dancedWithRiftHerald");

//        System.out.println(this);

    }

    @Override
    public String toString() {
        return "MatchAnalyzer{" +
                "matchAnalyzeJson='" + matchAnalyzeJson + '\'' +
                ", matchID='" + matchID + '\'' +
                ", role=" + role +
                ", gameMode='" + gameMode + '\'' +
                ", championName='" + championName + '\'' +
                ", items=" + Arrays.toString(items) +
                ", playersByPuuID=" + playersByPuuID +
                ", quadraKills=" + quadraKills +
                ", tripleKills=" + tripleKills +
                ", assists=" + assists +
                ", kda=" + kda +
                ", killParticipation=" + killParticipation +
                ", killingSprees=" + killingSprees +
                ", acesBefore15Minutes=" + acesBefore15Minutes +
                ", timeCCingOthers=" + timeCCingOthers +
                ", totalDamageTaken=" + totalDamageTaken +
                ", totalDamageDealt=" + totalDamageDealt +
                ", totalHeal=" + totalHeal +
                ", visionScore=" + visionScore +
                ", visionScorePerMinute=" + visionScorePerMinute +
                ", wardsKilled=" + wardsKilled +
                ", wardsPlaced=" + wardsPlaced +
                ", controlWardsPlaced=" + controlWardsPlaced +
                ", win=" + win +
                ", teamEarlySurrendered=" + teamEarlySurrendered +
                ", turretsLost=" + turretsLost +
                ", turretKills=" + turretKills +
                ", baronKills=" + baronKills +
                ", dragonKills=" + dragonKills +
                ", earliestDragonTakedown=" + earliestDragonTakedown +
                ", enemyJungleMonsterKills=" + enemyJungleMonsterKills +
                ", dancedWithRiftHerald=" + dancedWithRiftHerald +
                ", teamDamagePercentage=" + teamDamagePercentage +
                ", objectivesStolen=" + objectivesStolen +
                ", firstBloodKill=" + firstBloodKill +
                ", firstTowerKill=" + firstTowerKill +
                ", goldEarned=" + goldEarned +
                ", totalHealsOnTeammates=" + totalHealsOnTeammates +
                ", trueDamageTaken=" + trueDamageTaken +
                ", totalTimeSpentDead=" + totalTimeSpentDead +
                '}';
    }
}
