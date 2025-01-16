package org.example.pizzadronebackend.service;

import org.example.pizzadronebackend.model.Pizza;
import org.example.pizzadronebackend.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PizzaService {

    @Autowired
    private PizzaRepository pizzaRepository;

    // Hent alle pizzaer
    public List<Pizza> getAllPizzas() {
        return pizzaRepository.findAll();
    }

    // Tilføj en ny pizza
    public Pizza addPizza(String title, int price) {
        Pizza pizza = new Pizza();
        pizza.setTitel(title);
        pizza.setPris(price);
        return pizzaRepository.save(pizza);
    }

    // Opdater en pizza
    public Pizza updatePizza(Long pizzaId, String newTitle, int newPrice) {
        Pizza pizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new IllegalArgumentException("Pizza med ID " + pizzaId + " findes ikke."));
        pizza.setTitel(newTitle);
        pizza.setPris(newPrice);
        return pizzaRepository.save(pizza);
    }

    // Slet en pizza
    public void deletePizza(Long pizzaId) {
        if (!pizzaRepository.existsById(pizzaId)) {
            throw new IllegalArgumentException("Pizza med ID " + pizzaId + " findes ikke.");
        }
        pizzaRepository.deleteById(pizzaId);
    }
}
