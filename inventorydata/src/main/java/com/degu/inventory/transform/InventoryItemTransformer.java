package com.degu.inventory.transform;

import com.degu.inventory.entity.InventoryItemData;
import com.degu.inventory.model.InventoryItem;

import java.util.Arrays;
import java.util.Objects;

public class InventoryItemTransformer {
    public static InventoryItem toInventoryItem(InventoryItemData entity) {
        if (entity == null) {
            return null;
        }
        InventoryItem model = new InventoryItem();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setProductId(entity.getProductId());
        model.setProductName(entity.getProductName());
        model.setCategoryId(entity.getCategoryId());
        model.setCategoryName(entity.getCategoryName());
        model.setStatusId(entity.getStatusId());
        model.setStatusName(entity.getStatusName());
        model.setQuantityAvailable(entity.getQuantityAvailable());
        model.setMinimumStock(entity.getMinimumStock());
        model.setMaximumStock(entity.getMaximumStock());
        model.setWarehouseId(entity.getWarehouseId());
        model.setWarehouseName(entity.getWarehouseName());
        model.setUnitCost(entity.getUnitCost());
        model.setSupplier(entity.getSupplier());
        model.setSku(entity.getSku());
        model.setCreated(entity.getCreated());
        model.setLastUpdated(entity.getLastUpdated());
        return model;
    }
    public static InventoryItemData toInventoryItemData(InventoryItem model) {
        if (model == null) {
            return null;
        }
        InventoryItemData entity = new InventoryItemData();
        if (model.getId() > 0) {
            entity.setId(model.getId());
        }
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setProductId(model.getProductId());
        entity.setProductName(model.getProductName());
        entity.setCategoryId(model.getCategoryId());
        entity.setCategoryName(model.getCategoryName());
        entity.setStatusId(model.getStatusId());
        entity.setStatusName(model.getStatusName());
        entity.setQuantityAvailable(model.getQuantityAvailable());
        entity.setMinimumStock(model.getMinimumStock());
        entity.setMaximumStock(model.getMaximumStock());
        entity.setWarehouseId(model.getWarehouseId());
        entity.setWarehouseName(model.getWarehouseName());
        entity.setUnitCost(model.getUnitCost());
        entity.setSupplier(model.getSupplier());
        entity.setSku(model.getSku());
        entity.setCreated(model.getCreated());
        entity.setLastUpdated(model.getLastUpdated());
        return entity;
    }
    public static InventoryItem[] toInventoryItemArray(InventoryItemData[] entities) {
        if (entities == null) {
            return new InventoryItem[0];
        }
        return Arrays.stream(entities)
                .filter(Objects::nonNull)
                .map(InventoryItemTransformer::toInventoryItem)
                .toArray(InventoryItem[]::new);
    }
}
