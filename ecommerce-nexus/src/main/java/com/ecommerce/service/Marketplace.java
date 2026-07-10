package com.ecommerce.service;

import com.google.gson.Gson;
import com.ecommerce.model.entity.Product;
import com.ecommerce.model.entity.Order;
import com.ecommerce.service.persistence.JsonlRepository;
import com.ecommerce.service.persistence.Repository;

public class Marketplace {
    private final Repository<Product, String> productRepository;
    private final Repository<Order, String> orderRepository;

    public Marketplace(Gson gson) {
        // Inicializa disparando automaticamente a reidratação cronológica do log histórico
        this.productRepository = new JsonlRepository<>("data/products.jsonl", Product.class, gson);
        this.orderRepository = new JsonlRepository<>("data/orders.jsonl", Order.class, gson);
    }

    /**
     * 
     * @param id
     * @return
     */
    public Product getProduct(String id) {
        return productRepository.findById(id);
    }

    /**
     * 
     * @param product
     */
    public void saveProduct(Product product) {
        productRepository.productRepository.save(product);
    }

    /**
     * 
     * @param id
     */
    public Order getOrder(String id) {
        return orderRepository.findById(id);
    }

    /**
     * 
     * @param order
     */
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
}