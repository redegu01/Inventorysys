package com.degu.inventory.serviceimpl;
import com.degu.inventory.entity.InventoryItemData;
import com.degu.inventory.model.InventoryItem;
import com.degu.inventory.repository.InventoryItemDataRepository;
import com.degu.inventory.service.InventoryItemService;
import com.degu.inventory.transform.InventoryItemTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryItemServiceImpl implements InventoryItemService {

	Logger logger = LoggerFactory.getLogger(InventoryItemServiceImpl.class);

	@Autowired
	InventoryItemDataRepository inventoryItemDataRepository;

	@Override
	public InventoryItem[] getAll() throws Exception {
		List<InventoryItemData> inventoryItemsData = (List<InventoryItemData>) inventoryItemDataRepository.findAll();
		// Use InventoryItemTransformer for conversion
		return InventoryItemTransformer.toInventoryItemArray(inventoryItemsData.toArray(new InventoryItemData[0]));
	}

	@Override
	public InventoryItem create(InventoryItem inventoryItem) throws Exception {
		logger.info("create: Input inventory item: {}", inventoryItem.toString());
		InventoryItemData inventoryItemData = InventoryItemTransformer.toInventoryItemData(inventoryItem);
		inventoryItemData.setId(0);
		inventoryItemData = inventoryItemDataRepository.save(inventoryItemData);
		logger.info("create: Saved inventory item data: {}", inventoryItemData.toString());

		return InventoryItemTransformer.toInventoryItem(inventoryItemData);
	}

	@Override
	public InventoryItem update(InventoryItem inventoryItem) throws Exception {
		int id = inventoryItem.getId();
		Optional<InventoryItemData> optional  = inventoryItemDataRepository.findById(id);

		if(optional.isPresent()){
			logger.info("update: Updating inventory item with ID: {}", id);
			InventoryItemData existingInventoryItemData = optional.get();
			InventoryItemData inventoryItemDataToUpdate = InventoryItemTransformer.toInventoryItemData(inventoryItem);
			inventoryItemDataToUpdate.setCreated(existingInventoryItemData.getCreated());
			InventoryItemData updatedInventoryItemData = inventoryItemDataRepository.save(inventoryItemDataToUpdate);
			logger.info("update: Updated inventory item data: {}", updatedInventoryItemData.toString());

			return InventoryItemTransformer.toInventoryItem(updatedInventoryItemData);
		}
		else {
			logger.error("update: Inventory item record with id: {} does not exist.", id);
			throw new Exception("Inventory item record with id: " + id + " does not exist.");
		}
	}

	@Override
	public InventoryItem get(Integer id) throws Exception {
		logger.info("get: Input ID: {}", id);
		Optional<InventoryItemData> optional = inventoryItemDataRepository.findById(id);

		if(optional.isPresent()) {
			logger.info("get: Inventory item with ID {} found.", id);
			return InventoryItemTransformer.toInventoryItem(optional.get());
		}
		else {
			logger.info("get: Failed to locate inventory item with ID: {}", id);
			throw new Exception("Unable to locate inventory item with ID: " + id);
		}
	}

	@Override
	public void delete(Integer id) throws Exception {
		logger.info("delete: Input ID: {}", id);
		Optional<InventoryItemData> optional = inventoryItemDataRepository.findById(id);

		if( optional.isPresent()) {
			inventoryItemDataRepository.delete(optional.get());
			logger.info("delete: Successfully deleted Inventory item record with id: {}", id);
		}
		else {
			logger.error("delete: Unable to locate inventory item with id: {}", id);
			throw new Exception("Unable to locate inventory item with id: " + id);
		}
	}
	@Override
	public InventoryItem addStock(Integer itemId, double quantityToAdd, String reason, String reference, String performedBy) throws Exception {
		logger.info("addStock: Item ID: {}, Quantity: {}, Reason: {}", itemId, quantityToAdd, reason);
		InventoryItemData itemData = inventoryItemDataRepository.findById(itemId)
				.orElseThrow(() -> new Exception("Inventory item not found for adding stock: " + itemId));

		if (quantityToAdd <= 0) {
			throw new IllegalArgumentException("Quantity to add must be positive.");
		}

		itemData.setQuantityAvailable(itemData.getQuantityAvailable() + quantityToAdd);
		// TODO: Optionally, log this movement in a separate StockMovementData entity if you create one
		logger.debug("Stock for item {} before add: {}, after: {}", itemId, itemData.getQuantityAvailable() - quantityToAdd, itemData.getQuantityAvailable());

		InventoryItemData updatedEntity = inventoryItemDataRepository.save(itemData);
		return InventoryItemTransformer.toInventoryItem(updatedEntity);
	}

	@Override
	public InventoryItem reduceStock(Integer itemId, double quantityToReduce, String reason, String reference, String performedBy) throws Exception {
		logger.info("reduceStock: Item ID: {}, Quantity: {}, Reason: {}", itemId, quantityToReduce, reason);
		InventoryItemData itemData = inventoryItemDataRepository.findById(itemId)
				.orElseThrow(() -> new Exception("Inventory item not found for reducing stock: " + itemId));

		if (quantityToReduce <= 0) {
			throw new IllegalArgumentException("Quantity to reduce must be positive.");
		}
		if (itemData.getQuantityAvailable() < quantityToReduce) {
			throw new Exception("Insufficient stock for item " + itemId + ". Available: " + itemData.getQuantityAvailable() + ", Requested: " + quantityToReduce);
		}

		itemData.setQuantityAvailable(itemData.getQuantityAvailable() - quantityToReduce);
		// TODO: Optionally, log this movement in a separate StockMovementData entity
		logger.debug("Stock for item {} before reduce: {}, after: {}", itemId, itemData.getQuantityAvailable() + quantityToReduce, itemData.getQuantityAvailable());


		InventoryItemData updatedEntity = inventoryItemDataRepository.save(itemData);
		return InventoryItemTransformer.toInventoryItem(updatedEntity);
	}

	@Override
	public InventoryItem adjustStock(Integer itemId, double newQuantity, String reason, String performedBy) throws Exception {
		logger.info("adjustStock: Item ID: {}, New Quantity: {}, Reason: {}", itemId, newQuantity, reason);
		InventoryItemData itemData = inventoryItemDataRepository.findById(itemId)
				.orElseThrow(() -> new Exception("Inventory item not found for adjusting stock: " + itemId));

		if (newQuantity < 0) {
			throw new IllegalArgumentException("New quantity cannot be negative.");
		}

		double oldQuantity = itemData.getQuantityAvailable();
		itemData.setQuantityAvailable(newQuantity);
		logger.debug("Stock for item {} adjusted from {} to {}", itemId, oldQuantity, newQuantity);

		InventoryItemData updatedEntity = inventoryItemDataRepository.save(itemData);
		return InventoryItemTransformer.toInventoryItem(updatedEntity);
	}

	@Override
	public InventoryItem[] getLowStockItems() throws Exception {
		logger.info("getLowStockItems: Retrieving items below minimum stock.");
		double minimumStockThreshold = 10.0;
		List<InventoryItemData> lowStockEntities = inventoryItemDataRepository.findByQuantityAvailableLessThanEqual(minimumStockThreshold);
		return InventoryItemTransformer.toInventoryItemArray(lowStockEntities.toArray(new InventoryItemData[0]));
	}
}
