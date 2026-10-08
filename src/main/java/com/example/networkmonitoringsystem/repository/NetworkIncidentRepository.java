package com.example.networkmonitoringsystem.repository;

import com.example.networkmonitoringsystem.entity.NetworkIncident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NetworkIncidentRepository extends JpaRepository<NetworkIncident, Long> {

    List<NetworkIncident> findByHostIdOrderByCreatedAtDesc(Long hostId);

    NetworkIncident findFirstByHostIdAndStatusOrderByCreatedAtDesc(
            Long hostId,
            String status
    );
}