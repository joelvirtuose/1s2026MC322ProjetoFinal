package com.ecommerce.service;

import com.ecommerce.model.entity.Product;
import java.util.List;

public interface QueryService {
    List<Product> findProductsByPriceRange(double min, double max);
}