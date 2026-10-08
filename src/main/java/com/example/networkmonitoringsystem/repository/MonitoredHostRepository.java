package com.example.networkmonitoringsystem.repository;

import com.example.networkmonitoringsystem.entity.MonitoredHost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoredHostRepository extends JpaRepository<MonitoredHost, Long> {
}