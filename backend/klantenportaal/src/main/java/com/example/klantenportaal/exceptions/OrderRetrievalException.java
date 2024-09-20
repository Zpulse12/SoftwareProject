package com.example.klantenportaal.exceptions;

/**
 * Exception die wordt gegooid wanneer er een fout optreedt bij het ophalen van
 * een order.
 */
public class OrderRetrievalException extends Exception {

    /**
     * Construeert een nieuwe OrderRetrievalException met de opgegeven boodschap.
     *
     * @param message de boodschap.
     */
    public OrderRetrievalException(String message) {
        super(message);
    }

    /**
     * Construeert een nieuwe OrderRetrievalException met de opgegeven boodschap en
     * oorzaak.
     *
     * @param message de boodschap.
     * @param cause   de oorzaak (een null waarde is toegestaan, en betekent dat de
     *                oorzaak niet bestaat of onbekend is).
     */
    public OrderRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}
