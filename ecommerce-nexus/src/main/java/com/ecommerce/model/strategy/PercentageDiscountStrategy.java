package com.ecommerce.model.strategy;

public class PercentageDiscountStrategy implements DiscountStategy {
    private final double percentage;

    public PercentageDiscountStrategy(double percentage) {
        if (percentage < 0 || percentage > 100) throw new IllegalArgumentException("Percentual deve ser entre 0 e 100.");
        this.percentage = percentage;
    }

    @Override

    public double calculate(double currentPrice) {
        return currentPrice - (currentPrice * (percentage/100.0));
    }
}
