package com.padel.repository;
import com.padel.model.entity.JoueurMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface JoueurMatchRepository extends JpaRepository<JoueurMatch, Long> {
    List<JoueurMatch> findByMatchId(Long matchId);
    List<JoueurMatch> findByUserId(Long userId);
    boolean existsByMatchIdAndUserId(Long matchId, Long userId);
    long countByMatchId(Long matchId);
}
