package com.ecommerce.model.strategy;

public class FixedAmountDiscountStrategy implements DiscountStategy{
    private final double discountAmount;

    public FixedAmountDiscountStrategy(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public double calculate(double currentPrice) {
        return currentPrice - discountAmount;
    }

    
}
