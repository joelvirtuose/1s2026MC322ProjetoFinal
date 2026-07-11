package com.ecommerce.service.core;

/** DTO de intenção imutável para dar baixa em estoque (Command). */
public record DeductStockCommand(String productId, int quantity) implements Command {
    public DeductStockCommand {
        if (quantity <= 0) throw new IllegalArgumentException("Quantidade da baixa deve ser positiva.");
    }
}
