package com.degu.inventory.transform;

import com.degu.inventory.entity.InventoryStatusData;
import com.degu.inventory.model.InventoryStatus;

import java.util.Arrays;
import java.util.Objects;

public class InventoryStatusTransformer {
    public static InventoryStatus toInventoryStatus(InventoryStatusData entity) {
        if (entity == null) {
            return null;
        }
        InventoryStatus model = new InventoryStatus();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setCreated(entity.getCreated());
        model.setLastUpdated(entity.getLastUpdated());
        return model;
    }
    public static InventoryStatusData toInventoryStatusData(InventoryStatus model) {
        if (model == null) {
            return null;
        }
        InventoryStatusData entity = new InventoryStatusData();
        if (model.getId() > 0) {
            entity.setId(model.getId());
        }
        entity.setName(model.getName());
        entity.setCreated(model.getCreated());
        entity.setLastUpdated(model.getLastUpdated());
        return entity;
    }
    public static InventoryStatus[] toInventoryStatusArray(InventoryStatusData[] entities) {
        if (entities == null) {
            return new InventoryStatus[0];
        }
        return Arrays.stream(entities)
                .filter(Objects::nonNull)
                .map(InventoryStatusTransformer::toInventoryStatus)
                .toArray(InventoryStatus[]::new);
    }
}
