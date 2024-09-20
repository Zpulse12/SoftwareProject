package com.example.klantenportaal.services;

import com.example.klantenportaal.dto.OrderDTO;
import com.example.klantenportaal.dto.OrderDetailDTO;
import com.example.klantenportaal.exceptions.OrderCreationException;
import com.example.klantenportaal.exceptions.OrderRetrievalException;
import com.example.klantenportaal.models.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
/**
 * Service voor het beheren van orders.
*/
@Service
public class OrderService {

    private static final String BASE_URL = "https://sw11-1.devops-ap.be";
    private static final String ORDERS_URL = BASE_URL + "/order/all";
    private static final String BASE_DETAIL_URL = BASE_URL + "/order";
    private static final String ADMIN_ORDERS_URL = BASE_URL + "/admin/order/all";
    private static final String BASE_ADMIN_DETAIL_URL = BASE_URL + "/admin/order";
    private static final String CREATE_ORDER_URL = BASE_URL + "/order/new";

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final RestTemplate restTemplate;
    private final TokenManagerService tokenUpdater;
    /**
     * Construeert een OrderService met de opgegeven TokenManagerService.
     *
     * @param tokenUpdater de service voor het beheren van tokens
    */
    public OrderService(TokenManagerService tokenUpdater) {
        this.restTemplate = new RestTemplate();
        this.tokenUpdater = tokenUpdater;
    }
    /**
     * Haalt alle orders op.
     *
     * @param user de gebruiker die de orders opvraagt
     * @return een lijst van alle orders
     * @throws OrderRetrievalException als het ophalen van de orders mislukt
    */
    public List<OrderDTO> getAllOrders(User user) throws OrderRetrievalException {
        try {
            String url = "";
            HttpHeaders headers = new HttpHeaders();
            if (user.getRole().contains("admin")) {
                headers.set(AUTHORIZATION_HEADER, tokenUpdater.getBearerToken("ADMIN"));
                url = ADMIN_ORDERS_URL;
            } else {
                headers.set(AUTHORIZATION_HEADER, tokenUpdater.getBearerToken(user.getCompanyCode()));
                url = ORDERS_URL;
            }
            ResponseEntity<List<OrderDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<List<OrderDTO>>() {
                    });

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new OrderRetrievalException(
                        "Failed to retrieve orders. HTTP Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new OrderRetrievalException("Failed to retrieve orders.", e);
        }
    }
    /**
     * Haalt een order op op basis van het referentienummer.
     *
     * @param ref het referentienummer van de order
     * @param companyCode het bedrijfsnummer van de gebruiker
     * @param user de gebruiker die de order opvraagt
     * @return de order met het opgegeven referentienummer
     * @throws OrderRetrievalException als het ophalen van de order mislukt
    */
    public OrderDetailDTO getOrderByRef(String ref, String companyCode, User user) throws OrderRetrievalException {
        try {
            String url = "";
            HttpHeaders headers = new HttpHeaders();
            if (user.getRole().contains("admin")) {
                headers.set(AUTHORIZATION_HEADER, tokenUpdater.getBearerToken("ADMIN"));
                url = BASE_ADMIN_DETAIL_URL + "/" + companyCode + "/" + ref;
            } else {
                headers.set(AUTHORIZATION_HEADER, tokenUpdater.getBearerToken(user.getCompanyCode()));
                url = BASE_DETAIL_URL + "/" + ref;
            }
            ResponseEntity<OrderDetailDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    OrderDetailDTO.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new OrderRetrievalException(
                        "Failed to retrieve order. HTTP Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new OrderRetrievalException("Failed to retrieve order.", e);
        }
    }
    /**
     * Maakt een nieuwe order aan.
     *
     * @param order het dto met de gegevens van de order
     * @param user de gebruiker die de order aanmaakt
     * @return de aangemaakte order
     * @throws OrderCreationException als het aanmaken van de order mislukt
    */
    public OrderDetailDTO createNewOrder(OrderDetailDTO order, User user) throws OrderCreationException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(AUTHORIZATION_HEADER, tokenUpdater.getBearerToken(user.getCompanyCode()));

            ResponseEntity<OrderDetailDTO> response = restTemplate.postForEntity(CREATE_ORDER_URL,
                    new HttpEntity<>(order, headers), OrderDetailDTO.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new OrderCreationException(
                        "Failed to create order. HTTP Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new OrderCreationException("Failed to create order.", e);
        }
    }
}