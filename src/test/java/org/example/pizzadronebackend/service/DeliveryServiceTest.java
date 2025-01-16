package org.example.pizzadronebackend.service;

import jakarta.transaction.Transactional;
import org.example.pizzadronebackend.model.Drone;
import org.example.pizzadronebackend.model.Levering;
import org.example.pizzadronebackend.model.Pizza;
import org.example.pizzadronebackend.repository.DeliveryRepository;
import org.example.pizzadronebackend.repository.DroneRepository;
import org.example.pizzadronebackend.repository.PizzaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DeliveryServiceTest {

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private DroneRepository droneRepository;

    @BeforeEach
    void setUp() {
        // Rens databasen før hver test
        deliveryRepository.deleteAll();
        pizzaRepository.deleteAll();
        droneRepository.deleteAll();

        // Opret testdata
        Pizza pizza1 = new Pizza();
        pizza1.setTitel("Margherita");
        pizza1.setPris(60);

        Pizza pizza2 = new Pizza();
        pizza2.setTitel("Pepperoni");
        pizza2.setPris(75);

        pizzaRepository.save(pizza1);
        pizzaRepository.save(pizza2);
    }

    @Test
    void testAddDelivery() {
        // Arrange
        Pizza pizza = pizzaRepository.findAll().get(0);

        // Act
        Levering levering = deliveryService.addDelivery(pizza.getPizzaId(), "Testvej 123");

        // Assert
        assertNotNull(levering);
        assertEquals("Testvej 123", levering.getAdresse());
        assertEquals(pizza, levering.getPizza());
        assertNotNull(levering.getForventetLevering());
        assertNull(levering.getFaktiskLevering());
    }

    @Test
    void testAddDeliveryPizzaNotFound() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            deliveryService.addDelivery(999L, "Testvej 123");
        });

        assertEquals("Pizza med ID 999 findes ikke.", exception.getMessage());
    }

    @Test
    void testFinishDelivery() {
        // Arrange
        Pizza pizza = pizzaRepository.findAll().get(0);

        Levering levering = new Levering();
        levering.setAdresse("Testvej 123");
        levering.setForventetLevering(LocalDateTime.now().plusMinutes(30));
        levering.setPizza(pizza);

        Drone drone = new Drone();
        drone.setSerialUuid("UUID-1234");
        drone.setDriftsstatus("i drift");

        droneRepository.save(drone);
        levering.setDrone(drone);
        deliveryRepository.save(levering);

        // Act
        Levering finishedLevering = deliveryService.finishDelivery(levering.getLeveringId());

        // Assert
        assertNotNull(finishedLevering.getFaktiskLevering());
        assertEquals(drone, finishedLevering.getDrone());
    }

    @Test
    void testFinishDeliveryNoDrone() {
        // Arrange
        Pizza pizza = pizzaRepository.findAll().get(0);

        Levering levering = new Levering();
        levering.setAdresse("Testvej 123");
        levering.setForventetLevering(LocalDateTime.now().plusMinutes(30));
        levering.setPizza(pizza);

        deliveryRepository.save(levering);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            deliveryService.finishDelivery(levering.getLeveringId());
        });

        assertEquals("Leveringen har ingen drone tildelt.", exception.getMessage());
    }
}
