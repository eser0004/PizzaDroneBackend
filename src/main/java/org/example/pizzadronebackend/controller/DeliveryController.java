package org.example.pizzadronebackend.controller;

import org.example.pizzadronebackend.model.Levering;
import org.example.pizzadronebackend.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/deliveries")
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService){
        this.deliveryService = deliveryService;
    }


    // hent alle ikke-leverede leveringer
    @GetMapping
    public List<Levering> getPendingDeliveries() {
        return deliveryService.getPendingDeliveries();
    }
    // Opret en ny levering
    @PostMapping("/add")
    public Levering addDelivery(@RequestParam Long pizzaId, @RequestParam String adresse) {
        return deliveryService.addDelivery(pizzaId, adresse);
    }

    // Hent leveringer uden drone
    @GetMapping("/queue")
    public List<Levering> getQueuedDeliveries() {
        return deliveryService.getQueuedDeliveries();
    }
    // Tildel en drone til en levering
    @PostMapping("/schedule")
    public Levering scheduleDelivery(@RequestParam Long leveringId, @RequestParam(required = false) Long droneId) {
        return deliveryService.scheduleDelivery(leveringId, droneId);
    }

    // Markér en levering som afsluttet
    @PostMapping("/finish")
    public Levering finishDelivery(@RequestParam Long leveringId) {
        return deliveryService.finishDelivery(leveringId);
    }
}
