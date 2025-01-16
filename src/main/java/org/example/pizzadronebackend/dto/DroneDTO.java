package org.example.pizzadronebackend.dto;

public class DroneDTO {

    private Long droneId;
    private String serialUuid;
    private String driftsstatus;
    private double stationLatitude;
    private double stationLongitude;

    // Constructor
    public DroneDTO(Long droneId, String serialUuid, String driftsstatus, double stationLatitude, double stationLongitude) {
        this.droneId = droneId;
        this.serialUuid = serialUuid;
        this.driftsstatus = driftsstatus;
        this.stationLatitude = stationLatitude;
        this.stationLongitude = stationLongitude;
    }

    public Long getDroneId() {
        return droneId;
    }
    public void setDroneId(Long droneId) {
        this.droneId = droneId;
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

