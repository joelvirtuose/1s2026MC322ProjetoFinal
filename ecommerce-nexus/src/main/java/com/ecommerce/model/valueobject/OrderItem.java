package com.ecommerce.model.valueobject;

// Objeto de Valor (Value Object) — Imutável por padrão usando record
public record OrderItem(String productId, int quantity, double unitPrice) {
    public double getTotalPrice() {
        return quantity * unitPrice;
    }
}