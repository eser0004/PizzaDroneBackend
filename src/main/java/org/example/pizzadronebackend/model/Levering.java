package org.example.pizzadronebackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Levering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leveringId;
    private String adresse;
    private LocalDateTime forventetLevering;
    private LocalDateTime faktiskLevering;

    @ManyToOne
    @JsonManagedReference // Serialiser relationen
    private Drone drone;

    @ManyToOne
    private Pizza pizza;

    @Column(nullable = false)
    private int quantity;

    public Levering() {

    }


    public Long getLeveringId() {
        return leveringId;
    }

    public void setLeveringId(Long leveringId) {
        this.leveringId = leveringId;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDateTime getForventetLevering() {
        return forventetLevering;
    }

    public void setForventetLevering(LocalDateTime forventetLevering) {
        this.forventetLevering = forventetLevering;
    }

    public LocalDateTime getFaktiskLevering() {
        return faktiskLevering;
    }

    public void setFaktiskLevering(LocalDateTime faktiskLevering) {
        this.faktiskLevering = faktiskLevering;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
