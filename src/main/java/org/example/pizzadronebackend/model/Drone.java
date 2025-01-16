package org.example.pizzadronebackend.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long droneId;

    @Column(unique = true)
    private String serialUuid;

    private String driftsstatus; // "i drift", "ude af drift", "udfaset"

    @ManyToOne
    private Station station;

    @OneToMany(mappedBy = "drone")
    private List<Levering> leveringer = new ArrayList<>();

    // Tom constructor (kræves af JPA)
    public Drone() {}

    // Constructor til at initialisere Drone med serialUuid, driftsstatus og station
    public Drone(String serialUuid, String driftsstatus, Station station) {
        this.serialUuid = serialUuid;
        this.driftsstatus = driftsstatus;
        this.station = station;
    }

    public Long getDroneId() {
        return droneId;
    }

    public void setDroneId(Long droneId) {
        this.droneId = droneId;
    }

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

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public List<Levering> getLeveringer() {
        return leveringer;
    }

    public void setLeveringer(List<Levering> leveringer) {
        this.leveringer = leveringer;
    }
}
