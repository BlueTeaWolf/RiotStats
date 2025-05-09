package de.blueteawolf.riotstats.controller;

import de.blueteawolf.riotstats.Repos.MatchAnalyzeRepository;
import de.blueteawolf.riotstats.api.MatchAnalyzer;
import de.blueteawolf.riotstats.api.Role;
import de.blueteawolf.riotstats.api.builds.Build;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author BlueTeaWolf (Ole)
 */
@RestController
public class MatchAPIController {

    private final MatchAnalyzeRepository matchAnalyzeRepository;

    public MatchAPIController(MatchAnalyzeRepository matchAnalyzeRepository) {
        this.matchAnalyzeRepository = matchAnalyzeRepository;
    }

    @GetMapping("/api/matchAnalyzer")
    @ResponseBody
    public MatchAnalyzer getMatchAnalyzer(@RequestParam String matchIDPuuID) {
        Optional<MatchAnalyzer> matchAnalyzer = matchAnalyzeRepository.findByKey(matchIDPuuID);
        return matchAnalyzer.orElse(null);
    }

    @GetMapping("/api/matchAnalyze/wins")
    @ResponseBody
    public List<Boolean> getWinsAndLosses(@RequestParam String summonerPuuID) {
        return matchAnalyzeRepository.findByPuuID(summonerPuuID)
                .stream()
                .map(MatchAnalyzer::isWin)
                .collect(Collectors.toList());
    }

//    @GetMapping("/api/getChampionPool")
//    @ResponseBody
//    public List<String> getChampionPool(@RequestParam String summonerPuuID) {
//        return matchAnalyzeRepository.findByPuuID(summonerPuuID)
//                .stream()
//                .map(MatchAnalyzer::getChampionName)
//                .sorted(Comparator.comparing(String::toLowerCase))
//                .toList();
//    }

    @GetMapping("/api/getChampionPool")
    @ResponseBody
    public String getChampionPool(@RequestParam String summonerPuuID) {
        List<MatchAnalyzer> matches = matchAnalyzeRepository.findByPuuID(summonerPuuID);
        Map<String, Integer> championWins = new HashMap<>();
        Map<String, Integer> championPlays = new HashMap<>();
        Map<String, Double> championKDAs = new HashMap<>();

        for (MatchAnalyzer matchAnalyzer : matches) {
            String championName = matchAnalyzer.getChampionName();
            boolean isWin = matchAnalyzer.isWin();
            double kda = matchAnalyzer.getKda();

            championWins.put(championName, championWins.getOrDefault(championName, 0) + (isWin ? 1 : 0));
            championPlays.put(championName, championPlays.getOrDefault(championName, 0) + 1);
            championKDAs.put(championName, championKDAs.getOrDefault(championName, 0.0) + kda);
        }

        JSONObject championData = new JSONObject();

        for (String championName : championWins.keySet()) {
            int wins = championWins.get(championName);
            int plays = championPlays.get(championName);
            double kdaTotal = championKDAs.get(championName);
            double averageKDA = kdaTotal / plays;

            JSONObject championInfo = new JSONObject();
            championInfo.put("wins", wins);
            championInfo.put("played", plays);
            championInfo.put("averageKDA", averageKDA);

            championData.put(championName, championInfo);
        }

        return championData.toString();
    }

    @GetMapping("/api/get")
    @ResponseBody
    public Map<String, Double> getThings(@RequestParam String summonerPuuID) {

        List<MatchAnalyzer> matches = matchAnalyzeRepository.findByPuuID(summonerPuuID);
        Map<String, Double> averages = new HashMap<>();

        int count = matches.size();

        if (count > 0) {

            double kda = matches.stream().mapToDouble(MatchAnalyzer::getKda).sum() / count;
            double wardsPlaced = matches.stream().mapToDouble(MatchAnalyzer::getWardsPlaced).sum() / count;
            double wardsKilled = matches.stream().mapToDouble(MatchAnalyzer::getWardsKilled).sum() / count;
            double visionScore = matches.stream().mapToDouble(MatchAnalyzer::getVisionScore).sum() / count;
            double visionScorePerMinute = matches.stream().mapToDouble(MatchAnalyzer::getVisionScorePerMinute).sum() / count;
            double timeSpentDead = matches.stream().mapToDouble(MatchAnalyzer::getTotalTimeSpentDead).sum() / count;
            double totalDamageTaken = matches.stream().mapToDouble(MatchAnalyzer::getTotalDamageTaken).sum() / count;
            double totalDamageDealt = matches.stream().mapToDouble(MatchAnalyzer::getTotalDamageDealt).sum() / count;
            double totalHealsOnChampions = matches.stream().mapToDouble(MatchAnalyzer::getTotalHealsOnTeammates).sum() / count;
            double turretKills = matches.stream().mapToDouble(MatchAnalyzer::getTurretKills).sum() / count;

            averages.put("kda", kda);
            averages.put("wardsPlaced", wardsPlaced);
            averages.put("wardsKilled", wardsKilled);
            averages.put("visionScore", visionScore);
            averages.put("visionScorePerMinute", visionScorePerMinute);
            averages.put("timeSpentDead", timeSpentDead);
            averages.put("totalDamageTaken", totalDamageTaken);
            averages.put("totalDamageDealt", totalDamageDealt);
            averages.put("totalHealsOnChampions", totalHealsOnChampions);
            averages.put("turretKills", turretKills);
        }
        return averages;
    }

    @GetMapping("/api/roles")
    @ResponseBody
    public List<Role> getPlayedRoles(@RequestParam String summonerPuuID) {
        return matchAnalyzeRepository.findByPuuID(summonerPuuID)
                .stream()
                .map(MatchAnalyzer::getRole)
                .toList();
    }

    @GetMapping("/api/builds")
    @ResponseBody
    public String getBuilds(@RequestParam String summonerPuuID) {
        List<MatchAnalyzer> matchesList = matchAnalyzeRepository.findByPuuID(summonerPuuID);
        Map<String, List<Build>> championBuilds = new HashMap<>();

        for (MatchAnalyzer match : matchesList) {

            if (!championBuilds.containsKey(match.getChampionName())) {
                championBuilds.put(match.getChampionName(), new ArrayList<>());
            }

            Build build = new Build(match.getChampionName(), match.getItems(), match.getKda());
            championBuilds.get(match.getChampionName()).add(build);

            if (match.isWin()) {
                build.addWins(1);
            } else {
                build.addLosses(1);
            }

        }

        for (String champion : championBuilds.keySet()) {
            List<Build> builds = championBuilds.get(champion);
            int buildsSize = builds.size();

            for (int i = buildsSize - 1; i >= 0; i--) {
                Build currentBuild = builds.get(i);
                for (int j = buildsSize - 1; j >= 0; j--) {
                    if (i == j) {
                        continue;
                    }

                    int[] items = builds.get(j).getBuild();

                    if (isSubset(currentBuild.getBuild(), items)) {
                        currentBuild.addWins(builds.get(j).getWins());
                        currentBuild.addLosses(builds.get(j).getLosses());
                        currentBuild.addKda(builds.get(j).getKda());
                        builds.remove(j);
                        buildsSize--;
                    }
                }
            }
        }

        JSONObject championBuildData = new JSONObject();

        for (String champion : championBuilds.keySet()) {
            JSONArray buildsArray = new JSONArray();
            for (Build build : championBuilds.get(champion)) {
                JSONObject championBuildInfo = new JSONObject();
                championBuildInfo.put("BUILD", build.getBuild());
                championBuildInfo.put("WINS", build.getWins());
                championBuildInfo.put("LOSSES", build.getLosses());
                championBuildInfo.put("KDA", build.getKda());
                buildsArray.put(championBuildInfo);
            }
            championBuildData.put(champion, buildsArray);

        }
        return championBuildData.toString();
    }

    private boolean isSubset(int[] array1, int[] array2) {
        HashSet<Integer> set = new HashSet<>();

        if (array1.length >= array2.length) {
            for (int num : array1) {
                if(num != 0) {
                    set.add(num);
                }
            }
        } else {
            for (int num : array2) {
                if(num != 0) {
                    set.add(num);
                }
                set.add(num);
            }
        }

        for (int num : array2) {
            if (!set.contains(num) && num != 0) {
                return false;
            }
        }

        return true;
    }

}

