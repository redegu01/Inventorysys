package com.degu.inventory; // Changed package

import com.degu.inventory.model.InventoryItem; // Changed import
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert; // Added import for Alert
import javafx.fxml.FXML; // Added import for FXML

public class EditInventoryItemController extends GenericInventoryItemController {

	@FXML // Add FXML if this is linked in FXML
	public ImageView imgEcommerce;

	private final InventoryApiClient apiClient = new InventoryApiClient(); // Made final

	@Override
	protected void init() { // Changed to protected
		// setFields will use 'this.selectedItem' which ManageInventoryItemsController sets before init() is called
		setFields("Edit");
		enableFields(true);
	}

	@FXML // Add FXML if this is linked in FXML
	public void onSubmit(ActionEvent actionEvent) {
		try {
			InventoryItem inventoryItem = toObject(true); // 'true' for edit, updates existing object
			apiClient.updateInventoryItem(inventoryItem)
					.ifPresent(updatedInventoryItem -> {
						if (manageInventoryItemsController != null) {
							manageInventoryItemsController.refresh(); // Refresh parent list
						}
						Node node = ((Node) (actionEvent.getSource()));
						Window window = node.getScene().getWindow();
						window.hide();
						stage.setTitle("Manage Inventory Items");
						stage.setScene(manageScene);
						stage.show();
						// Optional: Show success message
						Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
						successAlert.setTitle("Success");
						successAlert.setHeaderText(null);
						successAlert.setContentText("Inventory item updated successfully!");
						successAlert.showAndWait();
					});
		} catch (IllegalArgumentException e) {
			// Error dialog already shown by toObject
		} catch (Exception e) {
			showErrorDialog("Error encountered updating inventory item", e.getMessage());
			e.printStackTrace(); // Print stack trace for debugging
		}
	}

	@Override
	public void onClose(ActionEvent actionEvent) {
		super.onClose(actionEvent);
		// Refresh the main screen when returning, in case user pressed cancel
		if (manageInventoryItemsController != null) {
			manageInventoryItemsController.refresh();
		}
	}
}