package com.arunava.networktool.model;

public class PingResult {
    private final String hostName;
    private final String ipAddress;
    private final long latencyMs;
    private final boolean isReachable;

    public PingResult(String hostName, String ipAddress, long latencyMs, boolean isReachable) {
        this.hostName = hostName;
        this.ipAddress = ipAddress;
        this.latencyMs = latencyMs;
        this.isReachable = isReachable;
    }

    public String getHostName() { return hostName; }
    public String getIpAddress() { return ipAddress; }
    public long getLatencyMs() { return latencyMs; }
    public boolean isReachable() { return isReachable; }
}
