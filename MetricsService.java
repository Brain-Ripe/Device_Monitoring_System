package com.DevMon.service;

import com.DevMon.dto.DeviceMetricsDTO;
import com.DevMon.entity.Device;
import com.DevMon.entity.DeviceMetric;
import com.DevMon.repository.DeviceMetricRepository;
import com.DevMon.repository.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MetricsService {

    private static final Logger logger = LoggerFactory.getLogger(MetricsService.class);
    private final DeviceRepository deviceRepository;
    private final DeviceMetricRepository metricsRepository;

    public MetricsService(DeviceRepository deviceRepository, DeviceMetricRepository metricsRepository) {
        this.deviceRepository = deviceRepository;
        this.metricsRepository = metricsRepository;
    }

    @Transactional
    public void processMetrics(Device device, DeviceMetricsDTO metrics) {
        // 1. Map DTO to Entity (Ensure all fields from Go Agent are here)
        DeviceMetric deviceMetric = new DeviceMetric();
        deviceMetric.setDevice(device);
        deviceMetric.setCpuUsage(metrics.getCpuUsage());
        deviceMetric.setRamUsageBytes(metrics.getRamUsage());
        
        // Ensure your DeviceMetric entity has these fields to stop losing data!
        // deviceMetric.setGpuUsage(metrics.getGpuUsage());
        // deviceMetric.setTemperature(metrics.getTemperature());

        // 2. Persist the 'Weak' record
        metricsRepository.save(deviceMetric);

        // 3. Update 'Strong' parent state
        // Hibernate Dirty Checking will sync this to DB at the end of the transaction
        device.setLastSeen(LocalDateTime.now());
        device.setStatus("ONLINE");
        
        logger.debug("Metrics persisted for device: {}", device.getDeviceIdentifier());
    }

    @Transactional
    public void flushOldMetrics() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(5);
        metricsRepository.deleteMetricsOlderThan(cutoff);
        logger.info("Flushed metrics older than: {}", cutoff);
    }
}