package org.example.pizzadronebackend.controller;

import org.example.pizzadronebackend.dto.DroneDTO;
import org.example.pizzadronebackend.model.Drone;
import org.example.pizzadronebackend.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/drones")
public class DroneController {

    @Autowired
    DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    // Hent alle droner
    @GetMapping
    public List<Map<String, Object>> getAllDrones() {
        List<DroneDTO> droner = droneService.getAllDrones();
        List<Map<String, Object>> response = new ArrayList<>();

        for (DroneDTO drone : droner) {
            Map<String, Object> droneInfo = new HashMap<>();
            droneInfo.put("droneId", drone.getDroneId());
            droneInfo.put("uuid", drone.getSerialUuid());
            droneInfo.put("status", drone.getDriftsstatus());
            response.add(droneInfo);
        }

        return response;
    }

    // Tilføj ny drone
    @PostMapping("/add")
    public ResponseEntity<?> addDrone() {
        try {
            Drone newDrone = droneService.addDrone();
            return ResponseEntity.ok(newDrone);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Fejl: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("En uventet fejl opstod.");
        }
    }


    @DeleteMapping("/{droneId}")
    public ResponseEntity<?> deleteDrone(@PathVariable Long droneId) {
        try {
            droneService.deleteDrone(droneId);
            return ResponseEntity.ok("Drone med ID " + droneId + " er slettet.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("/update")
    public ResponseEntity<?> updateDroneStatus(@RequestBody Map<String, String> request) {
        try {
            Long droneId = Long.parseLong(request.get("droneId"));
            String newStatus = request.get("status");
            Drone updatedDrone = droneService.updateDroneStatus(droneId, newStatus);
            return ResponseEntity.ok(updatedDrone);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    // Skift drone-status til "i drift"
    @PostMapping("/enable")
    public ResponseEntity<String> enableDrone(@RequestParam Long droneId) {
        try {
            droneService.enableDrone(droneId);
            return ResponseEntity.ok("Drone med ID " + droneId + " er nu i drift.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Fejl: " + e.getMessage());
        }
    }

    // Skift drone-status til "ude af drift"
    @PostMapping("/disable")
    public ResponseEntity<String> disableDrone(@RequestParam Long droneId) {
        try {
            droneService.disableDrone(droneId);
            return ResponseEntity.ok("Drone med ID " + droneId + " er nu ude af drift.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Fejl: " + e.getMessage());
        }
    }

    // Skift drone-status til "udfaset"
    @PostMapping("/retire")
    public ResponseEntity<String> retireDrone(@RequestParam Long droneId) {
        try {
            droneService.retireDrone(droneId);
            return ResponseEntity.ok("Drone med ID " + droneId + " er nu udfaset.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Fejl: " + e.getMessage());
        }
    }

    // Hent detaljerede droneoplysninger
    @GetMapping("/details")
    public List<Map<String, Object>> getAllDroneDetails() {
        List<DroneDTO> droner = droneService.getAllDrones();
        List<Map<String, Object>> response = new ArrayList<>();

        for (DroneDTO drone : droner) {
            Map<String, Object> droneInfo = new HashMap<>();
            droneInfo.put("droneId", drone.getDroneId());
            droneInfo.put("uuid", drone.getSerialUuid());
            droneInfo.put("status", drone.getDriftsstatus());
            response.add(droneInfo);
        }

        return response;
    }
}
