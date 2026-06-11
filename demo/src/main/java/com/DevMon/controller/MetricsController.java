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
// 🔹 FIX 1: Synchronize the path mapping with your SecurityConfig and Go Agent
@RequestMapping("/api/v1/metrics")
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
            // 🔹 FIX 2: Live print statements so you can see the stats scroll in your IDE terminal
            logger.info("==================================================");
            logger.info("📥 INCOMING TELEMETRY PACKET DETECTED");
            logger.info("Device ID    : {}", metrics.getDeviceIdentifier());
            logger.info("CPU Usage    : {}%", metrics.getCpuUsage());
            logger.info("RAM Usage    : {}%", metrics.getRamUsage());
            logger.info("Network In   : {} Bytes", metrics.getNetworkIn());
            logger.info("Network Out  : {} Bytes", metrics.getNetworkOut());
            logger.info("==================================================");

            // Fetch the device mapping for database persistence
            Device device = deviceService.findByApiKey(apiKey);

            // Fallback safety layer in case the key exists but isn't tied to an active DB record
            if (device == null) {
                logger.warn("Security passed, but no device record found in DB for key: {}", apiKey);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Device record unlinked.");
            }

            // 2. Delegate to Service (Persistence to 'Weak' Entity)
            metricsService.processMetrics(device, metrics);

            // 3. Return 201 Created
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (Exception e) {
            logger.error("Failed to process metrics for device: {}", metrics.getDeviceIdentifier(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred.");
        }
    }
}