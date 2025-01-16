package org.example.pizzadronebackend.service;


import jakarta.transaction.Transactional;
import org.example.pizzadronebackend.dto.DroneDTO;
import org.example.pizzadronebackend.model.Drone;
import org.example.pizzadronebackend.model.Station;
import org.example.pizzadronebackend.repository.DroneRepository;
import org.example.pizzadronebackend.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DroneService {

    @Autowired
    DroneRepository droneRepository;

    @Autowired
    StationRepository stationRepository;

    public DroneService(DroneRepository droneRepository, StationRepository stationRepository) {
        this.droneRepository = droneRepository;
        this.stationRepository = stationRepository;
    }

    // Hent alle droner
    public List<DroneDTO> getAllDrones() {
        List<Drone> droner = droneRepository.findAll();

        // Konverter hver Drone til en DroneDTO og returner som liste
        return droner.stream()
                .map(drone -> new DroneDTO(
                        drone.getDroneId(),
                        drone.getSerialUuid(),
                        drone.getDriftsstatus(),
                        drone.getStation().getLatitude(),
                        drone.getStation().getLongitude()
                ))
                .collect(Collectors.toList());
    }

    // Tilføj en ny drone og tilknyt den til en station
    @Transactional
    public Drone addDrone() {
        Station stationMedFaerrestDroner = stationRepository.findAll()
                .stream()
                .min(Comparator.comparing(station -> station.getDroner().size()))
                .orElseThrow(() -> new IllegalStateException("Ingen stationer tilgængelige"));

        // Find det laveste ledige ID
        Long lowestAvailableId = findLowestAvailableId();

        // Generer UUID i det ønskede format
        String customUUID = generateCustomUUID();

        Drone newDrone = new Drone(customUUID, "i drift", stationMedFaerrestDroner);
        newDrone.setDroneId(lowestAvailableId); // Sæt det laveste ledige ID
        return droneRepository.save(newDrone);
    }

    private Long findLowestAvailableId() {
        List<Long> existingIds = droneRepository.findAll()
                .stream()
                .map(Drone::getDroneId)
                .sorted()
                .toList();

        Long nextId = 1L;
        for (Long id : existingIds) {
            if (!id.equals(nextId)) {
                break;
            }
            nextId++;
        }

        // Valider, at ID'et ikke allerede findes i databasen
        if (droneRepository.existsById(nextId)) {
            throw new IllegalStateException("Det foreslåede ID " + nextId + " findes allerede.");
        }

        return nextId;
    }


    private String generateCustomUUID() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000; // Generer et tal mellem 1000 og 9999
        return "UUID-" + randomNumber;
    }
    public void deleteDrone(Long droneId) {
        if (!droneRepository.existsById(droneId)) {
            throw new IllegalArgumentException("Drone med ID " + droneId + " findes ikke.");
        }
        droneRepository.deleteById(droneId);
    }


    public Drone updateDroneStatus(Long droneId, String newStatus) {
        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new IllegalArgumentException("Drone med ID " + droneId + " findes ikke."));

        if (!List.of("i drift", "ude af drift", "udfaset").contains(newStatus)) {
            throw new IllegalArgumentException("Ugyldig status: " + newStatus);
        }

        drone.setDriftsstatus(newStatus);
        return droneRepository.save(drone);
    }





    // Ændre dronens status
    public void changeDroneStatus(Long droneId, String newStatus) {
        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new IllegalArgumentException("Drone med ID " + droneId + " findes ikke."));

        drone.setDriftsstatus(newStatus);
        droneRepository.save(drone);
    }

    public void enableDrone(Long droneId) {
        changeDroneStatus(droneId, "i drift");
    }

    public void disableDrone(Long droneId) {
        changeDroneStatus(droneId, "ude af drift");
    }

    public void retireDrone(Long droneId) {
        changeDroneStatus(droneId, "udfaset");
    }


}