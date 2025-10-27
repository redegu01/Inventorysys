package com.degu.inventory.service;
import com.degu.inventory.model.InventoryStatus;
public interface InventoryStatusService {
	InventoryStatus[] getAll() throws Exception;
	InventoryStatus get(Integer id) throws Exception;
	InventoryStatus create(InventoryStatus status) throws Exception;
	InventoryStatus update(InventoryStatus status) throws Exception;
	void delete(Integer id) throws Exception;
}
