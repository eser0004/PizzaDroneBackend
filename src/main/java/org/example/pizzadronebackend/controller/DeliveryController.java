package org.example.pizzadronebackend.controller;

import org.example.pizzadronebackend.dto.DeliveryRequest;
import org.example.pizzadronebackend.model.Levering;
import org.example.pizzadronebackend.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/deliveries")
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    // Hent alle ikke-leverede leveringer
    @GetMapping("/detailed")
    public List<Map<String, Object>> getDetailedPendingDeliveries() {
        List<Levering> leveringer = deliveryService.getPendingDeliveries();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Levering levering : leveringer) {
            Map<String, Object> deliveryInfo = new HashMap<>();
            deliveryInfo.put("leveringId", levering.getLeveringId());
            deliveryInfo.put("adresse", levering.getAdresse());
            deliveryInfo.put("status", levering.getDrone() == null ? "Mangler drone" : "Under levering");

            if (levering.getForventetLevering() != null && levering.getDrone() != null) {
                LocalDateTime now = LocalDateTime.now();
                Duration duration = Duration.between(now, levering.getForventetLevering());
                deliveryInfo.put("tidTilLevering", duration.isNegative() ? "Leveret" : duration.toMinutes() + " minutter");
            } else {
                deliveryInfo.put("tidTilLevering", "Ikke startet");
            }

            response.add(deliveryInfo);
        }

        return response;
    }


    @GetMapping
    public List<Levering> getPendingDeliveries() {
        return deliveryService.getPendingDeliveries();
    }

    // Opret en ny levering
    @PostMapping("/add")
    public Levering addDelivery(@RequestBody DeliveryRequest request) {
        return deliveryService.addDelivery(request.getAdresse(), request.getPizzaId(), request.getQuantity());
    }



    // Hent leveringer uden drone
    @GetMapping("/queue")
    public List<Levering> getQueuedDeliveries() {
        return deliveryService.getQueuedDeliveries();
    }

    // Tildel en drone til en levering
    @PostMapping("/schedule")
    public Levering scheduleDelivery(@RequestParam Long leveringId, @RequestParam Long droneId) {
        return deliveryService.scheduleDelivery(leveringId, droneId);
    }


    // Markér en levering som afsluttet
    @PostMapping("/finish")
    public Levering finishDelivery(@RequestParam Long leveringId) {
        return deliveryService.finishDelivery(leveringId);
    }

    // Hent status for en specifik levering
    @GetMapping("/{leveringId}/status")
    public Map<String, Object> getDeliveryStatus(@PathVariable Long leveringId) {
        Levering levering = deliveryService.getDeliveryById(leveringId);

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

    // Slet en levering
    @DeleteMapping("/{leveringId}")
    public void deleteDelivery(@PathVariable Long leveringId) {
        deliveryService.deleteDelivery(leveringId);
    }

    // Simuler oprettelse af levering
    @PostMapping("/simulate-create")
    public ResponseEntity<String> simulateCreateDelivery(@RequestParam Long pizzaId, @RequestParam String adresse, @RequestParam int antal) {
        try {
            deliveryService.simulateCreateDelivery(pizzaId, adresse, antal);
            return ResponseEntity.ok("Levering oprettet succesfuldt!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Simuler afslutning af levering
    @PostMapping("/simulate-finish")
    public ResponseEntity<String> simulateFinishDelivery(@RequestParam Long leveringId) {
        try {
            deliveryService.simulateFinishDelivery(leveringId);
            return ResponseEntity.ok("Levering afsluttet succesfuldt!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
