package com.example.klantenportaal;

import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.klantenportaal.dto.NotificationDTO;
import com.example.klantenportaal.dto.OrderDTO;
import com.example.klantenportaal.dto.OrderDetailDTO;
import com.example.klantenportaal.dto.ProductDetailDTO;
import com.example.klantenportaal.models.Notification;
import com.example.klantenportaal.models.RefreshToken;
import com.example.klantenportaal.models.User;

public class ObjectMother {
    public static NotificationDTO notification1() {
        return new NotificationDTO(Long.valueOf(1), "Notification1", "Departed", "2021-10-10T12:00:00", "Antwerp", "1",
                "1");
    }
    public static Notification notifications() {
        return new Notification("Notification1", "Departed", "2021-10-10T12:00:00", "Antwerp", "1",
                "1");
    }

    public static NotificationDTO notification2() {
        return new NotificationDTO(Long.valueOf(2), "Notification2", "Arrived", "2021-10-10T12:00:00", "Antwerp", "2",
                "2");
    }

    public static NotificationDTO notification3() {
        return new NotificationDTO(Long.valueOf(3), "Notification3", "dock", "2021-10-10T12:00:00", "Antwerp", "3",
                "3");
    }

    public static void setAuthentication(User user) {
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public static User user1() {
        return new User(
                "John Doe",
                "john.doe@example.com",
                "password123",
                "COMPANY123",
                "VAT123",
                "EORI123",
                "1234567890",
                "Doe Inc.",
                "Main Street",
                "123",
                "Anytown",
                "Any Country",
                User.Status.APPROVED,
                "user");
    }

    public static RefreshToken token1() {
        return new RefreshToken(
            "token",
            false,
            new RefreshToken(),
            new RefreshToken()
              );
    }

    public static User adminUser() {
        return new User(
                "admin",
                "admin@example.com",
                "password123",
                "Steelduxx",
                "VAT123",
                "EORI123",
                "1234567890",
                "Doe Inc.",
                "Main Street",
                "123",
                "Anytown",
                "Any Country",
                User.Status.APPROVED,
                "admin");
    }

    public static OrderDTO defaultOrder() {
        return new OrderDTO(
                "defaultCustomerCode",
                "defaultReferenceNumber",
                "defaultCustomerReferenceNumber",
                "defaultState",
                "defaultTransportType",
                "defaultPortOfOriginCode",
                "defaultPortOfOriginName",
                "defaultPortOfDestinationCode",
                "defaultPortOfDestinationName",
                "defaultShipName",
                "defaultEts",
                "defaultAts",
                "defaultEta",
                "defaultAta",
                "defaultCargoType",
                0,
                0,
                new String[0],
                "defaultShipMMSI");
    }
    public static OrderDTO sailingOrder() {
        return new OrderDTO(
                "defaultCustomerCode",
                "defaultReferenceNumber",
                "defaultCustomerReferenceNumber",
                "SAILING",
                "defaultTransportType",
                "defaultPortOfOriginCode",
                "defaultPortOfOriginName",
                "defaultPortOfDestinationCode",
                "defaultPortOfDestinationName",
                "defaultShipName",
                "defaultEts",
                "defaultAts",
                "defaultEta",
                "defaultAta",
                "defaultCargoType",
                0,
                0,
                new String[0],
                "defaultShipMMSI");
    }
    public static OrderDetailDTO createDefault() {
        return new OrderDetailDTO(
                "REF123456",
                "CUSTREF789",
                "SAILING",
                "SEA",
                "POR001",
                "Port of Origin",
                "POD001",
                "Port of Destination",
                "PORTCODE",
                "Ship Name",
                "IMO1234567",
                "MMSI7654321",
                "Cargo Ship",
                "2023-05-20T10:00:00Z",
                "2023-05-21T10:00:00Z",
                "2023-06-01T10:00:00Z",
                "2023-06-02T10:00:00Z",
                "Pre-Carriage Info",
                "2023-06-01T14:00:00Z",
                "2023-06-01T18:00:00Z",
                "http://example.com/billoflading.pdf",
                "http://example.com/packinglist.pdf",
                "http://example.com/customs.pdf",
                "General Cargo",
                createDefaultProducts()
        );
    }

    public static OrderDetailDTO createWithCustomReferenceNumber(String referenceNumber) {
        return new OrderDetailDTO(
                referenceNumber,
                "CUSTREF789",
                "SAILING",
                "SEA",
                "POR001",
                "Port of Origin",
                "POD001",
                "Port of Destination",
                "PORTCODE",
                "Ship Name",
                "IMO1234567",
                "MMSI7654321",
                "Cargo Ship",
                "2023-05-20T10:00:00Z",
                "2023-05-21T10:00:00Z",
                "2023-06-01T10:00:00Z",
                "2023-06-02T10:00:00Z",
                "Pre-Carriage Info",
                "2023-06-01T14:00:00Z",
                "2023-06-01T18:00:00Z",
                "http://example.com/billoflading.pdf",
                "http://example.com/packinglist.pdf",
                "http://example.com/customs.pdf",
                "General Cargo",
                createDefaultProducts()
        );
    }

    public static ProductDetailDTO[] createDefaultProducts() {
        ProductDetailDTO product1 = new ProductDetailDTO(
            "HS123456",
            "Default Product",
            10,
            1000L,
            "CONT123456",
            "40ft",
            "Standard"
        );

        ProductDetailDTO product2 = new ProductDetailDTO(
            "HS123458",
            "Unique Product",
            12,
            1010L,
            "CONT123457",
            "50ft",
            "Not so standard"
        );

        return new ProductDetailDTO[] { product1, product2 };
    }

}
