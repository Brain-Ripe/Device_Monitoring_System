package com.DevMon.controller;

import com.DevMon.dto.DeviceMetricsDTO;
import com.DevMon.entity.Device;
import com.DevMon.service.DeviceService;
import com.DevMon.service.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private static final Logger logger = LoggerFactory.getLogger(MetricsController.class);
    private final MetricsService metricsService;
    private final DeviceService deviceService;

    public MetricsController(MetricsService metricsService, DeviceService deviceService) {
        this.metricsService = metricsService;
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<?> receiveMetrics(
            @RequestHeader(value = "X-API-KEY", required = true) String apiKey,
            @RequestBody DeviceMetricsDTO metrics) {

        try {
            // 1. Auth Check
            Device device = deviceService.findByApiKey(apiKey);

            if (device == null) {
                logger.warn("Unauthorized access attempt with API Key: {}", apiKey);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key");
            }

            // 2. Delegate to Service (Persistence to 'Weak' Entity)
            metricsService.processMetrics(device, metrics);

            // 3. Return 201 Created (REST Standard for new data)
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (Exception e) {
            logger.error("Failed to process metrics for device: {}", metrics.getDeviceIdentifier(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred.");
        }
    }
}