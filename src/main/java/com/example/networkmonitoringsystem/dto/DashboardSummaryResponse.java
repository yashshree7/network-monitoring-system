package com.example.networkmonitoringsystem.dto;

public class DashboardSummaryResponse {

    private long totalHosts;
    private long upHosts;
    private long downHosts;
    private double overallAvailability;

    public DashboardSummaryResponse() {
    }

    public DashboardSummaryResponse(
            long totalHosts,
            long upHosts,
            long downHosts,
            double overallAvailability) {

        this.totalHosts = totalHosts;
        this.upHosts = upHosts;
        this.downHosts = downHosts;
        this.overallAvailability = overallAvailability;
    }

    public long getTotalHosts() {
        return totalHosts;
    }

    public void setTotalHosts(long totalHosts) {
        this.totalHosts = totalHosts;
    }

    public long getUpHosts() {
        return upHosts;
    }

    public void setUpHosts(long upHosts) {
        this.upHosts = upHosts;
    }

    public long getDownHosts() {
        return downHosts;
    }

    public void setDownHosts(long downHosts) {
        this.downHosts = downHosts;
    }

    public double getOverallAvailability() {
        return overallAvailability;
    }

    public void setOverallAvailability(double overallAvailability) {
        this.overallAvailability = overallAvailability;
    }
}