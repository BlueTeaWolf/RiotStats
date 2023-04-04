package de.blueteawolf.riotstats.summoner;

import de.blueteawolf.riotstats.api.MatchAnalyzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author BlueTeaWolf (Ole)
 */
@Repository
@Transactional //(readOnly = true)
public interface MatchRepository extends JpaRepository<MatchAnalyzer, String>{
        Optional<MatchAnalyzer> findByMatchID(String matchID);
}
