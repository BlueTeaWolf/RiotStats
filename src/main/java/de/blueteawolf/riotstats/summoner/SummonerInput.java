package de.blueteawolf.riotstats.summoner;

import de.blueteawolf.riotstats.api.Region;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author BlueTeaWolf
 */
@Getter
@AllArgsConstructor
public class SummonerInput {

    private String summonerName;
    private Region region;

    @Override
    public String toString() {
        return "SummonerInput{" +
                "summonerName='" + summonerName + '\'' +
                ", region=" + region +
                '}';
    }
}
