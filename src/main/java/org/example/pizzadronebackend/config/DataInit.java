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

import java.util.Optional;
import java.util.Random;

@Configuration
public class DataInit implements CommandLineRunner {

    @Autowired
    private PizzaRepository pizzaRepo;

    @Autowired
    private StationRepository stationRepo;

    @Autowired
    private DroneRepository droneRepo;

    public DataInit(PizzaRepository pizzaRepo, StationRepository stationRepo, DroneRepository droneRepo) {
        this.pizzaRepo = pizzaRepo;
        this.stationRepo = stationRepo;
        this.droneRepo = droneRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("DataInit: Initialisering af data...");

            // Opret pizzaer, hvis der ikke allerede er nogen
            if (pizzaRepo.count() == 0) {
                pizzaRepo.save(new Pizza("Margherita", 60));
                pizzaRepo.save(new Pizza("Pepperoni", 75));
                pizzaRepo.save(new Pizza("Hawaii", 70));
                pizzaRepo.save(new Pizza("Veggie", 65));
                pizzaRepo.save(new Pizza("BBQ Chicken", 80));
                System.out.println("Pizzaer tilføjet til databasen.");
            } else {
                System.out.println("Pizzaer findes allerede i databasen.");
            }

            // Opret stationer, hvis der ikke allerede er nogen
            if (stationRepo.count() == 0) {
                stationRepo.save(new Station(55.41, 12.34)); // Centrum
                stationRepo.save(new Station(55.42, 12.33)); // Østerbro
                stationRepo.save(new Station(55.40, 12.35)); // Amager
                System.out.println("Stationer tilføjet til databasen.");
            } else {
                System.out.println("Stationer findes allerede i databasen.");
            }

            // Generer droner, hvis der ikke allerede er nogen
            if (droneRepo.count() == 0) {
                generateRandomDrones();
                System.out.println("Droner tilføjet til databasen.");
            } else {
                System.out.println("Droner findes allerede i databasen.");
            }

            System.out.println("DataInit: Initialisering færdig.");
        } catch (Exception e) {
            System.err.println("Fejl under initialisering af data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void generateRandomDrones() {
        Optional<Station> station1 = stationRepo.findById(1L);
        Optional<Station> station2 = stationRepo.findById(2L);
        Optional<Station> station3 = stationRepo.findById(3L);

        if (station1.isPresent() && station2.isPresent() && station3.isPresent()) {
            droneRepo.save(new Drone(generateCustomUUID(), "i drift", station1.get()));
            droneRepo.save(new Drone(generateCustomUUID(), "ude af drift", station2.get()));
            droneRepo.save(new Drone(generateCustomUUID(), "udfaset", station3.get()));
        } else {
            System.err.println("Stationer mangler i databasen. Kan ikke tilføje droner.");
        }
    }

    private String generateCustomUUID() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000; // Generer et tal mellem 1000 og 9999
        return "UUID-" + randomNumber;
    }
}
