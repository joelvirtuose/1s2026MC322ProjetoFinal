package com.ecommerce.exception;

/*
Excessão lançada quando um cupom não é aplicável à compra (Checked)
*/
public class InvalidCouponException extends Exception {
    public InvalidCouponException(String message) {
        super(message);
    }
}
