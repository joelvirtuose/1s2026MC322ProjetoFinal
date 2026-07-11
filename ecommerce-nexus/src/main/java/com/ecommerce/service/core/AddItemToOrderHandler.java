package com.ecommerce.service.core;

import com.ecommerce.service.Marketplace;
import com.ecommerce.model.entity.Order;
import com.ecommerce.model.entity.Product;
import com.ecommerce.model.valueobject.OrderItem;
import com.ecommerce.model.valueobject.OrderStatus;
import com.ecommerce.exception.EntityNotFoundException;
import com.ecommerce.exception.InvalidOrderStateException;
import com.ecommerce.exception.InsufficientStockException;

public class AddItemToOrderHandler {
    private final Marketplace marketplace;

    public AddItemToOrderHandler(Marketplace marketplace) {
        this.marketplace = marketplace;
    }

    public void execute(AddItemToOrderCommand cmd) throws InsufficientStockException {
        // 1. FETCH: Localiza o Pedido e o Produto nos respectivos caches através da Fachada
        Order order = marketplace.getOrder(cmd.orderId());
        if (order == null) {
            throw new EntityNotFoundException("Pedido ID '" + cmd.orderId() + "' não foi localizado.");
        }
        if (order.getStatus() != OrderStatus.CARRINHO_ABERTO) {
            throw new InvalidOrderStateException("Operação negada: O pedido " + cmd.orderId() + " já se encontra em estado " + order.getStatus());
        }

        Product product = marketplace.getProduct(cmd.productId());
        if (product == null) {
            throw new EntityNotFoundException("Produto ID '" + cmd.productId() + "' não existe no catálogo.");
        }

        // 2. PURE MUTATION: Deduz o estoque do produto gerando uma nova cópia imutável (Fail-Fast interno)
        Product updatedProduct = product.deductStock(cmd.quantity());

        // Cria o item do pedido baseado no snapshot de preço atual do catálogo
        OrderItem newItem = new OrderItem(product.getId(), cmd.quantity(), product.getPrice());
        Order updatedOrder = order.withAddedItem(newItem);

        // 3. PERSIST: Anexa os novos estados atômicamente no fim dos arquivos .jsonl
        marketplace.saveProduct(updatedProduct);
        marketplace.saveOrder(updatedOrder);
    }
}