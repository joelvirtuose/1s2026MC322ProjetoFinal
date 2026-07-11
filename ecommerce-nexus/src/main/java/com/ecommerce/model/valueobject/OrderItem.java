package com.ecommerce.model.valueobject;

// Objeto de Valor (Value Object) — Imutável por padrão usando record
public record OrderItem(String productId, int quantity, double unitPrice) {
    public OrderItem {
        if (quantity <= 0) throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        if (unitPrice < 0) throw new IllegalArgumentException("Preço unitário não pode ser negativo.");
    }
    
    public double getTotalPrice() {
        return quantity * unitPrice;
    }
}