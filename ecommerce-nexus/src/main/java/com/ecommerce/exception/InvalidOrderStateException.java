package com.ecommerce.exception;

/**
 * Lançada quando uma operação viola o ciclo de vida ou o estado atual de um pedido.
 */
public class InvalidOrderStateException extends DomainException {
    public InvalidOrderStateException(String message) {
        super(message);
    }
}