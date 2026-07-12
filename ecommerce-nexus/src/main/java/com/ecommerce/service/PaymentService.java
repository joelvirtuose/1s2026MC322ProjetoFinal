package com.ecommerce.service;

import com.ecommerce.model.valueobject.PaymentMethod;
import com.ecommerce.exception.PaymentDeclinedException;

/** Contrato de autorização de pagamento (abstração de infraestrutura). */
public interface PaymentService {
    void authorize(double amount, PaymentMethod method) throws PaymentDeclinedException;
}
