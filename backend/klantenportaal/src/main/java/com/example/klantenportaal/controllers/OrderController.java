package com.example.klantenportaal.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;

import com.example.klantenportaal.dto.OrderDTO;
import com.example.klantenportaal.dto.OrderDetailDTO;
import com.example.klantenportaal.exceptions.OrderCreationException;
import com.example.klantenportaal.exceptions.OrderRetrievalException;
import com.example.klantenportaal.models.User;
import com.example.klantenportaal.services.OrderService;
import java.util.List;
/**
 * Controller voor het beheren van orders.
 */
@RestController
@RequestMapping("api/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    /**
     * Construeert een OrderController met de opgegeven OrderService.
     *
     * @param orderService de service die wordt gebruikt voor het beheren van orders
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
     /**
     * Haalt alle orders op voor de geauthenticeerde gebruiker.
     *
     * @param authentication het authenticatietoken van de huidige gebruiker
     * @return een ResponseEntity met een lijst van orders
     * @throws OrderRetrievalException als er een fout optreedt bij het ophalen van de orders
     */
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders(Authentication authentication) throws OrderRetrievalException {
        return new ResponseEntity<>(orderService.getAllOrders((User) authentication.getPrincipal()), HttpStatus.OK);
    }
        /**
     * Maakt een nieuwe order aan.
     *
     * @param order het dto met de details van de order
     * @param authentication het authenticatietoken van de huidige gebruiker
     * @return een ResponseEntity met de aangemaakte order
     * @throws OrderCreationException als er een fout optreedt bij het aanmaken van de order
     */
    @PostMapping
    public ResponseEntity<OrderDetailDTO> createOrder(@RequestBody OrderDetailDTO order,
            Authentication authentication) throws OrderCreationException {
        logger.info("Creating new order {}", order);
        return new ResponseEntity<>(orderService.createNewOrder(order, (User) authentication.getPrincipal()),
                    HttpStatus.OK);

    }
        /**
     * Haalt een order op basis van het bedrijfsnummer en referentienummer.
     *
     * @param companyCode het bedrijfsnummer van de order
     * @param ref het referentienummer van de order
     * @param authentication het authenticatietoken van de huidige gebruiker
     * @return een ResponseEntity met de order
     * @throws OrderRetrievalException als er een fout optreedt bij het ophalen van de order
     */
    @GetMapping("/{companyCode}/{ref}")
    public ResponseEntity<OrderDetailDTO> getOrderByRef(@PathVariable String companyCode, @PathVariable String ref,
            Authentication authentication) throws OrderRetrievalException {
        return new ResponseEntity<>(
                orderService.getOrderByRef(ref, companyCode, (User) authentication.getPrincipal()), HttpStatus.OK);
    }
}