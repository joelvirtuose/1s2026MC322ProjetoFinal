package com.ecommerce.service.core;

import com.ecommerce.service.Marketplace;
import com.ecommerce.model.entity.Product;
import com.ecommerce.exception.InsufficientStockException;

/**
 * Record do Java encapsula de forma brilhante comandos imutáveis (DTOs de intenção pura)
 * DeductStockCommand
 * @param productId
 * @param quantity
 */
record DeductStockCommand(String productId, int quantity) {}

public class UpdateStockHandler {
    
    /**
     * 
     * @param cmd
     * @param marketplace
     * @throws InsufficientStockException
     */
    public void execute(DeductStockCommand cmd, Marketplace marketplace) throws InsufficientStockException {
        // 1. Fetch: Resolve o estado em memória cache
        Product product = marketplace.getProduct(cmd.productId());
        if (product == null) {
            throw new IllegalArgumentException("Produto não localizado no catálogo.");
        }

        // 2. Pure Mutation: Aplica a lógica rica e gera a nova instância isolada
        Product updatedProduct = product.deductStock(cmd.quantity());

        // 3. Persist: Grava no arquivo .jsonl (append-only) e atualiza a projeção de cache
        marketplace.saveProduct(updatedProduct);
    }
}
