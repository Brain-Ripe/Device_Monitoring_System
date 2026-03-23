package com.DevMon.service;

import com.DevMon.dto.DeviceMetricsDTO;
import com.DevMon.entity.Device;
import com.DevMon.repository.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MetricsService {

    private final DeviceRepository deviceRepository;

    public MetricsService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

//    public void ingestMetrics(DeviceMetricsDTO metrics) {
//
//        Optional<Device> device = deviceRepository
//                .findByDeviceIdentifier(metrics.getDeviceIdentifier());
//
//        if (device.isEmpty()) {
//            throw new RuntimeException("Device not registered");
//        }
//
//        System.out.println("Metrics received from: " + metrics.getDeviceIdentifier());
//        System.out.println("CPU: " + metrics.getCpuUsage());
//        System.out.println("RAM: " + metrics.getRamUsage());
//    }

    public void processMetrics(Device device, DeviceMetricsDTO metrics) {

        System.out.println("Metrics received from: " + device.getDeviceIdentifier());

        System.out.println("CPU: " + metrics.getCpuUsage());
        System.out.println("RAM: " + metrics.getRamUsage());
        System.out.println("GPU: " + metrics.getGpuUsage());
        System.out.println("Network In: " + metrics.getNetworkIn());
        System.out.println("Network Out: " + metrics.getNetworkOut());
        System.out.println("Temp: " + metrics.getTemperature());
    }
}