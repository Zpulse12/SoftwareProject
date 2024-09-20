package com.example.klantenportaal.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.example.klantenportaal.ObjectMother.defaultOrder;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import com.example.klantenportaal.controllers.OrderController;
import com.example.klantenportaal.dto.OrderDTO;
import com.example.klantenportaal.exceptions.OrderRetrievalException;
import com.example.klantenportaal.models.User;
import com.example.klantenportaal.seeders.UserSeeder;
import com.example.klantenportaal.services.JwtService;
import com.example.klantenportaal.services.OrderService;

@WebMvcTest(OrderController.class)
class AdminTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;
    @MockBean
    private UserSeeder userSeeder;
    @MockBean
    private JwtService jwtService;

    @BeforeEach
    public void setup() throws OrderRetrievalException {
        OrderDTO mockOrder1 = defaultOrder();
        OrderDTO mockOrder2 = defaultOrder();
        User user = new User();
        List<OrderDTO> orders = Arrays.asList(mockOrder1, mockOrder2);

        given(orderService.getAllOrders(user)).willReturn(orders);
    }

    @Test
    void testGetAllOrders() throws Exception {
        User user = new User();
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList()));
        SecurityContextHolder.setContext(securityContext);
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }
}