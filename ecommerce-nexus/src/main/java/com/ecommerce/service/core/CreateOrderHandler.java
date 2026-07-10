package com.ecommerce.service.core;

import com.ecommerce.service.Marketplace;
import com.ecommerce.model.entity.Order;
import java.util.ArrayList;

public class CreateOrderHandler {
    private final Marketplace marketplace;

    public CreateOrderHandler(Marketplace marketplace) {
        this.marketplace = marketplace;
    }

    public void execute(CreateOrderCommand cmd) {
        // Validação Fail-Fast: impede criação duplicada de carrinho ativo
        if (marketplace.getOrder(cmd.orderId()) != null) {
            throw new IllegalStateException("Um pedido ou carrinho com este ID já está ativo.");
        }

        // Cria uma nova instância pura de Order com status "CARRINHO_ABERTO"
        Order newOrder = new Order(cmd.orderId(), cmd.userId(), new ArrayList<>(), "CARRINHO_ABERTO");

        // Persiste incrementalmente no arquivo orders.jsonl e atualiza o cache
        marketplace.saveOrder(newOrder);
    }
}