package com.ecommerce.service;

import com.google.gson.Gson;
import com.ecommerce.model.entity.Product;
import com.ecommerce.model.entity.Order;
import com.ecommerce.service.persistence.JsonlRepository;
import com.ecommerce.service.persistence.Repository;
import com.ecommerce.service.core.CreateOrderCommand;
import com.ecommerce.service.core.CreateOrderHandler;
import com.ecommerce.service.core.AddItemToOrderCommand;
import com.ecommerce.service.core.AddItemToOrderHandler;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Marketplace {
    private final Repository<Product, String> productRepository;
    private final Repository<Order, String> orderRepository;

    // Handlers encapsulados internamente na Fachada
    private final CreateOrderHandler createOrderHandler;
    private final AddItemToOrderHandler addItemToOrderHandler;

    public Marketplace(Gson gson) {
        // Inicializa disparando automaticamente a reidratação cronológica do log histórico
        this.productRepository = new JsonlRepository<>("data/products.jsonl", Product.class, gson);
        this.orderRepository = new JsonlRepository<>("data/orders.jsonl", Order.class, gson);

        this.createOrderHandler = new CreateOrderHandler(this);
        this.addItemToOrderHandler = new AddItemToOrderHandler(this);
    }

    /**
     * 
     * @param id
     * @return
     */
    public Product getProduct(String id) { return productRepository.findById(id); }

    /**
     * 
     * @param product
     */
    public void saveProduct(Product product) { productRepository.save(product); }

    /**
     * Retorna a lista de todos os produtos disponíveis no catálogo.
     * Delega a requisição para a camada de persistência.
     */
    public List<Product> getAllProducts() { return productRepository.findAll(); }

    /**
     * @param id
     */
    public Order getOrder(String id) { return orderRepository.findById(id); }

    /**
     * @param order
     */
    public void saveOrder(Order order) { orderRepository.save(order); }

    /**
     * Endpoint de Checkout: Recebe os dados brutos da View e converte em Comandos de Domínio.
     * @param userId O ID do usuário logado.
     * @param items Um mapa contendo ID do produto como chave e Quantidade como valor.
     */
    public void checkout(String userId, Map<String, Integer> items) throws Exception {
        // 1. Gera um ID estável único para o novo Pedido/Carrinho corporativo
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 2. Dispara a criação do Pedido através do Handler correspondente
        CreateOrderCommand createCmd = new CreateOrderCommand(orderId, userId);
        createOrderHandler.execute(createCmd);

        // 3. Itera sobre os itens selecionados na View disparando as regras atômicas de estoque
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            AddItemToOrderCommand addItemCmd = new AddItemToOrderCommand(orderId, entry.getKey(), entry.getValue());
            addItemToOrderHandler.execute(addItemCmd);
        }
        
        // 4. Altera o status do pedido para FINALIZADO
        Order finalizedOrder = getOrder(orderId).withStatus("FINALIZADO");
        saveOrder(finalizedOrder);
    }
}
