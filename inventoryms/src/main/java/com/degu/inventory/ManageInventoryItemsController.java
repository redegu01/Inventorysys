package com.degu.inventory;

import com.degu.inventory.model.InventoryItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Setter;

import java.util.Arrays;

public class ManageInventoryItemsController extends GenericInventoryItemController {

	@Setter
	Stage stage;

	@Setter
	Scene createViewScene;

	@Setter
	Scene editViewScene;

	@Setter
	Scene deleteViewScene;

	@FXML
	public ImageView ecommerceImage;

	@FXML
	public Button btnCreate;

	@FXML
	public Button btnEdit;

	@FXML
	public Button btnDelete;

	@FXML
	public Button btnClose;

	@FXML
	public Button imageButton;


	@FXML
	private ListView<InventoryItem> lvEcommerces;

	private final InventoryApiClient apiClient = new InventoryApiClient();


	public void refresh() {
		try {
			InventoryItem[] inventoryItems = apiClient.getAllInventoryItems().orElse(new InventoryItem[0]);

			lvEcommerces.getItems().clear();
			lvEcommerces.getItems().addAll(Arrays.asList(inventoryItems));

			lvEcommerces.getSelectionModel().clearSelection();
			this.setSelectedItem(null);
			clearFields("Manage");
			enableFields(false);
		} catch (Exception e) {
			showErrorDialog("Refresh Error", "Failed to load inventory items: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	protected void init() {
		System.out.println("Invoked from ManageInventoryItemsController init");
		try {
			refresh();
			lvEcommerces.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
				if (newSelection != null) {
					this.setSelectedItem(newSelection);
					super.setFields("Manage");
					enableFields(false);
				} else {
					this.setSelectedItem(null);
					clearFields("Manage");
				}
			});

		} catch (Exception e) {
			showErrorDialog("Initialization Error: ", e.getMessage());
		}
	}

	public void onAction(MouseEvent mouseEvent) {
		this.setSelectedItem(lvEcommerces.getSelectionModel().getSelectedItem());
		if (this.selectedItem == null) {
			clearFields("Manage");
			return;
		}
		setFields("Manage");
		enableFields(false);
	}

	public void onCreate(ActionEvent actionEvent) throws Exception {
		Node node = ((Node) (actionEvent.getSource()));
		Scene currentScene = node.getScene();
		Window window = currentScene.getWindow();
		window.hide();

		if (createViewScene == null) {
			// Changed getResource path to align with common resource structure within the package
			FXMLLoader fxmlLoader = new FXMLLoader(ManageInventoryItemsJFXApp.class.getResource("/com/degu/inventory/create-ecom-view.fxml"));
			Parent root = fxmlLoader.load();
			CreateInventoryItemController controller = fxmlLoader.getController();
			controller.setStage(stage);
			createViewScene = new Scene(root, 650, 550);
			controller.setManageInventoryItemsController(this); // Pass THIS controller instance
			controller.setManageScene(manageScene);
			controller.setSplashScene(splashScene);
			createViewScene.setUserData(controller); // Store controller with scene for later access
		}
		// Ensure the create controller's fields are cleared before showing
		CreateInventoryItemController createController = (CreateInventoryItemController) createViewScene.getUserData();
		createController.setSelectedItem(null); // Crucial: No item selected for creation
		createController.init(); // Call init to ensure fields are cleared/prepared for create

		stage.setTitle("Create Inventory Item");
		stage.setScene(createViewScene);
		stage.show();
	}

	public void onEdit(ActionEvent actionEvent) throws Exception {
		// Use the inherited selectedItem
		if (this.selectedItem == null) { // Changed from GenericInventoryItemController.selectedItem
			showErrorDialog("No Item Selected", "Please select an inventory item from the list to edit.");
			return;
		}

		Node node = ((Node) (actionEvent.getSource()));
		Scene currentScene = node.getScene();
		Window window = currentScene.getWindow();
		window.hide();

		if (editViewScene == null) {
			// Changed getResource path to align with common resource structure within the package
			FXMLLoader fxmlLoader = new FXMLLoader(ManageInventoryItemsJFXApp.class.getResource("/com/degu/inventory/edit-ecom-view.fxml"));
			Parent root = fxmlLoader.load();
			EditInventoryItemController controller = fxmlLoader.getController();
			controller.setStage(stage);
			editViewScene = new Scene(root, 650, 550);
			controller.setManageInventoryItemsController(this); // Pass THIS controller instance
			controller.setManageScene(manageScene);
			controller.setSplashScene(splashScene);
			editViewScene.setUserData(controller); // Store controller with scene for later access
		}

		// --- CRITICAL: Pass the actual selected item to the edit controller instance ---
		EditInventoryItemController editController = (EditInventoryItemController) editViewScene.getUserData();
		editController.setSelectedItem(this.selectedItem); // Pass the non-static selected item
		editController.init(); // Call init() to populate fields using the newly set selectedItem

		stage.setTitle("Edit Inventory Item");
		stage.setScene(editViewScene);
		stage.show();
	}

	public void onDelete(ActionEvent actionEvent) throws Exception {
		// Use the inherited selectedItem
		if (this.selectedItem == null) { // Changed from GenericInventoryItemController.selectedItem
			showErrorDialog("No Item Selected", "Please select an inventory item from the list to delete.");
			return;
		}

		Node node = ((Node) (actionEvent.getSource()));
		Scene currentScene = node.getScene();
		Window window = currentScene.getWindow();
		window.hide();

		if (deleteViewScene == null) {
			// Changed getResource path to align with common resource structure within the package
			FXMLLoader fxmlLoader = new FXMLLoader(ManageInventoryItemsJFXApp.class.getResource("/com/degu/inventory/delete-ecom-view.fxml"));
			Parent root = fxmlLoader.load();
			DeleteInventoryItemController controller = fxmlLoader.getController();
			controller.setStage(stage);
			deleteViewScene = new Scene(root, 650, 550);
			controller.setManageInventoryItemsController(this); // Pass THIS controller instance
			controller.setManageScene(manageScene);
			controller.setSplashScene(splashScene);
			deleteViewScene.setUserData(controller); // Store controller with scene for later access
		}

		// --- CRITICAL: Pass the actual selected item to the delete controller instance ---
		DeleteInventoryItemController deleteController = (DeleteInventoryItemController) deleteViewScene.getUserData();
		deleteController.setSelectedItem(this.selectedItem); // Pass the non-static selected item
		deleteController.init(); // Call init() to populate fields using the newly set selectedItem

		stage.setTitle("Delete Inventory Item");
		stage.setScene(deleteViewScene);
		stage.show();
	}

	@Override
	public void onClose(ActionEvent actionEvent) {
		super.onClose(actionEvent);
	}
}