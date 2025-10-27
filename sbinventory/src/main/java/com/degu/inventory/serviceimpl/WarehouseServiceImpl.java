package com.degu.inventory.serviceimpl;

import com.degu.inventory.entity.WarehouseData;
import com.degu.inventory.model.Warehouse;
import com.degu.inventory.repository.WarehouseDataRepository;
import com.degu.inventory.service.WarehouseService;
import com.degu.inventory.transform.WarehouseTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseServiceImpl implements WarehouseService {

	Logger logger = LoggerFactory.getLogger(WarehouseServiceImpl.class);

	@Autowired
	WarehouseDataRepository warehouseDataRepository;

	@Override
	public Warehouse[] getAll() throws Exception {
		List<WarehouseData> warehousesData = (List<WarehouseData>) warehouseDataRepository.findAll();
		return WarehouseTransformer.toWarehouseArray(warehousesData.toArray(new WarehouseData[0]));
	}

	@Override
	public Warehouse create(Warehouse warehouse) throws Exception {
		logger.info("create: Input warehouse: {}", warehouse.toString());
		WarehouseData warehouseData = WarehouseTransformer.toWarehouseData(warehouse);
		warehouseData.setId(0);
		warehouseData = warehouseDataRepository.save(warehouseData);
		logger.info("create: Saved warehouse data: {}", warehouseData.toString());

		return WarehouseTransformer.toWarehouse(warehouseData);
	}

	@Override
	public Warehouse update(Warehouse warehouse) throws Exception {
		int id = warehouse.getId();
		Optional<WarehouseData> optional  = warehouseDataRepository.findById(id);

		if(optional.isPresent()){
			logger.info("update: Updating warehouse with ID: {}", id);
			WarehouseData existingWarehouseData = optional.get();
			WarehouseData warehouseDataToUpdate = WarehouseTransformer.toWarehouseData(warehouse);
			warehouseDataToUpdate.setCreated(existingWarehouseData.getCreated());
			WarehouseData updatedWarehouseData = warehouseDataRepository.save(warehouseDataToUpdate);
			logger.info("update: Updated warehouse data: {}", updatedWarehouseData.toString());

			return WarehouseTransformer.toWarehouse(updatedWarehouseData);
		}
		else {
			logger.error("update: Warehouse record with id: {} does not exist.", id);
			throw new Exception("Warehouse record with id: " + id + " does not exist.");
		}
	}

	@Override
	public Warehouse get(Integer id) throws Exception {
		logger.info("get: Input ID: {}", id);
		Optional<WarehouseData> optional = warehouseDataRepository.findById(id);

		if(optional.isPresent()) {
			logger.info("get: Warehouse with ID {} found.", id);
			return WarehouseTransformer.toWarehouse(optional.get());
		}
		else {
			logger.info("get: Failed to locate warehouse with ID: {}", id);
			throw new Exception("Unable to locate warehouse with ID: " + id);
		}
	}

	@Override
	public void delete(Integer id) throws Exception {
		logger.info("delete: Input ID: {}", id);
		Optional<WarehouseData> optional = warehouseDataRepository.findById(id);

		if( optional.isPresent()) {
			warehouseDataRepository.delete(optional.get());
			logger.info("delete: Successfully deleted Warehouse record with id: {}", id);
		}
		else {
			logger.error("delete: Unable to locate warehouse with id: {}", id);
			throw new Exception("Unable to locate warehouse with id: " + id);
		}
	}
}