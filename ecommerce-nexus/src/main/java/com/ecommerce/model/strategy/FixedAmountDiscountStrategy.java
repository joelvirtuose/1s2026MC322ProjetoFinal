package com.ecommerce.model.strategy;

public class FixedAmountDiscountStrategy implements DiscountStategy{
    private final double discountAmount;

    public FixedAmountDiscountStrategy(double discountAmount) {
        if (discountAmount < 0) throw new IllegalArgumentException("Desconto não pode ser negativo.");
        this.discountAmount = discountAmount;
    }

    @Override
    public double calculate(double currentPrice) {
        return currentPrice - discountAmount;
    }

    
}
