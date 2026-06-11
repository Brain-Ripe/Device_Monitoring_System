package com.DevMon.controller;

import com.DevMon.entity.Device;
import com.DevMon.service.DeviceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    private final DeviceService deviceService;


//    Constructor Injection
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping("/register")
    public Device registerDevice(@RequestBody Device device) {
        return deviceService.registerDevice(device);
    }

    @GetMapping("/{id}")
    public Optional<Device> getDevice(@PathVariable Long id) {
        return deviceService.getDeviceById(id);
    }

    @GetMapping
    public List<Device> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @DeleteMapping("/{id}")
    public void deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
    }
}