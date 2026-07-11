package com.ecommerce.model.valueobject;

public record Price(double value) {
    public Price {
        if (value < 0) {
            throw new IllegalArgumentException("O valor monetário não pode ser negativo.");
        }
    }
}