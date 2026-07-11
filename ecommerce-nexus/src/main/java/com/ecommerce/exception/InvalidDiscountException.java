package com.ecommerce.exception;

/*
Excessão para impedir valores de desconto que violem regras de negócio, como o preço de um produto se tornar menor que 0 (unchecked)
*/
public class InvalidDiscountException extends DomainException{
    public InvalidDiscountException(String message) {
        super(message);
    }
}