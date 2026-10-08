package com.example.networkmonitoringsystem.repository;

import com.example.networkmonitoringsystem.entity.MonitoringResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonitoringResultRepository extends JpaRepository<MonitoringResult, Long> {

    List<MonitoringResult> findByHostIdOrderByCheckedAtDesc(Long hostId);
}