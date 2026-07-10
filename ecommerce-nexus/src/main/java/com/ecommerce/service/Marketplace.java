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

    /**
     * Endpoint de Checkout: Recebe os dados brutos da View e converte em Comandos de Domínio.
     * @param userId O ID do usuário logado.
     * @param items Um mapa contendo ID do produto como chave e Quantidade como valor.
     */
    public void checkout(String userId, Map<String, Integer> items) {
        // TODO (Integrante B): Criar o CreateOrderCommand e invocar o CreateOrderHandler
        
        // Simulação provisória para que o Integrante C consiga testar o fluxo da CLI
        System.out.println("[DEBUG MARKETPLACE] Recebendo pedido do utilizador: " + userId);
        System.out.println("[DEBUG MARKETPLACE] Itens do pedido: " + items.toString());
        System.out.println("[DEBUG MARKETPLACE] Encaminhando para Handlers de Domínio...");
    }
}
