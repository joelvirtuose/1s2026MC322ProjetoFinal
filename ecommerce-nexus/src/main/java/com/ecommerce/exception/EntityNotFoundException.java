package com.ecommerce.exception;

/**
 * Lançada quando um recurso (Produto, Pedido, Utilizador) não é localizado pelo ID.
 */
public class EntityNotFoundException extends DomainException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}