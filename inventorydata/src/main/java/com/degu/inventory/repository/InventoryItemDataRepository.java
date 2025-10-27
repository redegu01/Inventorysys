package com.degu.inventory.repository;

import com.degu.inventory.entity.InventoryItemData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventoryItemDataRepository extends CrudRepository<InventoryItemData, Integer> {
    List<InventoryItemData> findByQuantityAvailableLessThanEqual(double minimumStock);
}