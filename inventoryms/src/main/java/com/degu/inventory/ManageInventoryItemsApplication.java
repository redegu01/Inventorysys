package com.degu.inventory;
import javafx.application.Application;
import com.degu.inventory.model.InventoryItem;

public class ManageInventoryItemsApplication {
	public static void main(String[] args) {
		Application.launch(ManageInventoryItemsJFXApp.class, args);
		System.out.println("InventoryItem loaded from: " + InventoryItem.class.getProtectionDomain().getCodeSource().getLocation());
	}
}