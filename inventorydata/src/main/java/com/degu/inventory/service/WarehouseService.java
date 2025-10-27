package com.degu.inventory.service;
import com.degu.inventory.model.Warehouse;
public interface WarehouseService {
	Warehouse[] getAll() throws Exception;
	Warehouse get(Integer id) throws Exception;
	Warehouse create(Warehouse warehouse) throws Exception;
	Warehouse update(Warehouse warehouse) throws Exception;
	void delete(Integer id) throws Exception;
}
