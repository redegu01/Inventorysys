package com.degu.inventory.model;

import lombok.Data;
import java.util.Date;

@Data
public class InventoryItem {
	private int id;
	private String name;
	private String description;
	private int productId;
	private String productName;
	private int categoryId;
	private String categoryName;
	private int statusId;
	private String statusName;
	private double quantityAvailable;
	private double minimumStock;
	private double maximumStock;
	private int warehouseId;
	private String warehouseName;
	private double unitCost;
	private String supplier;
	private String sku;

	private Date lastUpdated;
	private Date created;

	@Override
	public String toString() {
		return name;
	}
}
