package com.degu.inventory.controller;

import com.degu.inventory.model.InventoryStatus;
import com.degu.inventory.service.InventoryStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory-statuses")
public class InventoryStatusController {

	Logger logger = LoggerFactory.getLogger(InventoryStatusController.class);

	@Autowired
	private InventoryStatusService inventoryStatusService;

	@GetMapping
	public ResponseEntity<InventoryStatus[]> listInventoryStatuses() {
		try {
			InventoryStatus[] inventoryStatuses = inventoryStatusService.getAll();
			logger.info("Retrieved {} inventory statuses.", inventoryStatuses.length);
			return ResponseEntity.ok(inventoryStatuses);
		} catch (Exception ex) {
			logger.error("Failed to retrieve inventory statuses: {}", ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping
	public ResponseEntity<InventoryStatus> createInventoryStatus(@RequestBody InventoryStatus status) {
		logger.info("Request to create inventory status: {}", status.getName());
		try {
			InventoryStatus newStatus = inventoryStatusService.create(status);
			logger.info("Created inventory status with ID: {}", newStatus.getId());
			return new ResponseEntity<>(newStatus, HttpStatus.CREATED);
		} catch (Exception ex) {
			logger.error("Failed to create inventory status: {}", ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping
	public ResponseEntity<InventoryStatus> updateInventoryStatus(@RequestBody InventoryStatus status) {
		logger.info("Request to update inventory status with ID: {}", status.getId());
		try {
			InventoryStatus updatedStatus = inventoryStatusService.update(status);
			if (updatedStatus != null) {
				logger.info("Updated inventory status with ID: {}", updatedStatus.getId());
				return ResponseEntity.ok(updatedStatus);
			} else {
				logger.warn("Inventory status with ID {} not found for update.", status.getId());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception ex) {
			logger.error("Failed to update inventory status with ID {}: {}", status.getId(), ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<InventoryStatus> getInventoryStatus(@PathVariable Integer id) {
		logger.info("Request to get inventory status with ID: {}", id);
		try {
			InventoryStatus status = inventoryStatusService.get(id);
			if (status != null) {
				logger.info("Found inventory status with ID: {}", id);
				return ResponseEntity.ok(status);
			} else {
				logger.warn("Inventory status with ID {} not found.", id);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception ex) {
			logger.error("Failed to get inventory status with ID {}: {}", id, ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteInventoryStatus(@PathVariable Integer id) {
		logger.info("Request to delete inventory status with ID: {}", id);
		try {
			inventoryStatusService.delete(id);
			logger.info("Deleted inventory status with ID: {}", id);
			return ResponseEntity.noContent().build();
		} catch (Exception ex) {
			logger.error("Failed to delete inventory status with ID {}: {}", id, ex.getMessage(), ex);
			if (ex.getMessage() != null && ex.getMessage().contains("Unable to locate")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}