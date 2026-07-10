package com.ecommerce.model.entity;

import com.ecommerce.model.valueobject.OrderItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order implements Entity {
    private final String id;
    private final String userId; // Associação estritamente por ID (Sem referência direta ao objeto User)
    private final List<OrderItem> items;
    private final String status;

    public Order(String id, String userId, List<OrderItem> items, String status) {
        this.id = id;
        this.userId = userId;
        // Blindagem defensiva: garante que a lista interna não seja modificada externamente
        this.items = Collections.unmodifiableList(new ArrayList<>(items));
        this.status = status;
    }

    @Override
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public List<OrderItem> getItems() { return items; }
    public String getStatus() { return status; }

    /**
     * PADRÃO WITHER: Retorna uma nova instância de Order contendo o novo item,
     * preservando a imutabilidade do objeto original.
     */
    public Order withAddedItem(OrderItem newItem) {
        List<OrderItem> updatedItems = new ArrayList<>(this.items);
        updatedItems.add(newItem);
        return new Order(this.id, this.userId, updatedItems, this.status);
    }

    public Order withStatus(String newStatus) {
        return new Order(this.id, this.userId, this.items, newStatus);
    }

    public double calculateOrderTotal() {
        return items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }
}