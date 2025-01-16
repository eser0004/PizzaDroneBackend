package org.example.pizzadronebackend.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stationId;

    private double latitude; //breddegrad
    private double longitude; //længdegrad, kommatal

    @OneToMany(mappedBy = "station")
    @JsonBackReference // serialiser relationen
    private List<Drone> droner = new ArrayList<>();

    // Tom constructor (kræves af JPA)
    public Station() {}

    // Constructor til at initialisere Station med latitude og longitude
    public Station(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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

    public List<Drone> getDroner() {
        return droner;
    }

    public void setDroner(List<Drone> droner) {
        this.droner = droner;
    }
}
