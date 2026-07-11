package com.ecommerce.exception;

/*
Excessão lançada quando um carrinho vazio tenta ser processada no pagamento (Unchecked)
*/
public class EmptyCardException extends DomainException{
    public EmptyCardException(String message) {
        super(message);
    }
}
