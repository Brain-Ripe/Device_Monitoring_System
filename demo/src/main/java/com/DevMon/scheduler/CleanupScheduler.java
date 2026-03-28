package com.DevMon.scheduler;

import com.DevMon.service.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CleanupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(CleanupScheduler.class);
    private final MetricsService metricsService;

    // Standard Constructor Injection
    public CleanupScheduler(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    /**
     * fixedRate = 3600000 ms (1 Hour)
     * This will trigger the service method to flush data older than 48 hours.
     */
    @Scheduled(fixedRate = 60000)
    public void runMetricCleanup() {
        logger.info("CRON START: Initiating 2-mins old metric retention flush...");

        try {
            metricsService.flushOldMetrics();
            logger.info("CRON SUCCESS: Old metrics purged.");
        } catch (Exception e) {
            logger.error("CRON FAILURE: Could not complete metrics flush", e);
        }
    }
}