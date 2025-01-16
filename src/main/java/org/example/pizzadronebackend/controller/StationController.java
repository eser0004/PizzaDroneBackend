package org.example.pizzadronebackend.controller;

import org.example.pizzadronebackend.dto.StationDTO;
import org.example.pizzadronebackend.model.Station;

import org.example.pizzadronebackend.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/stations")
@CrossOrigin(origins = "*")
public class StationController {

    @Autowired
    private StationService stationService;

    // Hent alle stationer
    @GetMapping
    public ResponseEntity<List<StationDTO>> getAllStations() {
        try {
            List<StationDTO> stations = stationService.getAllStations();
            return ResponseEntity.ok(stations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Returner null ved fejl
        }
    }

    // Opdater en station
    @PostMapping("/update")
    public ResponseEntity<?> updateStation(@RequestBody Map<String, Object> payload) {
        try {
            Long stationId = Long.parseLong(payload.get("stationId").toString());
            double latitude = Double.parseDouble(payload.get("latitude").toString());
            double longitude = Double.parseDouble(payload.get("longitude").toString());
            Station updatedStation = stationService.updateStation(stationId, latitude, longitude);
            return ResponseEntity.ok(updatedStation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fejl under opdatering af station.");
        }
    }
    @PostMapping("/{stationId}/assign-drone")
    public ResponseEntity<String> assignDroneToStation(@PathVariable Long stationId, @RequestParam Long droneId) {
        try {
            stationService.assignDroneToStation(stationId, droneId);
            return ResponseEntity.ok("Drone med ID " + droneId + " er blevet tildelt station med ID " + stationId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




    // Slet en station
    @DeleteMapping("/{stationId}")
    public ResponseEntity<?> deleteStation(@PathVariable Long stationId) {
        try {
            stationService.deleteStation(stationId);
            return ResponseEntity.ok("Station med ID " + stationId + " er slettet.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

