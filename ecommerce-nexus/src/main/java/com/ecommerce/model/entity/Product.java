package com.ecommerce.model.entity;

import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.InvalidDiscountException;
import com.ecommerce.model.strategy.DiscountStategy;
import com.ecommerce.model.valueobject.Price;

public class Product implements Entity {
    private final String id; // Associação estritamente por ID
    private final String name;
    private final Price price; // Protegido encapsulando a lógica de dinheiro no Value Object
    private final int stockQuantity;

    /**
     * 
     * @param id
     * @param name
     * @param price
     * @param stockQuantity
     */
    public Product(String id, String name, double price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.stockQuantity = stockQuantity;
    }

    // Getters limpos (Apenas Leitura)
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price.value(); } // Retorna o tipo primitivo para compatibilidade de visualização
    public int getStockQuantity() { return stockQuantity; }

    /**
     * PADRÃO WITHER: Modificação sem efeitos colaterais locais.
     * Retorna uma nova instância completa clonando o objeto com o novo dado.
     * @param newStock
     * @return
     */
    public Product withStockQuantity(int newStock) {
        if (newStock < 0) throw new IllegalArgumentException("Estoque não pode ser negativo.");
        return new Product(this.id, this.name, this.price.value(), newStock);
    }

    public Product withDiscount(DiscountStategy strategy) {
        return new Product(this.id, this.name, calculateDiscountedPrice(strategy) , this.stockQuantity);
    }

    /**
     * Regra de Negócio Rica incorporada na própria entidade (Evita Modelo Anêmico)
     * @param quantity
     * @return
     * @throws InsufficientStockException
     */
    public Product deductStock(int quantity) throws InsufficientStockException {
        if (this.stockQuantity < quantity) {
            throw new InsufficientStockException("Estoque insuficiente para o produto: " + name); // Fail-Fast
        }
        return this.withStockQuantity(this.stockQuantity - quantity);
    }

    /**
     * Calcula o preço final aplicando a estratégia de desconto fornecida.
     * Valida o resultado imediatamente (fail fast) para impedir estados inconsistentes.
     * 
     * @param strategy A estratégia de desconto a ser aplicada.
     * @return O valor calculado após o desconto. 
     */

    public double calculateDiscountedPrice(DiscountStategy strategy) {
        if (strategy == null) {
            return this.price.value();
        }

        double finalPrice = strategy.calculate(this.price.value());

        if (finalPrice < 0) {
            throw new InvalidDiscountException("O preço final após o desconto não pode ser inferior a zero.");

        }

        return finalPrice;
    }
}