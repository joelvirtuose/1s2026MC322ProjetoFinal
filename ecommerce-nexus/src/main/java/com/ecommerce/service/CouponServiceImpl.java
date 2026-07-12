package com.ecommerce.service;

import com.ecommerce.model.strategy.DiscountStrategy;
import com.ecommerce.model.strategy.PercentageDiscountStrategy;
import com.ecommerce.model.strategy.FixedAmountDiscountStrategy;
import com.ecommerce.exception.InvalidCouponException;
import java.util.Map;

/** Catálogo de cupons válidos mapeados a estratégias de desconto (reusa Strategy). */
public class CouponServiceImpl implements CouponService {
    private static final Map<String, DiscountStrategy> COUPONS = Map.of(
        "BEMVINDO10", new PercentageDiscountStrategy(10),
        "PROMO50",    new FixedAmountDiscountStrategy(50)
    );

    @Override
    public DiscountStrategy resolve(String code) throws InvalidCouponException {
        if (code == null || code.isBlank()) {
            throw new InvalidCouponException("Nenhum cupom informado.");
        }
        DiscountStrategy strategy = COUPONS.get(code.trim().toUpperCase());
        if (strategy == null) {
            throw new InvalidCouponException("Cupom inválido ou expirado: " + code);
        }
        return strategy;
    }
}
