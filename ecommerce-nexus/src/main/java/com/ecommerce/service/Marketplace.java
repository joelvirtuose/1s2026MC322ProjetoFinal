package com.ecommerce.service;

import com.google.gson.Gson;
import com.ecommerce.model.entity.Product;
import com.ecommerce.model.entity.Order;
import com.ecommerce.model.entity.User;
import com.ecommerce.model.valueobject.OrderStatus;
import com.ecommerce.service.persistence.JsonlRepository;
import com.ecommerce.service.persistence.Repository;
import com.ecommerce.service.core.Command;
import com.ecommerce.service.core.CommandDispatcher;
import com.ecommerce.service.core.CreateOrderCommand;
import com.ecommerce.service.core.CreateOrderHandler;
import com.ecommerce.service.core.AddItemToOrderCommand;
import com.ecommerce.service.core.AddItemToOrderHandler;
import com.ecommerce.service.core.DeductStockCommand;
import com.ecommerce.service.core.UpdateStockHandler;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Fachada do domínio (Facade). Concentra o acesso às entidades, expõe o motor
 * de comandos e o serviço de consultas, isolando a View da infraestrutura.
 */
public class Marketplace {
    private final Repository<Product, String> productRepository;
    private final Repository<Order, String> orderRepository;
    private final Repository<User, String> userRepository;

    private final CommandDispatcher dispatcher; // Invoker do padrão Command
    private final QueryService queryService;     // Consultas de leitura

    public Marketplace(Gson gson) {
        this.productRepository = new JsonlRepository<>("data/products.jsonl", Product.class, gson);
        this.orderRepository = new JsonlRepository<>("data/orders.jsonl", Order.class, gson);
        this.userRepository = new JsonlRepository<>("data/users.jsonl", User.class, gson);

        // Registra os pares Command -> Handler uma única vez.
        this.dispatcher = new CommandDispatcher();
        this.dispatcher.register(CreateOrderCommand.class, new CreateOrderHandler(this));
        this.dispatcher.register(AddItemToOrderCommand.class, new AddItemToOrderHandler(this));
        this.dispatcher.register(DeductStockCommand.class, new UpdateStockHandler(this));

        // 'this' só é consultado em runtime, não durante a construção (seguro).
        this.queryService = new QueryServiceImpl(this);
    }

    // ---- Acesso a entidades (usado pelos handlers) ----
    public Product getProduct(String id) { return productRepository.findById(id); }
    public void saveProduct(Product product) { productRepository.save(product); }
    public List<Product> getAllProducts() { return productRepository.findAll(); }
    public Order getOrder(String id) { return orderRepository.findById(id); }
    public void saveOrder(Order order) { orderRepository.save(order); }

    // ---- Autenticação (dá vida a User/Role) ----
    /** @return o User cujo e-mail confere, ou null se não existir. */
    public User authenticate(String email) {
        if (email == null) return null;
        for (User user : userRepository.findAll()) {
            if (user.getEmail().equalsIgnoreCase(email)) return user;
        }
        return null;
    }

    // ---- Consulta de leitura delegada ao QueryService ----
    public List<Product> findProductsByPriceRange(double min, double max) {
        return queryService.findProductsByPriceRange(min, max);
    }

    // ---- Ponto único de envio de intenções ao motor de comandos ----
    public void dispatch(Command command) throws Exception {
        dispatcher.dispatch(command);
    }

    // ---- Fluxo de negócio de checkout ----
    public void checkout(String userId, Map<String, Integer> items) throws Exception {
        if (items == null || items.isEmpty()) {
            throw new com.ecommerce.exception.EmptyCartException("Não é possível fechar um pedido vazio.");
        }
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        dispatch(new CreateOrderCommand(orderId, userId));
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            dispatch(new AddItemToOrderCommand(orderId, entry.getKey(), entry.getValue()));
        }
        Order finalizedOrder = getOrder(orderId).withStatus(OrderStatus.FINALIZADO);
        saveOrder(finalizedOrder);
    }
}
