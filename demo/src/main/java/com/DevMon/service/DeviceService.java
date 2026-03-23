package com.DevMon.service;

import com.DevMon.entity.Device;
import com.DevMon.repository.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Device registerDevice(Device device) {

        device.setApiKey(UUID.randomUUID().toString());

        return deviceRepository.save(device);
    }

    public Optional<Device> getDeviceById(Long id) {
        return deviceRepository.findById(id);
    }

    public Optional<Device> getDeviceByIdentifier(String identifier) {
        return deviceRepository.findByDeviceIdentifier(identifier);
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }

    public Device findByApiKey(String apiKey){ return deviceRepository.findByApiKey(apiKey);};
}