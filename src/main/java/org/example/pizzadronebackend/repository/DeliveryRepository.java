package org.example.pizzadronebackend.repository;

import org.example.pizzadronebackend.model.Levering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Levering, Long> {
    List<Levering> findByFaktiskLeveringIsNull();
    List<Levering> findByDroneIsNull();
}
