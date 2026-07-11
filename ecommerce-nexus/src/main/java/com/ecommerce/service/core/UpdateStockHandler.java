package com.ecommerce.service.core;

import com.ecommerce.service.Marketplace;
import com.ecommerce.model.entity.Product;
import com.ecommerce.exception.InsufficientStockException;

public class UpdateStockHandler implements Handler<DeductStockCommand> {
    private final Marketplace marketplace;

    public UpdateStockHandler(Marketplace marketplace) {
        this.marketplace = marketplace;
    }

    @Override
    public void handle(DeductStockCommand cmd) throws InsufficientStockException {
        Product product = marketplace.getProduct(cmd.productId());
        if (product == null) {
            throw new IllegalArgumentException("Produto não localizado no catálogo.");
        }
        Product updatedProduct = product.deductStock(cmd.quantity());
        marketplace.saveProduct(updatedProduct);
    }
}
