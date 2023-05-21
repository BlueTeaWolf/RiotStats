package de.blueteawolf.riotstats.Repos;

import de.blueteawolf.riotstats.api.MatchAnalyzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author BlueTeaWolf (Ole)
 */
@Repository
@Transactional //(readOnly = true)
public interface MatchAnalyzeRepository extends JpaRepository<MatchAnalyzer, String> {

    Optional<MatchAnalyzer> findByKey(String keyAndName);

    List<MatchAnalyzer> findByPuuID(String puuID);

}
