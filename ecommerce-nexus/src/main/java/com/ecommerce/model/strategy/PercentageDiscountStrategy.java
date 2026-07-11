package com.ecommerce.model.strategy;

public class PercentageDiscountStrategy implements DiscountStategy {
    private final double percentage;

    public PercentageDiscountStrategy(double percentage) {
        this.percentage = percentage;
    }

    @Override

    public double calculate(double currentPrice) {
        return currentPrice - (currentPrice * (percentage/100));
    }
}
