package com.padel.repository;
import com.padel.model.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {
    List<Site> findByActiveTrue();
    Optional<Site> findByName(String name);
}
