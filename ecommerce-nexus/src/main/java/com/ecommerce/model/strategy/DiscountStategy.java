package com.ecommerce.model.strategy;

public interface DiscountStategy {
    /**
     * Calcula o preço final após aplicar o desconto
     * @param currentPrice O preço atual do produto
     * @return O valor calculado
     */ 

    double calculate(double currentPrice);
}