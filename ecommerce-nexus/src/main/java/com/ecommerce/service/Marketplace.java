package com.ecommerce.service;

import com.google.gson.Gson;
import com.ecommerce.model.entity.Product;
import com.ecommerce.service.persistence.JsonlRepository;
import com.ecommerce.service.persistence.Repository;

public class Marketplace {
    private final Repository<Product, String> productRepository;

    public Marketplace(Gson gson) {
        // Inicializa disparando automaticamente a reidratação cronológica do log histórico
        this.productRepository = new JsonlRepository<>("data/products.jsonl", Product.class, gson);
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
        productRepository.save(product);
    }

    /**
     * Retorna a lista de todos os produtos disponíveis no catálogo.
     * Delega a requisição para a camada de persistência.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
