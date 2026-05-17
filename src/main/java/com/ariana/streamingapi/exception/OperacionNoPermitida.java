package com.ariana.streamingapi.exception;

public class OperacionNoPermitida extends RuntimeException {
    public OperacionNoPermitida(String message) {
        super(message);
    }
}
