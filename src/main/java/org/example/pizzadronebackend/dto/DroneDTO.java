package org.example.pizzadronebackend.dto;

public class DroneDTO {

    private String serialUuid;
    private String driftsstatus;
    private double stationLatitude;
    private double stationLongitude;

    // Constructor
    public DroneDTO(String serialUuid, String driftsstatus, double stationLatitude, double stationLongitude) {
        this.serialUuid = serialUuid;
        this.driftsstatus = driftsstatus;
        this.stationLatitude = stationLatitude;
        this.stationLongitude = stationLongitude;
    }

    // Getters and Setters
    public String getSerialUuid() {
        return serialUuid;
    }

    public void setSerialUuid(String serialUuid) {
        this.serialUuid = serialUuid;
    }

    public String getDriftsstatus() {
        return driftsstatus;
    }

    public void setDriftsstatus(String driftsstatus) {
        this.driftsstatus = driftsstatus;
    }

    public double getStationLatitude() {
        return stationLatitude;
    }

    public void setStationLatitude(double stationLatitude) {
        this.stationLatitude = stationLatitude;
    }

    public double getStationLongitude() {
        return stationLongitude;
    }

    public void setStationLongitude(double stationLongitude) {
        this.stationLongitude = stationLongitude;
    }
}

