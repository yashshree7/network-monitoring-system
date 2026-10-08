package com.example.networkmonitoringsystem.cotroller;

import com.example.networkmonitoringsystem.dto.DashboardSummaryResponse;
import com.example.networkmonitoringsystem.dto.NetworkKpiResponse;
import com.example.networkmonitoringsystem.entity.MonitoredHost;
import com.example.networkmonitoringsystem.entity.MonitoringResult;
import com.example.networkmonitoringsystem.entity.NetworkIncident;
import com.example.networkmonitoringsystem.repository.MonitoredHostRepository;
import com.example.networkmonitoringsystem.repository.MonitoringResultRepository;
import com.example.networkmonitoringsystem.repository.NetworkIncidentRepository;
import com.example.networkmonitoringsystem.service.NetworkMonitoringService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NetworkMonitoringController {

    private final MonitoredHostRepository monitoredHostRepository;
    private final MonitoringResultRepository monitoringResultRepository;
    private final NetworkIncidentRepository networkIncidentRepository;
    private final NetworkMonitoringService networkMonitoringService;

    public NetworkMonitoringController(
            MonitoredHostRepository monitoredHostRepository,
            MonitoringResultRepository monitoringResultRepository,
            NetworkIncidentRepository networkIncidentRepository,
            NetworkMonitoringService networkMonitoringService) {

        this.monitoredHostRepository = monitoredHostRepository;
        this.monitoringResultRepository = monitoringResultRepository;
        this.networkIncidentRepository = networkIncidentRepository;
        this.networkMonitoringService = networkMonitoringService;
    }


    // Add a new host
    @PostMapping("/hosts")
    public MonitoredHost addHost(
            @RequestBody MonitoredHost host) {

        return monitoredHostRepository.save(host);
    }


    // Get all monitored hosts
    @GetMapping("/hosts")
    public List<MonitoredHost> getAllHosts() {

        return monitoredHostRepository.findAll();
    }


    // Update an existing host
    @PutMapping("/hosts/{id}")
    public MonitoredHost updateHost(
            @PathVariable Long id,
            @RequestBody MonitoredHost updatedHost) {

        MonitoredHost existingHost =
                monitoredHostRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Host not found with ID: "
                                                + id));

        existingHost.setHostName(
                updatedHost.getHostName());

        existingHost.setHostAddress(
                updatedHost.getHostAddress());

        existingHost.setDescription(
                updatedHost.getDescription());

        existingHost.setActive(
                updatedHost.isActive());

        return monitoredHostRepository.save(
                existingHost);
    }


    // Check a specific host
    @PostMapping("/monitoring/check/{id}")
    public MonitoringResult checkHost(
            @PathVariable Long id) {

        return networkMonitoringService.checkHost(id);
    }


    // Get monitoring history for a host
    @GetMapping("/monitoring/history/{id}")
    public List<MonitoringResult> getMonitoringHistory(
            @PathVariable Long id) {

        return monitoringResultRepository
                .findByHostIdOrderByCheckedAtDesc(id);
    }


    // Get incidents for a host
    @GetMapping("/incidents/{id}")
    public List<NetworkIncident> getIncidents(
            @PathVariable Long id) {

        return networkIncidentRepository
                .findByHostIdOrderByCreatedAtDesc(id);
    }


    // Get network KPI summary for a host
    @GetMapping("/monitoring/kpi/{id}")
    public NetworkKpiResponse getKpiSummary(
            @PathVariable Long id) {

        return networkMonitoringService
                .getKpiSummary(id);
    }


    // Get overall dashboard summary
    @GetMapping("/dashboard/summary")
    public DashboardSummaryResponse getDashboardSummary() {

        return networkMonitoringService
                .getDashboardSummary();
    }
}