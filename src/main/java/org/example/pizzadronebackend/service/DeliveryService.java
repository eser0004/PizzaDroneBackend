package org.example.pizzadronebackend.service;

import org.example.pizzadronebackend.model.Drone;
import org.example.pizzadronebackend.model.Levering;
import org.example.pizzadronebackend.model.Pizza;
import org.example.pizzadronebackend.repository.DeliveryRepository;
import org.example.pizzadronebackend.repository.DroneRepository;
import org.example.pizzadronebackend.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
    public Levering addDelivery(String adresse, Long pizzaId, int quantity) {
        Pizza pizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new IllegalArgumentException("Pizza med ID " + pizzaId + " findes ikke."));

        Levering levering = new Levering();
        levering.setAdresse(adresse);
        levering.setPizza(pizza);
        levering.setQuantity(quantity);
        levering.setForventetLevering(LocalDateTime.now().plusMinutes(30));

        return deliveryRepository.save(levering);
    }


    // Hent levering efter ID
    public Levering getDeliveryById(Long leveringId) {
        return deliveryRepository.findById(leveringId)
                .orElseThrow(() -> new IllegalArgumentException("Levering med ID " + leveringId + " findes ikke."));
    }

    // Hent leveringer uden drone
    public List<Levering> getQueuedDeliveries() {
        return deliveryRepository.findByDroneIsNull();
    }

    // Hent alle leveringer, der ikke er færdige
    public List<Levering> getPendingDeliveries() {
        return deliveryRepository.findByFaktiskLeveringIsNull();
    }

    // Hent status for en specifik levering
    public Map<String, Object> getDeliveryStatus(Long leveringId) {
        Levering levering = getDeliveryById(leveringId);

        Map<String, Object> status = new HashMap<>();
        status.put("leveringId", levering.getLeveringId());
        status.put("adresse", levering.getAdresse());
        status.put("status", levering.getDrone() == null ? "Mangler drone" :
                (levering.getFaktiskLevering() == null && LocalDateTime.now().isBefore(levering.getForventetLevering()))
                        ? "Under levering" : "Leveret");

        if (levering.getForventetLevering() != null && levering.getDrone() != null) {
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(now, levering.getForventetLevering());
            status.put("tidTilLevering", duration.isNegative() ? "0" : duration.toMinutes());
        } else {
            status.put("tidTilLevering", "Ikke startet");
        }

        return status;
    }

    // Tildel en drone til en levering
    public Levering scheduleDelivery(Long leveringId, Long droneId) {
        Levering levering = getDeliveryById(leveringId);

        if (levering.getDrone() != null) {
            throw new IllegalArgumentException("Leveringen har allerede en drone tildelt.");
        }

        Drone drone = (droneId != null)
                ? droneRepository.findById(droneId)
                .orElseThrow(() -> new IllegalArgumentException("Drone med ID " + droneId + " findes ikke."))
                : droneRepository.findAll().stream()
                .filter(d -> "i drift".equals(d.getDriftsstatus()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Ingen droner er i drift."));

        if (!"i drift".equals(drone.getDriftsstatus())) {
            throw new IllegalArgumentException("Drone med ID " + drone.getDroneId() + " er ikke i drift.");
        }

        levering.setDrone(drone);
        return deliveryRepository.save(levering);
    }

    // Markér en levering som afsluttet
    public Levering finishDelivery(Long leveringId) {
        Levering levering = getDeliveryById(leveringId);

        if (levering.getDrone() == null) {
            throw new IllegalArgumentException("Leveringen har ingen drone tildelt.");
        }

        if (levering.getFaktiskLevering() != null) {
            throw new IllegalArgumentException("Leveringen er allerede markeret som afsluttet.");
        }

        levering.setFaktiskLevering(LocalDateTime.now());
        return deliveryRepository.save(levering);
    }

    // Slet en levering
    public void deleteDelivery(Long leveringId) {
        getDeliveryById(leveringId); // Tjekker, om leveringen eksisterer
        deliveryRepository.deleteById(leveringId);
    }

    // Simuler oprettelse af levering
    public void simulateCreateDelivery(Long pizzaId, String adresse, int antal) {
        Pizza pizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new IllegalArgumentException("Pizza med ID " + pizzaId + " findes ikke."));

        for (int i = 0; i < antal; i++) {
            Levering levering = new Levering();
            levering.setAdresse(adresse);
            levering.setPizza(pizza);
            levering.setForventetLevering(LocalDateTime.now().plusMinutes(30));
            levering.setFaktiskLevering(null);
            levering.setDrone(null);

            deliveryRepository.save(levering);
        }
    }

    // Simuler afslutning af levering
    public void simulateFinishDelivery(Long leveringId) {
        Levering levering = deliveryRepository.findById(leveringId)
                .orElseThrow(() -> new IllegalArgumentException("Levering med ID " + leveringId + " findes ikke."));

        if (levering.getDrone() == null) {
            throw new IllegalArgumentException("Levering har ingen drone tildelt.");
        }

        levering.setFaktiskLevering(LocalDateTime.now());
        deliveryRepository.save(levering);
    }
}
