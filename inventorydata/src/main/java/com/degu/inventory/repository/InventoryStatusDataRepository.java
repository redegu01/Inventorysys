package com.degu.inventory.repository;
import com.degu.inventory.entity.InventoryStatusData; // Changed entity
import org.springframework.data.repository.CrudRepository;
public interface InventoryStatusDataRepository extends CrudRepository<InventoryStatusData, Integer> {} // Changed interface name and generic type
