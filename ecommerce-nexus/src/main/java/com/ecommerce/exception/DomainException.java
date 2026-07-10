package com.ecommerce.exception;

/**
 * Exceção base para todas as violações de regras de negócio do E-Commerce.
 */
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}