package com.example.networkmonitoringsystem.service;

import com.example.networkmonitoringsystem.dto.DashboardSummaryResponse;
import com.example.networkmonitoringsystem.dto.NetworkKpiResponse;
import com.example.networkmonitoringsystem.entity.MonitoredHost;
import com.example.networkmonitoringsystem.entity.MonitoringResult;
import com.example.networkmonitoringsystem.entity.NetworkIncident;
import com.example.networkmonitoringsystem.repository.MonitoredHostRepository;
import com.example.networkmonitoringsystem.repository.MonitoringResultRepository;
import com.example.networkmonitoringsystem.repository.NetworkIncidentRepository;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NetworkMonitoringService {

    private final MonitoredHostRepository monitoredHostRepository;
    private final MonitoringResultRepository monitoringResultRepository;
    private final NetworkIncidentRepository networkIncidentRepository;

    public NetworkMonitoringService(
            MonitoredHostRepository monitoredHostRepository,
            MonitoringResultRepository monitoringResultRepository,
            NetworkIncidentRepository networkIncidentRepository) {

        this.monitoredHostRepository = monitoredHostRepository;
        this.monitoringResultRepository = monitoringResultRepository;
        this.networkIncidentRepository = networkIncidentRepository;
    }

    public MonitoringResult checkHost(Long hostId) {

        MonitoredHost host = monitoredHostRepository.findById(hostId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Host not found with ID: " + hostId));

        MonitoringResult result = new MonitoringResult();

        result.setHost(host);
        result.setCheckedAt(LocalDateTime.now());

        try {

            InetAddress address =
                    InetAddress.getByName(host.getHostAddress());

            long startTime = System.currentTimeMillis();

            boolean reachable =
                    address.isReachable(3000);

            long responseTime =
                    System.currentTimeMillis() - startTime;

            result.setResponseTime(responseTime);

            if (reachable) {

                result.setStatus("UP");
                result.setErrorMessage(null);

                closeOpenIncident(host);

            } else {

                result.setStatus("DOWN");
                result.setErrorMessage(
                        "Host is not reachable");

                createIncidentIfNotAlreadyOpen(
                        host,
                        "HOST_UNREACHABLE",
                        "Host " + host.getHostAddress()
                                + " is not reachable"
                );
            }

        } catch (Exception e) {

            result.setStatus("DOWN");
            result.setResponseTime(null);
            result.setErrorMessage(e.getMessage());

            createIncidentIfNotAlreadyOpen(
                    host,
                    "MONITORING_ERROR",
                    e.getMessage()
            );
        }

        return monitoringResultRepository.save(result);
    }


    private void createIncidentIfNotAlreadyOpen(
            MonitoredHost host,
            String incidentType,
            String description) {

        NetworkIncident existingIncident =
                networkIncidentRepository
                        .findFirstByHostIdAndStatusOrderByCreatedAtDesc(
                                host.getId(),
                                "OPEN"
                        );

        if (existingIncident == null) {

            NetworkIncident incident =
                    new NetworkIncident();

            incident.setHost(host);
            incident.setIncidentType(incidentType);
            incident.setDescription(description);
            incident.setStatus("OPEN");
            incident.setCreatedAt(LocalDateTime.now());

            networkIncidentRepository.save(incident);
        }
    }


    private void closeOpenIncident(MonitoredHost host) {

        NetworkIncident existingIncident =
                networkIncidentRepository
                        .findFirstByHostIdAndStatusOrderByCreatedAtDesc(
                                host.getId(),
                                "OPEN"
                        );

        if (existingIncident != null) {

            existingIncident.setStatus("CLOSED");

            networkIncidentRepository.save(existingIncident);
        }
    }


    public NetworkKpiResponse getKpiSummary(Long hostId) {

        MonitoredHost host =
                monitoredHostRepository.findById(hostId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Host not found with ID: "
                                                + hostId));

        List<MonitoringResult> results =
                monitoringResultRepository
                        .findByHostIdOrderByCheckedAtDesc(hostId);

        long totalChecks = results.size();

        long upChecks = results.stream()
                .filter(result ->
                        "UP".equalsIgnoreCase(
                                result.getStatus()))
                .count();

        long downChecks = results.stream()
                .filter(result ->
                        "DOWN".equalsIgnoreCase(
                                result.getStatus()))
                .count();

        double availabilityPercentage = 0.0;

        if (totalChecks > 0) {

            availabilityPercentage =
                    (upChecks * 100.0) / totalChecks;
        }

        long failureCount = downChecks;

        double averageResponseTime =
                results.stream()
                        .filter(result ->
                                result.getResponseTime() != null)
                        .mapToLong(
                                MonitoringResult::getResponseTime)
                        .average()
                        .orElse(0.0);

        return new NetworkKpiResponse(
                host.getId(),
                totalChecks,
                upChecks,
                downChecks,
                availabilityPercentage,
                failureCount,
                averageResponseTime
        );
    }


    public DashboardSummaryResponse getDashboardSummary() {

        List<MonitoredHost> hosts =
                monitoredHostRepository.findAll();

        long totalHosts = hosts.size();

        long upHosts = 0;
        long downHosts = 0;

        long totalChecks = 0;
        long upChecks = 0;

        for (MonitoredHost host : hosts) {

            List<MonitoringResult> results =
                    monitoringResultRepository
                            .findByHostIdOrderByCheckedAtDesc(
                                    host.getId());

            if (!results.isEmpty()) {

                MonitoringResult latestResult =
                        results.get(0);

                if ("UP".equalsIgnoreCase(
                        latestResult.getStatus())) {

                    upHosts++;

                } else {

                    downHosts++;
                }

                totalChecks += results.size();

                upChecks += results.stream()
                        .filter(result ->
                                "UP".equalsIgnoreCase(
                                        result.getStatus()))
                        .count();

            } else {

                downHosts++;
            }
        }

        double overallAvailability = 0.0;

        if (totalChecks > 0) {

            overallAvailability =
                    (upChecks * 100.0) / totalChecks;
        }

        return new DashboardSummaryResponse(
                totalHosts,
                upHosts,
                downHosts,
                overallAvailability
        );
    }
}