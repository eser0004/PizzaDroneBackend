package org.example.pizzadronebackend.service;

import jakarta.transaction.Transactional;
import org.example.pizzadronebackend.model.Drone;
import org.example.pizzadronebackend.model.Station;
import org.example.pizzadronebackend.repository.DroneRepository;
import org.example.pizzadronebackend.repository.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DroneServiceTest {

    @Autowired
    private DroneService droneService;

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        // Rens databasen før hver test
        droneRepository.deleteAll();
        stationRepository.deleteAll();

        // Initialiser nogle stationer
        Station station1 = new Station(55.41, 12.34);
        Station station2 = new Station(55.42, 12.33);
        stationRepository.save(station1);
        stationRepository.save(station2);
    }

    @Test
    void testGetAllDrones() {
        // Arrange
        Station station = stationRepository.findAll().get(0);
        Drone drone1 = new Drone("UUID-1234", "i drift", station);
        Drone drone2 = new Drone("UUID-5678", "ude af drift", station);
        droneRepository.save(drone1);
        droneRepository.save(drone2);

        // Act
        var drones = droneService.getAllDrones();

        // Assert
        assertEquals(2, drones.size());
        assertEquals("UUID-1234", drones.get(0).getSerialUuid());
    }

    @Test
    void testAddDrone() {
        // Arrange
        Station station1 = new Station(55.41, 12.34);
        station1.setStationId(1L);
        station1.setDroner(new ArrayList<>()); // Initialiser droner

        Station station2 = new Station(55.42, 12.33);
        station2.setStationId(2L);
        station2.setDroner(new ArrayList<>());

        stationRepository.save(station1);
        stationRepository.save(station2);

        // Act
        droneService.addDrone();

        // Assert
        List<Drone> drones = droneRepository.findAll();
        assertEquals(1, drones.size());
        assertEquals("i drift", drones.get(0).getDriftsstatus());
    }

    @Test
    void testAddDroneNoStations() {
        // Rens stationer
        stationRepository.deleteAll();

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> droneService.addDrone());
        assertEquals("Ingen stationer fundet.", exception.getMessage());
    }

    @Test
    void testChangeDroneStatus() {
        // Arrange
        Station station = stationRepository.findAll().get(0);
        Drone drone = new Drone("UUID-1234", "ude af drift", station);
        droneRepository.save(drone);

        // Act
        droneService.changeDroneStatus(drone.getDroneId(), "i drift");

        // Assert
        Optional<Drone> updatedDrone = droneRepository.findById(drone.getDroneId());
        assertTrue(updatedDrone.isPresent());
        assertEquals("i drift", updatedDrone.get().getDriftsstatus());
    }
}
