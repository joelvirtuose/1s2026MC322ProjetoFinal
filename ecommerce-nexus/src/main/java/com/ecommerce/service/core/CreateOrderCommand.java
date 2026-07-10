package com.ecommerce.service.core;

public record CreateOrderCommand(String orderId, String userId) implements Command {
}