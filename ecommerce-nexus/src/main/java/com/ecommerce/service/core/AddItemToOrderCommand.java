package com.ecommerce.service.core;

public record AddItemToOrderCommand(String orderId, String productId, int quantity) implements Command {
}