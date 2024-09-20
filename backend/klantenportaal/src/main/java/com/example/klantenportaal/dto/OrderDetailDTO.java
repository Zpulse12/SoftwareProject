package com.example.klantenportaal.dto;

import java.util.Arrays;
import java.util.Objects;

public record OrderDetailDTO(
        String referenceNumber,
        String customerReferenceNumber,
        String state,
        String transportType,
        String portOfOriginCode,
        String portOfOriginName,
        String portOfDestinationCode,
        String portOfDestinationName,
        String portCode,
        String shipName,
        String shipIMO,
        String shipMMSI,
        String shipType,
        String ets,
        String ats,
        String eta,
        String ata,
        String preCarriage,
        String estimatedTimeCargoOnQuay,
        String actualTimeCargoLoaded,
        String billOfLadingDownloadLink,
        String packingListDownloadLink,
        String customsDownloadLink,
        String cargoType,
        ProductDetailDTO[] products) {

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderDetailDTO that = (OrderDetailDTO) o;
        return Objects.equals(referenceNumber, that.referenceNumber) &&
                Objects.equals(customerReferenceNumber, that.customerReferenceNumber) &&
                Objects.equals(state, that.state) &&
                Objects.equals(transportType, that.transportType) &&
                Objects.equals(portOfOriginCode, that.portOfOriginCode) &&
                Objects.equals(portOfOriginName, that.portOfOriginName) &&
                Objects.equals(portOfDestinationCode, that.portOfDestinationCode) &&
                Objects.equals(portOfDestinationName, that.portOfDestinationName) &&
                Objects.equals(portCode, that.portCode) &&
                Objects.equals(shipName, that.shipName) &&
                Objects.equals(shipIMO, that.shipIMO) &&
                Objects.equals(shipMMSI, that.shipMMSI) &&
                Objects.equals(shipType, that.shipType) &&
                Objects.equals(ets, that.ets) &&
                Objects.equals(ats, that.ats) &&
                Objects.equals(eta, that.eta) &&
                Objects.equals(ata, that.ata) &&
                Objects.equals(preCarriage, that.preCarriage) &&
                Objects.equals(estimatedTimeCargoOnQuay, that.estimatedTimeCargoOnQuay) &&
                Objects.equals(actualTimeCargoLoaded, that.actualTimeCargoLoaded) &&
                Objects.equals(billOfLadingDownloadLink, that.billOfLadingDownloadLink) &&
                Objects.equals(packingListDownloadLink, that.packingListDownloadLink) &&
                Objects.equals(customsDownloadLink, that.customsDownloadLink) &&
                Objects.equals(cargoType, that.cargoType) &&
                Arrays.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(referenceNumber, customerReferenceNumber, state, transportType, portOfOriginCode,
                portOfOriginName, portOfDestinationCode, portOfDestinationName, portCode, shipName, shipIMO, shipMMSI,
                shipType, ets, ats, eta, ata, preCarriage, estimatedTimeCargoOnQuay, actualTimeCargoLoaded,
                billOfLadingDownloadLink, packingListDownloadLink, customsDownloadLink, cargoType);
        return 31 * result + Arrays.hashCode(products);
    }

    @Override
    public String toString() {
        return "OrderDetailDTO{" +
                "referenceNumber=" + referenceNumber +
                ", customerReferenceNumber=" + customerReferenceNumber +
                ", state=" + state +
                ", transportType=" + transportType +
                ", portOfOriginCode=" + portOfOriginCode +
                ", portOfOriginName=" + portOfOriginName +
                ", portOfDestinationCode=" + portOfDestinationCode +
                ", portOfDestinationName=" + portOfDestinationName +
                ", portCode=" + portCode +
                ", shipName=" + shipName +
                ", shipIMO=" + shipIMO +
                ", shipMMSI=" + shipMMSI +
                ", shipType=" + shipType +
                ", ets=" + ets +
                ", ats=" + ats +
                ", eta=" + eta +
                ", ata=" + ata +
                ", preCarriage=" + preCarriage +
                ", estimatedTimeCargoOnQuay=" + estimatedTimeCargoOnQuay +
                ", actualTimeCargoLoaded=" + actualTimeCargoLoaded +
                ", billOfLadingDownloadLink=" + billOfLadingDownloadLink +
                ", packingListDownloadLink=" + packingListDownloadLink +
                ", customsDownloadLink=" + customsDownloadLink +
                ", cargoType=" + cargoType +
                ", products=" + Arrays.toString(products) +
                '}';
    }
}