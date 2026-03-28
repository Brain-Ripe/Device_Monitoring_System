package com.DevMon.service;

import com.DevMon.dto.DeviceMetricsDTO;
import com.DevMon.entity.Device;
import com.DevMon.entity.DeviceMetric;
import com.DevMon.repository.DeviceMetricRepository;
import com.DevMon.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MetricsService {

    private final DeviceRepository deviceRepository;
    private final DeviceMetricRepository metricsRepository; // Added this

    public MetricsService(DeviceRepository deviceRepository, DeviceMetricRepository metricsRepository) {
        this.deviceRepository = deviceRepository;
        this.metricsRepository = metricsRepository;
    }

    @Transactional
    public void processMetrics(Device device, DeviceMetricsDTO metrics) {
        // 1. Create the 'Weak Entity' instance
        DeviceMetric deviceMetric = new DeviceMetric();

        // 2. Link the 'Strong' parent (The ID is extracted automatically)
        deviceMetric.setDevice(device);

        // 3. Map data from DTO to Entity
        deviceMetric.setCpuUsage(metrics.getCpuUsage());
        deviceMetric.setRamUsageBytes(metrics.getRamUsage());
        // If your entity has fields for these, map them too:
        // deviceMetric.setGpuUsage(metrics.getGpuUsage());
        // deviceMetric.setTemperature(metrics.getTemperature());

        // 4. Save to Supabase via the Repository
        metricsRepository.save(deviceMetric);

        // 5. Update the 'Strong' entity's status/lastSeen
        device.setLastSeen(LocalDateTime.now());
        device.setStatus("ONLINE");
        deviceRepository.save(device);

        System.out.println("Metrics persisted for: " + device.getDeviceIdentifier());
    }

    /**
     * Call this from a Scheduler to maintain your 48-hour window.
     */
    @Transactional
    public void flushOldMetrics() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(48);
        metricsRepository.deleteMetricsOlderThan(cutoff);


        System.out.println(" \n\n\n[CLEANUP] Successfully purged \n\n\n");

    }
}