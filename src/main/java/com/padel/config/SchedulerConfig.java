package com.padel.config;
import com.padel.service.MatchService;
import com.padel.service.PaiementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {
    private final MatchService matchService;
    private final PaiementService paiementService;
    @Scheduled(cron = "0 0 0 * * *")
    public void checkPrivateMatchesVeille() {
        log.info("Verification matches prives incomplets...");
        matchService.checkAndConvertPrivateMatchesVeille();
    }
    @Scheduled(cron = "0 0 8 * * *")
    public void checkMissingPayments() {
        log.info("Verification paiements manquants...");
        paiementService.verifierPaiementsVeille();
    }
}
