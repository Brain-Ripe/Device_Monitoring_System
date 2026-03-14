package com.DevMon.controller;

import com.DevMon.dto.DeviceMetricsDTO;
import com.DevMon.service.MetricsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @PostMapping
    public void ingestMetrics(@RequestBody DeviceMetricsDTO metrics) {
        metricsService.ingestMetrics(metrics);
    }
}