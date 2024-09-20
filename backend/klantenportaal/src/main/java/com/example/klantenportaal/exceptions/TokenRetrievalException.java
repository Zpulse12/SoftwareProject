package com.example.klantenportaal.exceptions;

/**
 * Exception die wordt gegooid wanneer er een fout optreedt bij het ophalen van
 * een token.
 */
public class TokenRetrievalException extends Exception {

    /**
     * Construeert een nieuwe TokenRetrievalException met de opgegeven boodschap.
     *
     * @param message de boodschap.
     */
    public TokenRetrievalException(String message) {
        super(message);
    }

    /**
     * Construeert een nieuwe TokenRetrievalException met de opgegeven boodschap en
     * oorzaak.
     *
     * @param message de boodschap.
     * @param cause   de oorzaak (een null waarde is toegestaan, en betekent dat de
     *                oorzaak niet bestaat of onbekend is).
     */
    public TokenRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}
