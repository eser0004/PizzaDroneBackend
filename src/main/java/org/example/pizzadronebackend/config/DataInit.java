package org.example.pizzadronebackend.config;

import org.example.pizzadronebackend.model.Drone;
import org.example.pizzadronebackend.model.Pizza;
import org.example.pizzadronebackend.model.Station;
import org.example.pizzadronebackend.repository.DroneRepository;
import org.example.pizzadronebackend.repository.PizzaRepository;
import org.example.pizzadronebackend.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class DataInit implements CommandLineRunner {
    @Autowired
    PizzaRepository pizzaRepo;
    @Autowired
    StationRepository stationRepo;
    @Autowired
    DroneRepository droneRepo;

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

        // Generer droner
        generateRandomDrones();
    }

    public void generateRandomDrones() {
        // Find stationer
        Station station1 = stationRepo.findById(1L).orElseThrow(() -> new IllegalArgumentException("Station 1 findes ikke."));
        Station station2 = stationRepo.findById(2L).orElseThrow(() -> new IllegalArgumentException("Station 2 findes ikke."));
        Station station3 = stationRepo.findById(3L).orElseThrow(() -> new IllegalArgumentException("Station 3 findes ikke."));

        // Opret droner med tilpassede UUID'er
        droneRepo.save(new Drone(generateCustomUUID(), "i drift", station1));
        droneRepo.save(new Drone(generateCustomUUID(), "ude af drift", station2));
        droneRepo.save(new Drone(generateCustomUUID(), "udfaset", station3));
    }

    private String generateCustomUUID() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000; // Generer et tal mellem 1000 og 9999
        return "UUID-" + randomNumber;
    }
}
