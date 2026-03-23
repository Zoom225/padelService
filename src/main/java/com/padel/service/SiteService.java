package com.padel.service;
import com.padel.exception.BusinessException;
import com.padel.exception.ResourceNotFoundException;
import com.padel.model.entity.Site;
import com.padel.model.entity.Terrain;
import com.padel.repository.SiteRepository;
import com.padel.repository.TerrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SiteService {
    private final SiteRepository siteRepository;
    private final TerrainRepository terrainRepository;
    public List<Site> getAllActiveSites() { return siteRepository.findByActiveTrue(); }
    public List<Site> getAllSites() { return siteRepository.findAll(); }
    public Site getSiteById(Long id) {
        return siteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Site non trouve: " + id));
    }
    @Transactional
    public Site createSite(Site site) {
        if (siteRepository.findByName(site.getName()).isPresent())
            throw new BusinessException("Site deja existant: " + site.getName());
        return siteRepository.save(site);
    }
    @Transactional
    public Site updateSite(Long id, Site updated) {
        Site existing = getSiteById(id);
        existing.setName(updated.getName());
        existing.setAddress(updated.getAddress());
        existing.setDescription(updated.getDescription());
        existing.setOpeningTime(updated.getOpeningTime());
        existing.setClosingTime(updated.getClosingTime());
        return siteRepository.save(existing);
    }
    @Transactional
    public void deactivateSite(Long id) {
        Site site = getSiteById(id);
        site.setActive(false);
        siteRepository.save(site);
    }
    public List<Terrain> getTerrainsForSite(Long siteId) {
        getSiteById(siteId);
        return terrainRepository.findBySiteIdAndActiveTrue(siteId);
    }
}
