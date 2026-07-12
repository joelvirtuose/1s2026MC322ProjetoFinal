package com.ecommerce.service;

import com.ecommerce.model.valueobject.PaymentMethod;
import com.ecommerce.exception.PaymentDeclinedException;

/**
 * Gateway simulado com regra determinística e explicável:
 * cartão de crédito recusa acima de R$ 5.000 (limite); PIX acima de R$ 20.000.
 */
public class PaymentServiceImpl implements PaymentService {
    private static final double CREDIT_LIMIT = 5000.0;
    private static final double PIX_LIMIT = 20000.0;

    @Override
    public void authorize(double amount, PaymentMethod method) throws PaymentDeclinedException {
        if (amount <= 0) {
            throw new PaymentDeclinedException("Valor de pagamento inválido.");
        }
        double limit = (method == PaymentMethod.CARTAO_CREDITO) ? CREDIT_LIMIT : PIX_LIMIT;
        if (amount > limit) {
            throw new PaymentDeclinedException(String.format(
                "Pagamento recusado: R$ %.2f excede o limite de %s.", amount, method));
        }
        // Autorizado.
    }
}
