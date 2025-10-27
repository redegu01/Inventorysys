package com.degu.inventory.service;
import com.degu.inventory.model.InventoryItem;
public interface InventoryItemService {
	InventoryItem[] getAll() throws Exception;
	InventoryItem get(Integer id) throws Exception;
	InventoryItem create(InventoryItem inventoryItem) throws Exception;
	InventoryItem update(InventoryItem inventoryItem) throws Exception;
	void delete(Integer id) throws Exception;


	/**
	 * Adds quantity to an existing inventory item.
	 * @param itemId The ID of the inventory item.
	 * @param quantityToAdd The amount to add.
	 * @param reason The reason for the stock addition (e.g., "received shipment").
	 * @param reference An optional reference (e.g., "PO-123").
	 * @param performedBy The user performing the action.
	 * @return The updated InventoryItem.
	 * @throws Exception if item not found or quantity is invalid.
	 */
	InventoryItem addStock(Integer itemId, double quantityToAdd, String reason, String reference, String performedBy) throws Exception;

	/**
	 * Reduces quantity from an existing inventory item.
	 * @param itemId The ID of the inventory item.
	 * @param quantityToReduce The amount to reduce.
	 * @param reason The reason for the stock reduction (e.g., "sale", "damaged").
	 * @param reference An optional reference (e.g., "SO-456").
	 * @param performedBy The user performing the action.
	 * @return The updated InventoryItem.
	 * @throws Exception if item not found, quantity is invalid, or insufficient stock.
	 */
	InventoryItem reduceStock(Integer itemId, double quantityToReduce, String reason, String reference, String performedBy) throws Exception;

	/**
	 * Adjusts the stock quantity of an inventory item to a specific new level.
	 * This can be used for corrections (e.g., after a physical count).
	 * @param itemId The ID of the inventory item.
	 * @param newQuantity The new quantity to set.
	 * @param reason The reason for the adjustment (e.g., "physical count adjustment").
	 * @param performedBy The user performing the action.
	 * @return The updated InventoryItem.
	 * @throws Exception if item not found or new quantity is invalid.
	 */
	InventoryItem adjustStock(Integer itemId, double newQuantity, String reason, String performedBy) throws Exception;

	/**
	 * Retrieves inventory items that are below their minimum stock threshold.
	 * @return An array of InventoryItem objects that are low in stock.
	 * @throws Exception
	 */
	InventoryItem[] getLowStockItems() throws Exception;
}