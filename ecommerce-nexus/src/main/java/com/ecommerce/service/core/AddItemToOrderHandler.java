package com.ecommerce.service.core;

import com.ecommerce.service.Marketplace;
import com.ecommerce.model.entity.Order;
import com.ecommerce.model.entity.Product;
import com.ecommerce.model.valueobject.OrderItem;
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
            throw new IllegalArgumentException("Pedido não localizado para o ID informado.");
        }
        if (!"CARRINHO_ABERTO".equals(order.getStatus())) {
            throw new IllegalStateException("Não é possível adicionar itens a um pedido fechado ou finalizado.");
        }

        Product product = marketplace.getProduct(cmd.productId());
        if (product == null) {
            throw new IllegalArgumentException("Produto não localizado no catálogo.");
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