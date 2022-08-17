package de.blueteawolf.riotstats.summoner;


import de.blueteawolf.riotstats.api.Summoner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author BlueTeaWolf
 */
@Repository
@Transactional //(readOnly = true)
public interface SummonerRepository extends JpaRepository<Summoner, String> {

    Optional<Summoner> findBySummonerName(String summonerName);

}
