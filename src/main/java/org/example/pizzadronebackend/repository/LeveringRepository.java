package org.example.pizzadronebackend.repository;

import org.example.pizzadronebackend.model.Levering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeveringRepository extends JpaRepository<Levering, Long> {
}
