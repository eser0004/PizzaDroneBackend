package org.example.pizzadronebackend.service;

import org.example.pizzadronebackend.dto.StationDTO;
import org.example.pizzadronebackend.model.Drone;
import org.example.pizzadronebackend.model.Station;
import org.example.pizzadronebackend.repository.DroneRepository;
import org.example.pizzadronebackend.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StationService {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private DroneRepository droneRepository;

    public List<StationDTO> getAllStations() {
        return stationRepository.findAll().stream()
                .map(StationDTO::new) // Map hver Station til en StationDTO
                .collect(Collectors.toList());
    }


    // Opdater en stations koordinater
    public Station updateStation(Long stationId, double latitude, double longitude) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Station med ID " + stationId + " findes ikke."));
        station.setLatitude(latitude);
        station.setLongitude(longitude);
        return stationRepository.save(station);
    }

    // Slet en station
    public void deleteStation(Long stationId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Station med ID " + stationId + " findes ikke."));
        if (!station.getDroner().isEmpty()) {
            throw new IllegalArgumentException("Stationen har droner tildelt og kan ikke slettes.");
        }
        stationRepository.delete(station);
    }
    public void assignDroneToStation(Long stationId, Long droneId) {
        System.out.println("Tildeling af drone. Station ID: " + stationId + ", Drone ID: " + droneId);

        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Station med ID " + stationId + " findes ikke."));
        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new IllegalArgumentException("Drone med ID " + droneId + " findes ikke."));

        if (drone.getStation() != null) {
            throw new IllegalArgumentException("Drone med ID " + droneId + " er allerede tildelt en station.");
        }

        System.out.println("Drone og station fundet. Tildeler nu...");
        drone.setStation(station);
        droneRepository.save(drone);
        System.out.println("Drone tildelt succesfuldt!");
    }



}
