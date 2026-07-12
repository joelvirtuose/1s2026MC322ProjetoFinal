package com.ecommerce.service;

import com.ecommerce.model.strategy.DiscountStrategy;
import com.ecommerce.exception.InvalidCouponException;

/** Resolve um código de cupom para uma estratégia de desconto. */
public interface CouponService {
    DiscountStrategy resolve(String code) throws InvalidCouponException;
}
