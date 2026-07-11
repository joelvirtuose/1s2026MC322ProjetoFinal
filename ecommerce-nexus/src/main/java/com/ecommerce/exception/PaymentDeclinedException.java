package com.ecommerce.exception;

/*
Excessão lançada com a recusa do método de pagamento (checked)
*/
public class PaymentDeclinedException extends Exception {
    public PaymentDeclinedException(String message) {
        super(message);
    }
}