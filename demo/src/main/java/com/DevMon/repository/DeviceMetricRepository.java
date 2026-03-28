package com.DevMon.repository;

import com.DevMon.entity.DeviceMetric;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeviceMetricRepository extends JpaRepository<DeviceMetric, Long> {


    List<DeviceMetric> findByDeviceIdOrderByRecordedAtDesc(Long deviceId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM DeviceMetric m WHERE m.recordedAt < :cutoff")
    void deleteMetricsOlderThan(@Param("cutoff") LocalDateTime cutoff);
}