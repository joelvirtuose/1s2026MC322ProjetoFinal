package com.ecommerce.exception;

/*
Excessão lançada quando um carrinho vazio tenta ser processada no pagamento (Unchecked)
*/
public class EmptyCartException extends DomainException{
    public EmptyCartException(String message) {
        super(message);
    }
}
