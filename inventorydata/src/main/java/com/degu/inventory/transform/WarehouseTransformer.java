package com.degu.inventory.transform;

import com.degu.inventory.entity.WarehouseData;
import com.degu.inventory.model.Warehouse;

import java.util.Arrays;
import java.util.Objects;

public class WarehouseTransformer {
    public static Warehouse toWarehouse(WarehouseData entity) {
        if (entity == null) {
            return null;
        }
        Warehouse model = new Warehouse();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setLocation(entity.getLocation());
        model.setAddress(entity.getAddress());
        model.setManager(entity.getManager());
        model.setCreated(entity.getCreated());
        model.setLastUpdated(entity.getLastUpdated());
        return model;
    }
    public static WarehouseData toWarehouseData(Warehouse model) {
        if (model == null) {
            return null;
        }
        WarehouseData entity = new WarehouseData();
        if (model.getId() > 0) {
            entity.setId(model.getId());
        }
        entity.setName(model.getName());
        entity.setLocation(model.getLocation());
        entity.setAddress(model.getAddress());
        entity.setManager(model.getManager());
        entity.setCreated(model.getCreated()); // Often null for new entities, set by DB
        entity.setLastUpdated(model.getLastUpdated()); // Often null for new entities, set by DB
        return entity;
    }
    public static Warehouse[] toWarehouseArray(WarehouseData[] entities) {
        if (entities == null) {
            return new Warehouse[0];
        }
        return Arrays.stream(entities)
                .filter(Objects::nonNull)
                .map(WarehouseTransformer::toWarehouse)
                .toArray(Warehouse[]::new);
    }
}
