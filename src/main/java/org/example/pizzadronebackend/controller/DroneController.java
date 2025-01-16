package org.example.pizzadronebackend.controller;

import org.example.pizzadronebackend.dto.DroneDTO;
import org.example.pizzadronebackend.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/drones")
public class DroneController {

    @Autowired
    DroneService droneService;

    //injekt dependency af Drone service
    public DroneController(DroneService droneService){
        this.droneService = droneService;
    }


    //husk og lav en getmapping til drones endpointet.
    @GetMapping
    public List<DroneDTO> getAllDrones() {
        return droneService.getAllDrones();
    }

    @PostMapping("/add")
    public String addDrone() {
        try {
            droneService.addDrone();
            return "Drone tilføjet med succes!";
        } catch (IllegalArgumentException e) {
            return "Fejl: " + e.getMessage();
        }
    }

    // Skift drone-status til "i drift"
    @PostMapping("/enable")
    public String enableDrone(@RequestParam Long droneId) {
        try {
            droneService.enableDrone(droneId);
            return "Drone med ID " + droneId + " er nu i drift.";
        } catch (IllegalArgumentException e) {
            return "Fejl: " + e.getMessage();
        }
    }

    // Skift drone-status til "ude af drift"
    @PostMapping("/disable")
    public String disableDrone(@RequestParam Long droneId) {
        try {
            droneService.disableDrone(droneId);
            return "Drone med ID " + droneId + " er nu ude af drift.";
        } catch (IllegalArgumentException e) {
            return "Fejl: " + e.getMessage();
        }
    }

    // Skift drone-status til "udfaset"
    @PostMapping("/retire")
    public String retireDrone(@RequestParam Long droneId) {
        try {
            droneService.retireDrone(droneId);
            return "Drone med ID " + droneId + " er nu udfaset.";
        } catch (IllegalArgumentException e) {
            return "Fejl: " + e.getMessage();
        }
    }

}
