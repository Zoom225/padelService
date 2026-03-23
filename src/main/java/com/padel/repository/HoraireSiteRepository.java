package com.padel.repository;
import com.padel.model.entity.HoraireSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface HoraireSiteRepository extends JpaRepository<HoraireSite, Long> {
    Optional<HoraireSite> findBySiteIdAndAnnee(Long siteId, Integer annee);
    List<HoraireSite> findBySiteId(Long siteId);
}
