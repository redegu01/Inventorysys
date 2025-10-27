package com.degu.inventory.controller;

import com.degu.inventory.model.InventoryItem;
import com.degu.inventory.service.InventoryItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

class StockOperationRequest {
	public Double quantity;
	public String reason;
	public String reference;
	public String performedBy;
}

@RestController
@RequestMapping("/api/inventory-items")
public class InventoryItemController {

	Logger logger = LoggerFactory.getLogger(InventoryItemController.class);

	@Autowired
	private InventoryItemService inventoryItemService;

	@GetMapping
	public ResponseEntity<InventoryItem[]> listInventoryItems() {
		try {
			InventoryItem[] inventoryItems = inventoryItemService.getAll();
			logger.info("Retrieved {} inventory items.", inventoryItems.length);
			return ResponseEntity.ok(inventoryItems);
		} catch (Exception ex) {
			logger.error("Failed to retrieve inventory items: {}", ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping
	public ResponseEntity<InventoryItem> createInventoryItem(@RequestBody InventoryItem inventoryItem) {
		System.out.println("Received inventory item JSON: " + inventoryItem);
		logger.info("Request to create inventory item: {}", inventoryItem.getName());
		try {
			InventoryItem newInventoryItem = inventoryItemService.create(inventoryItem);
			logger.info("Created inventory item with ID: {}", newInventoryItem.getId());
			return new ResponseEntity<>(newInventoryItem, HttpStatus.CREATED);
		} catch (Exception ex) {
			logger.error("Failed to create inventory item: {}", ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping
	public ResponseEntity<InventoryItem> updateInventoryItem(@RequestBody InventoryItem inventoryItem) {
		logger.info("Request to update inventory item with ID: {}", inventoryItem.getId());
		try {
			InventoryItem updatedInventoryItem = inventoryItemService.update(inventoryItem);
			if (updatedInventoryItem != null) {
				logger.info("Updated inventory item with ID: {}", updatedInventoryItem.getId());
				return ResponseEntity.ok(updatedInventoryItem);
			} else {
				logger.warn("Inventory item with ID {} not found for update.", inventoryItem.getId());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception ex) {
			logger.error("Failed to update inventory item with ID {}: {}", inventoryItem.getId(), ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<InventoryItem> getInventoryItem(@PathVariable Integer id) {
		logger.info("Request to get inventory item with ID: {}", id);
		try {
			InventoryItem inventoryItem = inventoryItemService.get(id);
			if (inventoryItem != null) {
				logger.info("Found inventory item with ID: {}", id);
				return ResponseEntity.ok(inventoryItem);
			} else {
				logger.warn("Inventory item with ID {} not found.", id);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception ex) {
			logger.error("Failed to get inventory item with ID {}: {}", id, ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteInventoryItem(@PathVariable Integer id) {
		logger.info("Request to delete inventory item with ID: {}", id);
		try {
			inventoryItemService.delete(id);
			logger.info("Deleted inventory item with ID: {}", id);
			return ResponseEntity.noContent().build();
		} catch (Exception ex) {
			logger.error("Failed to delete inventory item with ID {}: {}", id, ex.getMessage(), ex);
			if (ex.getMessage() != null && ex.getMessage().contains("Unable to locate")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	@PostMapping("/{itemId}/add-stock")
	public ResponseEntity<InventoryItem> addStock(
			@PathVariable Integer itemId,
			@RequestBody StockOperationRequest request) {
		logger.info("Request to add {} stock to item ID: {}", request.quantity, itemId);
		try {
			if (request.quantity == null || request.quantity <= 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
			InventoryItem updatedItem = inventoryItemService.addStock(
					itemId, request.quantity, request.reason, request.reference, request.performedBy);
			logger.info("Successfully added stock to item ID: {}. New quantity: {}", itemId, updatedItem.getQuantityAvailable());
			return ResponseEntity.ok(updatedItem);
		} catch (IllegalArgumentException e) {
			logger.error("Invalid quantity for add stock: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch (Exception ex) {
			logger.error("Failed to add stock to item ID {}: {}", itemId, ex.getMessage(), ex);
			if (ex.getMessage() != null && ex.getMessage().contains("not found")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/{itemId}/reduce-stock")
	public ResponseEntity<InventoryItem> reduceStock(
			@PathVariable Integer itemId,
			@RequestBody StockOperationRequest request) {
		logger.info("Request to reduce {} stock from item ID: {}", request.quantity, itemId);
		try {
			if (request.quantity == null || request.quantity <= 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
			InventoryItem updatedItem = inventoryItemService.reduceStock(
					itemId, request.quantity, request.reason, request.reference, request.performedBy);
			logger.info("Successfully reduced stock from item ID: {}. New quantity: {}", itemId, updatedItem.getQuantityAvailable());
			return ResponseEntity.ok(updatedItem);
		} catch (IllegalArgumentException e) {
			logger.error("Invalid quantity for reduce stock: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch (Exception ex) {
			logger.error("Failed to reduce stock from item ID {}: {}", itemId, ex.getMessage(), ex);
			if (ex.getMessage() != null) {
				if (ex.getMessage().contains("not found")) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
				} else if (ex.getMessage().contains("Insufficient stock")) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
				}
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/{itemId}/adjust-stock")
	public ResponseEntity<InventoryItem> adjustStock(
			@PathVariable Integer itemId,
			@RequestBody StockOperationRequest request) {
		logger.info("Request to adjust stock for item ID {} to new quantity: {}", itemId, request.quantity);
		try {
			if (request.quantity == null || request.quantity < 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
			InventoryItem updatedItem = inventoryItemService.adjustStock(
					itemId, request.quantity, request.reason, request.performedBy);
			logger.info("Successfully adjusted stock for item ID: {}. New quantity: {}", itemId, updatedItem.getQuantityAvailable());
			return ResponseEntity.ok(updatedItem);
		} catch (IllegalArgumentException e) {
			logger.error("Invalid quantity for adjust stock: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch (Exception ex) {
			logger.error("Failed to adjust stock for item ID {}: {}", itemId, ex.getMessage(), ex);
			if (ex.getMessage() != null && ex.getMessage().contains("not found")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/low-stock")
	public ResponseEntity<InventoryItem[]> getLowStockItems() {
		logger.info("Request to get low stock items.");
		try {
			InventoryItem[] lowStockItems = inventoryItemService.getLowStockItems();
			logger.info("Retrieved {} low stock items.", lowStockItems.length);
			return ResponseEntity.ok(lowStockItems);
		} catch (Exception ex) {
			logger.error("Failed to retrieve low stock items: {}", ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}