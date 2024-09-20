package com.example.klantenportaal.dto;

import java.util.Arrays;
import java.util.Objects;

public record OrderDTO(
        String customerCode,
        String referenceNumber,
        String customerReferenceNumber,
        String state,
        String transportType,
        String portOfOriginCode,
        String portOfOriginName,
        String portOfDestinationCode,
        String portOfDestinationName,
        String shipName,
        String ets,
        String ats,
        String eta,
        String ata,
        String cargoType,
        int totalWeight,
        int totalContainers,
        String[] containerTypes,
        String shipMMSI) {

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return totalWeight == orderDTO.totalWeight &&
                totalContainers == orderDTO.totalContainers &&
                Objects.equals(customerCode, orderDTO.customerCode) &&
                Objects.equals(referenceNumber, orderDTO.referenceNumber) &&
                Objects.equals(customerReferenceNumber, orderDTO.customerReferenceNumber) &&
                Objects.equals(state, orderDTO.state) &&
                Objects.equals(transportType, orderDTO.transportType) &&
                Objects.equals(portOfOriginCode, orderDTO.portOfOriginCode) &&
                Objects.equals(portOfOriginName, orderDTO.portOfOriginName) &&
                Objects.equals(portOfDestinationCode, orderDTO.portOfDestinationCode) &&
                Objects.equals(portOfDestinationName, orderDTO.portOfDestinationName) &&
                Objects.equals(shipName, orderDTO.shipName) &&
                Objects.equals(ets, orderDTO.ets) &&
                Objects.equals(ats, orderDTO.ats) &&
                Objects.equals(eta, orderDTO.eta) &&
                Objects.equals(ata, orderDTO.ata) &&
                Objects.equals(cargoType, orderDTO.cargoType) &&
                Arrays.equals(containerTypes, orderDTO.containerTypes) &&
                Objects.equals(shipMMSI, orderDTO.shipMMSI);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(customerCode, referenceNumber, customerReferenceNumber, state, transportType,
                portOfOriginCode, portOfOriginName, portOfDestinationCode, portOfDestinationName,
                shipName, ets, ats, eta, ata, cargoType, totalWeight, totalContainers, shipMMSI);
        return 31 * result + Arrays.hashCode(containerTypes);
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "customerCode='" + customerCode +
                ", referenceNumber='" + referenceNumber +
                ", customerReferenceNumber='" + customerReferenceNumber +
                ", state='" + state +
                ", transportType='" + transportType +
                ", portOfOriginCode='" + portOfOriginCode +
                ", portOfOriginName='" + portOfOriginName +
                ", portOfDestinationCode='" + portOfDestinationCode +
                ", portOfDestinationName='" + portOfDestinationName +
                ", shipName='" + shipName +
                ", ets='" + ets +
                ", ats='" + ats +
                ", eta='" + eta +
                ", ata='" + ata +
                ", cargoType='" + cargoType +
                ", totalWeight=" + totalWeight +
                ", totalContainers=" + totalContainers +
                ", containerTypes=" + Arrays.toString(containerTypes) +
                ", shipMMSI='" + shipMMSI +
                '}';
    }
}