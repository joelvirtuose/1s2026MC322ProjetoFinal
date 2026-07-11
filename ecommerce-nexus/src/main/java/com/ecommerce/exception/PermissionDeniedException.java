package com.ecommerce.exception;

/**
 * Lançada quando um usuário tenta executar uma ação não permitida pelo seu papel (Role).
 */
public class PermissionDeniedException extends DomainException {
    public PermissionDeniedException(String message) {
        super(message);
    }
}