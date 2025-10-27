package com.degu.inventory.controller;

import com.degu.inventory.model.Category;
import com.degu.inventory.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	Logger logger = LoggerFactory.getLogger(CategoryController.class);

	@Autowired
	private CategoryService categoryService;

	@GetMapping
	public ResponseEntity<Category[]> listCategories() {
		try {
			Category[] categories = categoryService.getAll();
			logger.info("Retrieved {} categories.", categories.length);
			return ResponseEntity.ok(categories);
		} catch (Exception ex) {
			logger.error("Failed to retrieve categories: {}", ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping
	public ResponseEntity<Category> createCategory(@RequestBody Category category) {
		logger.info("Request to create category: {}", category.getName());
		try {
			Category newCategory = categoryService.create(category);
			logger.info("Created category with ID: {}", newCategory.getId());
			return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
		} catch (Exception ex) {
			logger.error("Failed to create category: {}", ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping
	public ResponseEntity<Category> updateCategory(@RequestBody Category category) {
		logger.info("Request to update category with ID: {}", category.getId());
		try {
			Category updatedCategory = categoryService.update(category);
			if (updatedCategory != null) {
				logger.info("Updated category with ID: {}", updatedCategory.getId());
				return ResponseEntity.ok(updatedCategory);
			} else {
				logger.warn("Category with ID {} not found for update.", category.getId());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception ex) {
			logger.error("Failed to update category with ID {}: {}", category.getId(), ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Category> getCategory(@PathVariable Integer id) {
		logger.info("Request to get category with ID: {}", id);
		try {
			Category category = categoryService.get(id);
			if (category != null) {
				logger.info("Found category with ID: {}", id);
				return ResponseEntity.ok(category);
			} else {
				logger.warn("Category with ID {} not found.", id);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception ex) {
			logger.error("Failed to get category with ID {}: {}", id, ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
		logger.info("Request to delete category with ID: {}", id);
		try {
			categoryService.delete(id);
			logger.info("Deleted category with ID: {}", id);
			return ResponseEntity.noContent().build();
		} catch (Exception ex) {
			logger.error("Failed to delete category with ID {}: {}", id, ex.getMessage(), ex);
			if (ex.getMessage() != null && ex.getMessage().contains("Unable to locate category")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}