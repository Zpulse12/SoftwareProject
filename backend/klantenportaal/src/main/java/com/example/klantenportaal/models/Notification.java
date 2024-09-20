package com.example.klantenportaal.models;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "ship_name")
    private String shipName;
    @Column(name = "departure_status")
    private String departureStatus;
    @Column(name = "departure_time")
    private String departureTime;
    @Column(name = "destination")
    private String destination;
    @Column(name = "reference_number")
    private String referenceNumber;
    @Column(name = "company_code")
    private String companyCode;

    public Notification(String shipName, String departureStatus, String departureTime, String destination, String referenceNumber, String companyCode) {
        this.shipName = shipName;
        this.departureStatus = departureStatus;
        this.departureTime = departureTime;
        this.destination = destination;
        this.referenceNumber = referenceNumber;
        this.companyCode = companyCode;
    }
}