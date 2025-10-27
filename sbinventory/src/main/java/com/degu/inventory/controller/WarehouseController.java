package com.degu.inventory.controller;

import com.degu.inventory.model.Warehouse;
import com.degu.inventory.service.WarehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

	Logger logger = LoggerFactory.getLogger(WarehouseController.class);

	@Autowired
	private WarehouseService warehouseService;

	@GetMapping
	public ResponseEntity<Warehouse[]> listWarehouses() {
		try {
			Warehouse[] warehouses = warehouseService.getAll();
			logger.info("Retrieved {} warehouses.", warehouses.length);
			return ResponseEntity.ok(warehouses);
		} catch (Exception ex) {
			logger.error("Failed to retrieve warehouses: {}", ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping
	public ResponseEntity<Warehouse> createWarehouse(@RequestBody Warehouse warehouse) {
		logger.info("Request to create warehouse: {}", warehouse.getName());
		try {
			Warehouse newWarehouse = warehouseService.create(warehouse);
			logger.info("Created warehouse with ID: {}", newWarehouse.getId());
			return new ResponseEntity<>(newWarehouse, HttpStatus.CREATED);
		} catch (Exception ex) {
			logger.error("Failed to create warehouse: {}", ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping
	public ResponseEntity<Warehouse> updateWarehouse(@RequestBody Warehouse warehouse) {
		logger.info("Request to update warehouse with ID: {}", warehouse.getId());
		try {
			Warehouse updatedWarehouse = warehouseService.update(warehouse);
			if (updatedWarehouse != null) {
				logger.info("Updated warehouse with ID: {}", updatedWarehouse.getId());
				return ResponseEntity.ok(updatedWarehouse);
			} else {
				logger.warn("Warehouse with ID {} not found for update.", warehouse.getId());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception ex) {
			logger.error("Failed to update warehouse with ID {}: {}", warehouse.getId(), ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Warehouse> getWarehouse(@PathVariable Integer id) {
		logger.info("Request to get warehouse with ID: {}", id);
		try {
			Warehouse warehouse = warehouseService.get(id);
			if (warehouse != null) {
				logger.info("Found warehouse with ID: {}", id);
				return ResponseEntity.ok(warehouse);
			} else {
				logger.warn("Warehouse with ID {} not found.", id);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception ex) {
			logger.error("Failed to get warehouse with ID {}: {}", id, ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteWarehouse(@PathVariable Integer id) {
		logger.info("Request to delete warehouse with ID: {}", id);
		try {
			warehouseService.delete(id);
			logger.info("Deleted warehouse with ID: {}", id);
			return ResponseEntity.noContent().build();
		} catch (Exception ex) {
			logger.error("Failed to delete warehouse with ID {}: {}", id, ex.getMessage(), ex);
			if (ex.getMessage() != null && ex.getMessage().contains("Unable to locate")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}