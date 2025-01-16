package org.example.pizzadronebackend.dto;

import org.example.pizzadronebackend.model.Station;

public class StationDTO {
    private Long stationId;
    private double latitude;
    private double longitude;
    private int antalDroner;

    public StationDTO(Station station) {
        this.stationId = station.getStationId();
        this.latitude = station.getLatitude();
        this.longitude = station.getLongitude();
        this.antalDroner = station.getDroner().size();

        // Sørg for, at listen ikke er null
        this.antalDroner = (station.getDroner() != null) ? station.getDroner().size() : 0;
    }


    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getAntalDroner() {
        return antalDroner;
    }

    public void setAntalDroner(int antalDroner) {
        this.antalDroner = antalDroner;
    }
}
