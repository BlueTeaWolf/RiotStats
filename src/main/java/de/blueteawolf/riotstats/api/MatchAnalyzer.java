package de.blueteawolf.riotstats.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author BlueTeaWolf
 */
@Entity
@Table
@Getter
public class MatchAnalyzer {

    @Transient
    @JsonIgnore
    private String matchAnalyzeJson;
    @Id
    private String key;
    private String matchID;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String gameMode;
    private String championName;
    private String summonerName;
    private int[] items = new int[6];
    @Transient
    @JsonIgnore
    private HashMap<String, Integer> playersByPuuID = new HashMap();
    private String puuID;
    @ElementCollection
    List<String> players = new ArrayList<>();
    private int quadraKills;
    private int tripleKills;
    private int assists;
    private double kda;
//    private double killParticipation;
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
//    private double earliestDragonTakedown;
    private int enemyJungleMonsterKills;
    private int dancedWithRiftHerald;
    private double teamDamagePercentage;
    private int skillShotsDodged;
    private int objectivesStolen;
    private boolean firstBloodKill;
    private boolean firstTowerKill;
    private int goldEarned;
    private int totalHealsOnTeammates;
    private int trueDamageTaken;
    private int totalTimeSpentDead;
    private long gameDuration;
    private long gameCreation;

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
        gameCreation = info.getLong("gameCreation");
        gameDuration = info.getLong("gameDuration");

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
//        killParticipation = challenges.getDouble("killParticipation");
        visionScorePerMinute = challenges.getDouble("visionScorePerMinute");
        controlWardsPlaced = challenges.getInt("controlWardsPlaced");
//        earliestDragonTakedown = challenges.getDouble("earliestDragonTakedown");
        enemyJungleMonsterKills = challenges.getInt("enemyJungleMonsterKills");
        dancedWithRiftHerald = challenges.getInt("dancedWithRiftHerald");

        this.players = this.playersByPuuID.keySet().stream().toList();
        this.puuID = playerPuuID;
        this.key = this.matchID + "_" + playerPuuID;
        System.out.println("Made it till the end");

    }

    @JsonIgnore
    public HashMap<String, Integer> getPlayersByPuuIDHashMap() {
        return this.playersByPuuID;
    }

    @Override
    public String toString() {
        return "MatchAnalyzer{" +
                "matchAnalyzeJson='" + matchAnalyzeJson + '\'' +
                "\n matchID='" + matchID + '\'' +
                "\n role=" + role +
                "\n gameMode='" + gameMode + '\'' +
                "\n championName='" + championName + '\'' +
                "\n items=" + Arrays.toString(items) +
                "\n playersByPuuID=" + playersByPuuID +
                "\n quadraKills=" + quadraKills +
                "\n tripleKills=" + tripleKills +
                "\n assists=" + assists +
                "\n kda=" + kda +
//                "\n killParticipation=" + killParticipation +
                "\n killingSprees=" + killingSprees +
                "\n acesBefore15Minutes=" + acesBefore15Minutes +
                "\n timeCCingOthers=" + timeCCingOthers +
                "\n totalDamageTaken=" + totalDamageTaken +
                "\n totalDamageDealt=" + totalDamageDealt +
                "\n totalHeal=" + totalHeal +
                "\n visionScore=" + visionScore +
                "\n visionScorePerMinute=" + visionScorePerMinute +
                "\n wardsKilled=" + wardsKilled +
                "\n wardsPlaced=" + wardsPlaced +
                "\n controlWardsPlaced=" + controlWardsPlaced +
                "\n win=" + win +
                "\n teamEarlySurrendered=" + teamEarlySurrendered +
                "\n turretsLost=" + turretsLost +
                "\n turretKills=" + turretKills +
                "\n baronKills=" + baronKills +
                "\n dragonKills=" + dragonKills +
//                "\n earliestDragonTakedown=" + earliestDragonTakedown +
                "\n enemyJungleMonsterKills=" + enemyJungleMonsterKills +
                "\n dancedWithRiftHerald=" + dancedWithRiftHerald +
                "\n teamDamagePercentage=" + teamDamagePercentage +
                "\n objectivesStolen=" + objectivesStolen +
                "\n firstBloodKill=" + firstBloodKill +
                "\n firstTowerKill=" + firstTowerKill +
                "\n goldEarned=" + goldEarned +
                "\n totalHealsOnTeammates=" + totalHealsOnTeammates +
                "\n trueDamageTaken=" + trueDamageTaken +
                "\n totalTimeSpentDead=" + totalTimeSpentDead +
                "\n gameDuration=" + gameDuration +
                "\n gameCreation=" + gameCreation +
                '}';
    }
}
