package com.ecommerce.service.core;

import com.ecommerce.service.Marketplace;
import com.ecommerce.model.entity.Order;
import com.ecommerce.model.valueobject.OrderStatus;
import java.util.ArrayList;

public class CreateOrderHandler implements Handler<CreateOrderCommand> {
    private final Marketplace marketplace;

    public CreateOrderHandler(Marketplace marketplace) {
        this.marketplace = marketplace;
    }

    @Override
    public void handle(CreateOrderCommand cmd) {
        if (marketplace.getOrder(cmd.orderId()) != null) {
            throw new IllegalStateException("Um pedido ou carrinho com este ID já está ativo.");
        }
        Order newOrder = new Order(cmd.orderId(), cmd.userId(), new ArrayList<>(), OrderStatus.CARRINHO_ABERTO);
        marketplace.saveOrder(newOrder);
    }
}
