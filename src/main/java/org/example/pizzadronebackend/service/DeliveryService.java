package org.example.pizzadronebackend.service;

import org.example.pizzadronebackend.model.Drone;
import org.example.pizzadronebackend.model.Levering;
import org.example.pizzadronebackend.model.Pizza;
import org.example.pizzadronebackend.repository.DeliveryRepository;
import org.example.pizzadronebackend.repository.DroneRepository;
import org.example.pizzadronebackend.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeliveryService {

    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    PizzaRepository pizzaRepository;
    @Autowired
    DroneRepository droneRepository;

    public DeliveryService(DeliveryRepository deliveryRepository, PizzaRepository pizzaRepository, DroneRepository droneRepository) {
        this.deliveryRepository = deliveryRepository;
        this.pizzaRepository = pizzaRepository;
        this.droneRepository = droneRepository;
    }

    // Opret en ny levering
    public Levering addDelivery(Long pizzaId, String adresse) {
        // Find pizzaen baseret på pizzaId
        Pizza pizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new IllegalArgumentException("Pizza med ID " + pizzaId + " findes ikke."));

        // Opret levering
        Levering levering = new Levering();
        levering.setAdresse(adresse);
        levering.setPizza(pizza);
        levering.setForventetLevering(LocalDateTime.now().plusMinutes(30)); // 30 minutter frem
        levering.setFaktiskLevering(null);
        levering.setDrone(null);

        // Gem levering i databasen
        return deliveryRepository.save(levering);
    }
    // Hent leveringer uden drone
    public List<Levering> getQueuedDeliveries() {
        return deliveryRepository.findByDroneIsNull();
    }

    // Hent alle leveringer, der ikke er færdige
    public List<Levering> getPendingDeliveries() {
        return deliveryRepository.findByFaktiskLeveringIsNull();
    }
    // Tildel en drone til en levering
    public Levering scheduleDelivery(Long leveringId, Long droneId) {
        // Find leveringen
        Levering levering = deliveryRepository.findById(leveringId)
                .orElseThrow(() -> new IllegalArgumentException("Levering med ID " + leveringId + " findes ikke."));

        // Sørg for, at leveringen ikke allerede har en drone
        if (levering.getDrone() != null) {
            throw new IllegalArgumentException("Leveringen har allerede en drone tildelt.");
        }

        Drone drone;

        // Hvis droneId er givet, find den specifikke drone
        if (droneId != null) {
            drone = droneRepository.findById(droneId)
                    .orElseThrow(() -> new IllegalArgumentException("Drone med ID " + droneId + " findes ikke."));
        } else {
            // Ellers vælg en tilfældig drone, der er "i drift"
            drone = droneRepository.findAll().stream()
                    .filter(d -> "i drift".equals(d.getDriftsstatus()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Ingen droner er i drift."));
        }

        // Sørg for, at dronen er "i drift"
        if (!"i drift".equals(drone.getDriftsstatus())) {
            throw new IllegalArgumentException("Drone med ID " + drone.getDroneId() + " er ikke i drift.");
        }

        // Tildel dronen til leveringen
        levering.setDrone(drone);

        // Gem opdateringen i databasen
        return deliveryRepository.save(levering);
    }
    // Markér en levering som afsluttet
    public Levering finishDelivery(Long leveringId) {
        // Find leveringen
        Levering levering = deliveryRepository.findById(leveringId)
                .orElseThrow(() -> new IllegalArgumentException("Levering med ID " + leveringId + " findes ikke."));

        // Sørg for, at leveringen har en drone tildelt
        if (levering.getDrone() == null) {
            throw new IllegalArgumentException("Leveringen har ingen drone tildelt.");
        }

        // Sørg for, at leveringen ikke allerede er færdig
        if (levering.getFaktiskLevering() != null) {
            throw new IllegalArgumentException("Leveringen er allerede markeret som afsluttet.");
        }

        // Sæt faktisk leveringstidspunkt til nu
        levering.setFaktiskLevering(LocalDateTime.now());

        // Gem opdateringen i databasen
        return deliveryRepository.save(levering);
    }
}
