package com.arunava.networktool.model;

public class NetworkStatusModel {
    private final String connectionType;
    private final String localIp;
    private final String linkSpeed;
    private final boolean isConnected;

    public NetworkStatusModel(String connectionType, String localIp, String linkSpeed, boolean isConnected) {
        this.connectionType = connectionType;
        this.localIp = localIp;
        this.linkSpeed = linkSpeed;
        this.isConnected = isConnected;
    }

    public String getConnectionType() { return connectionType; }
    public String getLocalIp() { return localIp; }
    public String getLinkSpeed() { return linkSpeed; }
    public boolean isConnected() { return isConnected; }
}
