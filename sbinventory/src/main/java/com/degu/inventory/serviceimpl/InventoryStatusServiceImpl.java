package com.degu.inventory.serviceimpl;

import com.degu.inventory.entity.InventoryStatusData;
import com.degu.inventory.model.InventoryStatus;
import com.degu.inventory.repository.InventoryStatusDataRepository;
import com.degu.inventory.service.InventoryStatusService;
import com.degu.inventory.transform.InventoryStatusTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryStatusServiceImpl implements InventoryStatusService {

	Logger logger = LoggerFactory.getLogger(InventoryStatusServiceImpl.class);

	@Autowired
	InventoryStatusDataRepository inventoryStatusDataRepository;

	@Override
	public InventoryStatus[] getAll() throws Exception {
		List<InventoryStatusData> inventoryStatusesData = (List<InventoryStatusData>) inventoryStatusDataRepository.findAll();
		return InventoryStatusTransformer.toInventoryStatusArray(inventoryStatusesData.toArray(new InventoryStatusData[0]));
	}

	@Override
	public InventoryStatus create(InventoryStatus status) throws Exception {
		logger.info("create: Input inventory status: {}", status.toString());
		InventoryStatusData inventoryStatusData = InventoryStatusTransformer.toInventoryStatusData(status);
		inventoryStatusData.setId(0);
		inventoryStatusData = inventoryStatusDataRepository.save(inventoryStatusData);
		logger.info("create: Saved inventory status data: {}", inventoryStatusData.toString());

		return InventoryStatusTransformer.toInventoryStatus(inventoryStatusData);
	}

	@Override
	public InventoryStatus update(InventoryStatus status) throws Exception {
		int id = status.getId();
		Optional<InventoryStatusData> optional  = inventoryStatusDataRepository.findById(id);

		if(optional.isPresent()){
			logger.info("update: Updating inventory status with ID: {}", id);
			InventoryStatusData existingInventoryStatusData = optional.get();
			InventoryStatusData inventoryStatusDataToUpdate = InventoryStatusTransformer.toInventoryStatusData(status);
			inventoryStatusDataToUpdate.setCreated(existingInventoryStatusData.getCreated());

			InventoryStatusData updatedInventoryStatusData = inventoryStatusDataRepository.save(inventoryStatusDataToUpdate);
			logger.info("update: Updated inventory status data: {}", updatedInventoryStatusData.toString());

			return InventoryStatusTransformer.toInventoryStatus(updatedInventoryStatusData);
		}
		else {
			logger.error("update: Inventory status record with id: {} does not exist.", id);
			throw new Exception("Inventory status record with id: " + id + " does not exist.");
		}
	}

	@Override
	public InventoryStatus get(Integer id) throws Exception {
		logger.info("get: Input ID: {}", id);
		Optional<InventoryStatusData> optional = inventoryStatusDataRepository.findById(id);

		if(optional.isPresent()) {
			logger.info("get: Inventory status with ID {} found.", id);
			return InventoryStatusTransformer.toInventoryStatus(optional.get());
		}
		else {
			logger.info("get: Failed to locate inventory status with ID: {}", id);
			throw new Exception("Unable to locate inventory status with ID: " + id);
		}
	}

	@Override
	public void delete(Integer id) throws Exception {
		logger.info("delete: Input ID: {}", id);
		Optional<InventoryStatusData> optional = inventoryStatusDataRepository.findById(id);

		if( optional.isPresent()) {
			inventoryStatusDataRepository.delete(optional.get());
			logger.info("delete: Successfully deleted Inventory status record with id: {}", id);
		}
		else {
			logger.error("delete: Unable to locate inventory status with id: {}", id);
			throw new Exception("Unable to locate inventory status with id: " + id);
		}
	}
}