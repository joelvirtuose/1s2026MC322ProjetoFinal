package com.ecommerce.service;

import com.ecommerce.model.entity.Product;
import java.util.List;
import java.util.stream.Collectors;

public class QueryServiceImpl implements QueryService {
    private final Marketplace marketplace;

    public QueryServiceImpl(Marketplace marketplace) {
        this.marketplace = marketplace;
    }

    @Override
    public List<Product> findProductsByPriceRange(double min, double max) {
        return marketplace.getAllProducts().stream()
                .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
                .collect(Collectors.toList());
    }
}