package com.DevMon.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.LocalDateTime;

@Entity
@Table(name = "device_metrics")
public class DeviceMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // Ensures "Weak" behavior
    private Device device;

    private Double cpuUsage;
    private Double ramUsageBytes;

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt = LocalDateTime.now();

    public DeviceMetric() {}


    public Long getId() { return id; }
    public Device getDevice() { return device; }
    public void setDevice(Device device) { this.device = device; }
    public Double getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(Double cpuUsage) { this.cpuUsage = cpuUsage; }
    public Double getRamUsageBytes() { return ramUsageBytes; }
    public void setRamUsageBytes(Double ramUsageBytes) { this.ramUsageBytes = ramUsageBytes; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
}