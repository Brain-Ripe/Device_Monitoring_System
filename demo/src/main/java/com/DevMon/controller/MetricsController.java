package com.DevMon.controller;

import com.DevMon.dto.DeviceMetricsDTO;
import com.DevMon.entity.Device;
import com.DevMon.repository.DeviceRepository;
import com.DevMon.service.DeviceService;
import com.DevMon.service.MetricsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private final MetricsService metricsService;
    private final DeviceService deviceService;

    public MetricsController(MetricsService metricsService, DeviceService deviceService) {
        this.metricsService = metricsService;
        this.deviceService=deviceService;
    }


//    @PostMapping
//    public void ingestMetrics(@RequestBody DeviceMetricsDTO metrics) {
//        metricsService.ingestMetrics(metrics);
//    }

    @PostMapping
    public ResponseEntity<?> receiveMetrics(
            @RequestHeader("X-API-KEY") String apiKey,
            @RequestBody DeviceMetricsDTO metrics) {


        Device device = deviceService.findByApiKey(apiKey);

        if (device == null) {
            return ResponseEntity.status(401).body("Invalid API Key");
        }

        metricsService.processMetrics(device, metrics);

        return ResponseEntity.ok("Metrics received");
    }
}