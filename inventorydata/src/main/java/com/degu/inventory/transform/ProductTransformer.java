package com.degu.inventory.transform;

import com.degu.inventory.entity.ProductData;
import com.degu.inventory.model.Product;

import java.util.Arrays;
import java.util.Objects;

public class ProductTransformer {
    public static Product toProduct(ProductData entity) {
        if (entity == null) {
            return null;
        }
        Product model = new Product();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setCreated(entity.getCreated());
        model.setLastUpdated(entity.getLastUpdated());
        return model;
    }
    public static ProductData toProductData(Product model) {
        if (model == null) {
            return null;
        }
        ProductData entity = new ProductData();
        if (model.getId() > 0) {
            entity.setId(model.getId());
        }
        entity.setName(model.getName());
        entity.setCreated(model.getCreated());
        entity.setLastUpdated(model.getLastUpdated());
        return entity;
    }
    public static Product[] toProductArray(ProductData[] entities) {
        if (entities == null) {
            return new Product[0];
        }
        return Arrays.stream(entities)
                .filter(Objects::nonNull)
                .map(ProductTransformer::toProduct)
                .toArray(Product[]::new);
    }
}
