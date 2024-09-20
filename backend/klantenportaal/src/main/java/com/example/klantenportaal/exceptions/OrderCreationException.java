package com.example.klantenportaal.exceptions;

/**
 * Exception die wordt gegooid wanneer er een fout optreedt bij het aanmaken van
 * een order.
 */
public class OrderCreationException extends Exception {

    /**
     * Construeert een nieuwe OrderCreationException met de opgegeven
     * boodschap.
     *
     * @param message de boodschap.
     */
    public OrderCreationException(String message) {
        super(message);
    }

    /**
     * Construeert een nieuwe OrderCreationException met de opgegeven
     * boodschap en oorzaak.
     *
     * @param message de boodschap.
     * @param cause   de oorzaak (een null waarde is toegestaan, en betekent dat de
     *                oorzaak niet bestaat of onbekend is).
     */
    public OrderCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
