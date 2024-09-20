package com.example.klantenportaal.service;
import com.example.klantenportaal.dto.OrderDTO;
import com.example.klantenportaal.dto.OrderDetailDTO;
import com.example.klantenportaal.exceptions.OrderCreationException;
import com.example.klantenportaal.exceptions.OrderRetrievalException;
import com.example.klantenportaal.exceptions.TokenRetrievalException;
import com.example.klantenportaal.models.User;
import com.example.klantenportaal.services.OrderService;
import com.example.klantenportaal.services.TokenManagerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static com.example.klantenportaal.ObjectMother.createDefault;
import static com.example.klantenportaal.ObjectMother.defaultOrder;
import static com.example.klantenportaal.ObjectMother.user1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TokenManagerService tokenUpdater;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private OrderDTO orderDTO;
    private OrderDetailDTO orderDetailDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = user1();
        orderDTO = defaultOrder();
        orderDetailDTO = createDefault();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllOrdersForUser() throws OrderRetrievalException, TokenRetrievalException {
        String token = "Bearer 742c5599-0f51-4b22-946b-4494e2921d41";
        when(tokenUpdater.getBearerToken(user.getCompanyCode())).thenReturn(token);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        ResponseEntity<List<OrderDTO>> responseEntity = new ResponseEntity<>(
                Collections.singletonList(orderDTO), HttpStatus.OK);

        when(restTemplate.exchange(
                eq("https://sw11-1.devops-ap.be/order/all"),
                eq(HttpMethod.GET),
                eq(new HttpEntity<>(headers)),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        List<OrderDTO> orders = orderService.getAllOrders(user);
        assertEquals(1, orders.size());
        assertEquals(orderDTO, orders.get(0));
    }

    @Test
    public void testGetOrderByRefForUser() throws OrderRetrievalException, TokenRetrievalException {
        String ref = "referenceNumber";
        String token = "Bearer testToken";
        when(tokenUpdater.getBearerToken(user.getCompanyCode())).thenReturn(token);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        ResponseEntity<OrderDetailDTO> responseEntity = new ResponseEntity<>(orderDetailDTO, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("https://sw11-1.devops-ap.be/order/" + ref),
                eq(HttpMethod.GET),
                eq(new HttpEntity<>(headers)),
                eq(OrderDetailDTO.class)
        )).thenReturn(responseEntity);

        OrderDetailDTO result = orderService.getOrderByRef(ref, user.getCompanyCode(), user);

        assertEquals(orderDetailDTO, result);
    }

    @Test
    public void testCreateNewOrder() throws OrderCreationException, TokenRetrievalException {
        String token = "Bearer testToken";
        when(tokenUpdater.getBearerToken(user.getCompanyCode())).thenReturn(token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        ResponseEntity<OrderDetailDTO> responseEntity = new ResponseEntity<>(orderDetailDTO, HttpStatus.OK);

        when(restTemplate.postForEntity(
                eq("https://sw11-1.devops-ap.be/order/new"),
                eq(new HttpEntity<>(orderDetailDTO, headers)),
                eq(OrderDetailDTO.class)
        )).thenReturn(responseEntity);

        OrderDetailDTO result = orderService.createNewOrder(orderDetailDTO, user);

        assertEquals(orderDetailDTO, result);
    }
}
