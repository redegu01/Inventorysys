package com.degu.inventory; // Changed package

// Removed unused import: com.degu.inventory.model.InventoryItem; // Changed import
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType; // Import ButtonType
import javafx.fxml.FXML; // Added import for FXML

public class DeleteInventoryItemController extends GenericInventoryItemController {

	@FXML // Add FXML if this is linked in FXML
	public ImageView imgEcommerce;

	private final InventoryApiClient apiClient = new InventoryApiClient(); // Made final

	@Override
	protected void init() { // Changed to protected
		// setFields will use 'this.selectedItem' which ManageInventoryItemsController sets before init() is called
		setFields("Delete");
		enableFields(false); // Fields should be read-only for delete confirmation
	}

	@FXML // Add FXML if this is linked in FXML
	public void onSubmit(ActionEvent actionEvent) {
		// Use the inherited 'selectedItem' directly
		if (this.selectedItem == null) { // Changed from GenericInventoryItemController.selectedItem
			showErrorDialog("Deletion Error", "No item selected for deletion.");
			return;
		}

		// Add a confirmation dialog before deleting
		Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmationAlert.setTitle("Confirm Deletion");
		confirmationAlert.setHeaderText("Delete Inventory Item?");
		// Use the inherited 'selectedItem' directly
		confirmationAlert.setContentText("Are you sure you want to delete the item: " + this.selectedItem.getName() + " (ID: " + this.selectedItem.getId() + ")?"); // Changed from GenericInventoryItemController.selectedItem

		confirmationAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) { // Use ButtonType.OK
				try {
					// Use the inherited 'selectedItem' directly
					int itemIdToDelete = this.selectedItem.getId(); // Changed from GenericInventoryItemController.selectedItem
					apiClient.deleteInventoryItem(itemIdToDelete);

					Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
					successAlert.setTitle("Deletion Successful");
					successAlert.setHeaderText(null);
					successAlert.setContentText("Inventory item deleted successfully!");
					successAlert.showAndWait();

					clearFields("Delete");
					this.setSelectedItem(null); // CRITICAL: Clear the selected item after deletion

					// The manageInventoryItemsController.refresh() is responsible for updating the list
					// and clearing the selection/detail fields on the main screen.
					if (manageInventoryItemsController != null) {
						manageInventoryItemsController.refresh();
					}

					// Navigate back to the manage scene
					Node node = ((Node) (actionEvent.getSource()));
					Window window = node.getScene().getWindow();
					window.hide();
					stage.setTitle("Manage Inventory Items");
					stage.setScene(manageScene);
					stage.show();

				} catch (NumberFormatException e) {
					showErrorDialog("Deletion Error", "Invalid ID format: " + e.getMessage());
				} catch (Exception e) {
					showErrorDialog("Error encountered deleting inventory item", e.getMessage());
					e.printStackTrace();
				}
			}
		});
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