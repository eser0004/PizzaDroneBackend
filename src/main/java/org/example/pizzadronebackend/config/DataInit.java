package org.example.pizzadronebackend.config;

import org.example.pizzadronebackend.model.Drone;
import org.example.pizzadronebackend.model.Pizza;
import org.example.pizzadronebackend.model.Station;
import org.example.pizzadronebackend.repository.DroneRepository;
import org.example.pizzadronebackend.repository.PizzaRepository;
import org.example.pizzadronebackend.repository.StationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner {
    private final PizzaRepository pizzaRepo;
    private final StationRepository stationRepo;
    private final DroneRepository droneRepo;

    public DataInit(PizzaRepository pizzaRepo, StationRepository stationRepo, DroneRepository droneRepo) {
        this.pizzaRepo = pizzaRepo;
        this.stationRepo = stationRepo;
        this.droneRepo = droneRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        // Opret pizzaer
        pizzaRepo.save(new Pizza("Margherita", 60));
        pizzaRepo.save(new Pizza("Pepperoni", 75));
        pizzaRepo.save(new Pizza("Hawaii", 70));
        pizzaRepo.save(new Pizza("Veggie", 65));
        pizzaRepo.save(new Pizza("BBQ Chicken", 80));

        // Opret stationer
        stationRepo.save(new Station(55.41, 12.34)); // Centrum
        stationRepo.save(new Station(55.42, 12.33)); // Nær Østerbro
        stationRepo.save(new Station(55.40, 12.35)); // Nær Amager

        // Opret droner
        droneRepo.save(new Drone("UUID-1234", "i drift", stationRepo.findById(1L).get()));
        droneRepo.save(new Drone("UUID-5678", "ude af drift", stationRepo.findById(2L).get()));
    }
}
