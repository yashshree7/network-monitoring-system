package com.example.networkmonitoringsystem.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "monitoring_results")
public class MonitoringResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "host_id", nullable = false)
    private MonitoredHost host;

    @Column(nullable = false)
    private String status;

    private Long responseTime;

    private String errorMessage;

    @Column(nullable = false)
    private LocalDateTime checkedAt;

    public MonitoringResult() {
    }

    public MonitoringResult(
            MonitoredHost host,
            String status,
            Long responseTime,
            String errorMessage
    ) {
        this.host = host;
        this.status = status;
        this.responseTime = responseTime;
        this.errorMessage = errorMessage;
        this.checkedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public MonitoredHost getHost() {
        return host;
    }

    public void setHost(MonitoredHost host) {
        this.host = host;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(LocalDateTime checkedAt) {
        this.checkedAt = checkedAt;
    }
}