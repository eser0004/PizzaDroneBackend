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
                        drone.getSerialUuid(),
                        drone.getDriftsstatus(),
                        drone.getStation().getLatitude(),
                        drone.getStation().getLongitude()
                ))
                .collect(Collectors.toList());
    }

    // Tilføj en ny drone og tilknyt den til en station
    @Transactional
    public void addDrone() {
        Station station = stationRepository.findAll().stream()
                .min(Comparator.comparingInt(s -> s.getDroner().size()))
                .orElseThrow(() -> new IllegalArgumentException("Ingen stationer fundet."));

        // Generer en tilfældig UUID
        String randomUuid = UUID.randomUUID().toString();

        // Opret en ny drone
        Drone newDrone = new Drone(randomUuid, "i drift", station);

        // Tilføj dronen til stationens liste
        station.getDroner().add(newDrone);

        // Gem både dronen og stationen eksplicit
        droneRepository.save(newDrone);
        stationRepository.save(station);
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