package com.degu.inventory.transform;

import com.degu.inventory.entity.CategoryData;
import com.degu.inventory.model.Category;

import java.util.Arrays;
import java.util.Objects;

public class CategoryTransformer {
    public static Category toCategory(CategoryData entity) {
        if (entity == null) {
            return null;
        }
        Category model = new Category();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setCreated(entity.getCreated());
        model.setLastUpdated(entity.getLastUpdated());
        return model;
    }

    public static CategoryData toCategoryData(Category model) {
        if (model == null) {
            return null;
        }
        CategoryData entity = new CategoryData();
        if (model.getId() > 0) {
            entity.setId(model.getId());
        }
        entity.setName(model.getName());
        entity.setCreated(model.getCreated());
        entity.setLastUpdated(model.getLastUpdated());
        return entity;
    }

    public static Category[] toCategoryArray(CategoryData[] entities) {
        if (entities == null) {
            return new Category[0];
        }
        return Arrays.stream(entities)
                .filter(Objects::nonNull)
                .map(CategoryTransformer::toCategory)
                .toArray(Category[]::new);
    }
}
