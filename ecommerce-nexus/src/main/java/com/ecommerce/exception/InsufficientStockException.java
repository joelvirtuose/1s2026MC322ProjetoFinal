package com.ecommerce.exception;

// Estende Exception para ser uma Checked Exception (recuperável), conforme o roteiro
public class InsufficientStockException extends Exception {
    public InsufficientStockException(String message) {
        super(message);
    }
}
