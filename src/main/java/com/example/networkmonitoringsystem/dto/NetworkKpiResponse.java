package com.example.networkmonitoringsystem.dto;

public class NetworkKpiResponse {

    private Long hostId;
    private long totalChecks;
    private long upChecks;
    private long downChecks;
    private double availabilityPercentage;
    private long failureCount;
    private double averageResponseTime;

    public NetworkKpiResponse() {
    }

    public NetworkKpiResponse(
            Long hostId,
            long totalChecks,
            long upChecks,
            long downChecks,
            double availabilityPercentage,
            long failureCount,
            double averageResponseTime) {

        this.hostId = hostId;
        this.totalChecks = totalChecks;
        this.upChecks = upChecks;
        this.downChecks = downChecks;
        this.availabilityPercentage = availabilityPercentage;
        this.failureCount = failureCount;
        this.averageResponseTime = averageResponseTime;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public long getTotalChecks() {
        return totalChecks;
    }

    public void setTotalChecks(long totalChecks) {
        this.totalChecks = totalChecks;
    }

    public long getUpChecks() {
        return upChecks;
    }

    public void setUpChecks(long upChecks) {
        this.upChecks = upChecks;
    }

    public long getDownChecks() {
        return downChecks;
    }

    public void setDownChecks(long downChecks) {
        this.downChecks = downChecks;
    }

    public double getAvailabilityPercentage() {
        return availabilityPercentage;
    }

    public void setAvailabilityPercentage(double availabilityPercentage) {
        this.availabilityPercentage = availabilityPercentage;
    }

    public long getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(long failureCount) {
        this.failureCount = failureCount;
    }

    public double getAverageResponseTime() {
        return averageResponseTime;
    }

    public void setAverageResponseTime(double averageResponseTime) {
        this.averageResponseTime = averageResponseTime;
    }
}