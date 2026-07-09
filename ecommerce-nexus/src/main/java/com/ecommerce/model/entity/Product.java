package com.ecommerce.model.entity;

import com.ecommerce.exception.InsufficientStockException;

public class Product implements Entity {
    private final String id; // Associação estritamente por ID
    private final String name;
    private final double price;
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
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // Getters limpos (Apenas Leitura)
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }

    /**
     * PADRÃO WITHER: Modificação sem efeitos colaterais locais.
     * Retorna uma nova instância completa clonando o objeto com o novo dado.
     * @param newStock
     * @return
     */
    public Product withStockQuantity(int newStock) {
        return new Product(this.id, this.name, this.price, newStock);
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
}