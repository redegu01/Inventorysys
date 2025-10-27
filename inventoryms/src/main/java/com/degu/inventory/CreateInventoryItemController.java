package com.degu.inventory; // Changed package

import com.degu.inventory.model.InventoryItem; // Changed import

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert; // Added import for Alert
import javafx.fxml.FXML; // Added import for FXML

public class CreateInventoryItemController extends GenericInventoryItemController {

	@FXML // Add FXML if this is linked in FXML
	public ImageView imgEcommerce;

	private final InventoryApiClient apiClient = new InventoryApiClient(); // Made final

	@Override
	protected void init() { // Changed to protected
		clearFields("Create");
		enableFields(true);
		// When creating, ensure no item is selected (though Manage will pass null via setSelectedItem)
		this.setSelectedItem(null);
	}

	@FXML // Add FXML if this is linked in FXML
	public void onSubmit(ActionEvent actionEvent) {
		try {
			InventoryItem inventoryItem = toObject(false); // 'false' for create, always makes new object
			apiClient.createInventoryItem(inventoryItem)
					.ifPresent(newInventoryItem -> {
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
						successAlert.setContentText("Inventory item created successfully!");
						successAlert.showAndWait();
					});
		} catch (IllegalArgumentException e) {
			// Error dialog already shown by toObject
		} catch (Exception e) {
			showErrorDialog("Error encountered creating inventory item", e.getMessage());
			e.printStackTrace(); // Print stack trace for debugging
		}
	}

	@Override
	public void onClose(ActionEvent actionEvent) {
		super.onClose(actionEvent);
		// Refresh the main screen in case user pressed cancel without creating,
		// to ensure any partial data entry doesn't confuse the view
		if (manageInventoryItemsController != null) {
			manageInventoryItemsController.refresh();
		}
	}
}