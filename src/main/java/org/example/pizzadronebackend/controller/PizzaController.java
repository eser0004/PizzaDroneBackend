package org.example.pizzadronebackend.controller;

import org.example.pizzadronebackend.model.Pizza;
import org.example.pizzadronebackend.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/pizzas")
@CrossOrigin(origins = "*")
public class PizzaController {

    @Autowired
    private PizzaService pizzaService;

    // Hent alle pizzaer
    @GetMapping
    public List<Pizza> getAllPizzas() {
        return pizzaService.getAllPizzas();
    }

    // Tilføj en ny pizza
    @PostMapping("/add")
    public ResponseEntity<?> addPizza(@RequestBody Map<String, Object> payload) {
        try {
            String title = (String) payload.get("title");
            int price = (int) payload.get("price");
            Pizza pizza = pizzaService.addPizza(title, price);
            return ResponseEntity.ok(pizza);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fejl under oprettelse af pizza.");
        }
    }

    // Opdater en pizza
    @PostMapping("/update")
    public ResponseEntity<?> updatePizza(@RequestBody Map<String, Object> payload) {
        try {
            Long pizzaId = Long.parseLong(payload.get("pizzaId").toString());
            String newTitle = (String) payload.get("title");
            int newPrice = (int) payload.get("price");
            Pizza updatedPizza = pizzaService.updatePizza(pizzaId, newTitle, newPrice);
            return ResponseEntity.ok(updatedPizza);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fejl under opdatering af pizza.");
        }
    }

    // Slet en pizza
    @DeleteMapping("/{pizzaId}")
    public ResponseEntity<?> deletePizza(@PathVariable Long pizzaId) {
        try {
            pizzaService.deletePizza(pizzaId);
            return ResponseEntity.ok("Pizza med ID " + pizzaId + " er slettet.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
