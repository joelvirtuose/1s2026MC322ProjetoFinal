package com.ecommerce.service.core;

import com.ecommerce.service.Marketplace;
import com.ecommerce.model.entity.Order;
import com.ecommerce.model.entity.Product;
import com.ecommerce.model.valueobject.OrderItem;
import com.ecommerce.model.valueobject.OrderStatus;
import com.ecommerce.exception.EntityNotFoundException;
import com.ecommerce.exception.InvalidOrderStateException;
import com.ecommerce.exception.InsufficientStockException;

public class AddItemToOrderHandler implements Handler<AddItemToOrderCommand> {
    private final Marketplace marketplace;

    public AddItemToOrderHandler(Marketplace marketplace) {
        this.marketplace = marketplace;
    }

    @Override
    public void handle(AddItemToOrderCommand cmd) throws InsufficientStockException {
        Order order = marketplace.getOrder(cmd.orderId());
        if (order == null) {
            throw new EntityNotFoundException("Pedido ID '" + cmd.orderId() + "' não foi localizado.");
        }
        if (order.getStatus() != OrderStatus.CARRINHO_ABERTO) {
            throw new InvalidOrderStateException("Operação negada: O pedido " + cmd.orderId()
                + " já se encontra em estado " + order.getStatus());
        }
        Product product = marketplace.getProduct(cmd.productId());
        if (product == null) {
            throw new EntityNotFoundException("Produto ID '" + cmd.productId() + "' não existe no catálogo.");
        }
        Product updatedProduct = product.deductStock(cmd.quantity());
        OrderItem newItem = new OrderItem(product.getId(), cmd.quantity(), product.getPrice());
        Order updatedOrder = order.withAddedItem(newItem);
        marketplace.saveProduct(updatedProduct);
        marketplace.saveOrder(updatedOrder);
    }
}
