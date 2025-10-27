package com.degu.inventory.controller;

import com.degu.inventory.model.Product;
import com.degu.inventory.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

	@GetMapping
	public ResponseEntity<Product[]> listProducts() {
		try {
			Product[] products = productService.getAll();
			logger.info("Retrieved {} products.", products.length);
			return ResponseEntity.ok(products);
		} catch (Exception ex) {
			logger.error("Failed to retrieve products: {}", ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		logger.info("Request to create product: {}", product.getName());
		try {
			Product newProduct = productService.create(product);
			logger.info("Created product with ID: {}", newProduct.getId());
			return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
		} catch (Exception ex) {
			logger.error("Failed to create product: {}", ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping
	public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
		logger.info("Request to update product with ID: {}", product.getId());
		try {
			Product updatedProduct = productService.update(product);
			if (updatedProduct != null) {
				logger.info("Updated product with ID: {}", updatedProduct.getId());
				return ResponseEntity.ok(updatedProduct);
			} else {
				logger.warn("Product with ID {} not found for update.", product.getId());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception ex) {
			logger.error("Failed to update product with ID {}: {}", product.getId(), ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable Integer id) {
		logger.info("Request to get product with ID: {}", id);
		try {
			Product product = productService.get(id);
			if (product != null) {
				logger.info("Found product with ID: {}", id);
				return ResponseEntity.ok(product);
			} else {
				logger.warn("Product with ID {} not found.", id);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception ex) {
			logger.error("Failed to get product with ID {}: {}", id, ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
		logger.info("Request to delete product with ID: {}", id);
		try {
			productService.delete(id);
			logger.info("Deleted product with ID: {}", id);
			return ResponseEntity.noContent().build();
		} catch (Exception ex) {
			logger.error("Failed to delete product with ID {}: {}", id, ex.getMessage(), ex);
			if (ex.getMessage() != null && ex.getMessage().contains("Unable to locate")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}