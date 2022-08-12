package de.blueteawolf.riotstats.summoner;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author BlueTeaWolf
 */
@Getter
@AllArgsConstructor
public class SummonerInput {

    private String summonerName;

    @Override
    public String toString() {
        return "SummonerInput{" +
                "summonerName='" + summonerName + '\'' +
                '}';
    }
}
