package com.degu.inventory; // Changed package

import com.degu.inventory.model.Category; // Changed import
import com.degu.inventory.model.InventoryItem; // Changed import
import com.degu.inventory.model.Product; // Changed import
import com.degu.inventory.model.InventoryStatus; // Changed import
import com.degu.inventory.model.Warehouse; // Changed import

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Setter;
import javafx.util.StringConverter;
import java.net.URL;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.util.ResourceBundle;
import java.util.Arrays;
import javafx.scene.image.ImageView;
import java.util.Optional; // Added for .orElse()

// Make this class abstract as it's intended to be a base class
public abstract class GenericInventoryItemController implements Initializable {

	// Removed direct @Setters for child controllers.
	// ManageInventoryItemsController will set itself on child controllers.
	// @Setter CreateInventoryItemController createInventoryItemController;
	// @Setter DeleteInventoryItemController deleteInventoryItemController;
	// @Setter EditInventoryItemController editInventoryItemController;

	@Setter protected ManageInventoryItemsController manageInventoryItemsController; // Keep this setter

	@Setter protected Stage stage;
	@Setter protected Scene splashScene;
	@Setter protected Scene manageScene;

	// --- REMOVED THIS: @Setter public ListView<InventoryItem> lvEcommerces;
	// ListView belongs in ManageInventoryItemsController only.

	// --- CRITICAL CHANGE 1: Make selectedItem an instance variable (NOT static) ---
	protected InventoryItem selectedItem;

	// --- CRITICAL CHANGE 2: Add a public setter for selectedItem ---
	public void setSelectedItem(InventoryItem item) {
		this.selectedItem = item;
	}

	// --- FXML Fields - Add @FXML annotation for clarity ---
	@FXML protected TextField txtId;
	@FXML protected TextField txtName;
	@FXML protected TextField txtDescription;
	@FXML protected ComboBox<Product> cmbProduct;
	@FXML protected TextField txtProductName;
	@FXML protected ComboBox<Category> cmbCategory;
	@FXML protected TextField txtCategoryName;
	@FXML protected ComboBox<InventoryStatus> cmbStatus;
	@FXML protected TextField txtStatusName;
	@FXML protected TextField txtQuantityAvailable;
	@FXML protected ComboBox<Warehouse> cmbInventory;
	@FXML protected TextField txtInventoryName;
	@FXML protected TextField txtPrice;
	@FXML protected ImageView imgEcommerce;

	protected final InventoryApiClient apiClient = new InventoryApiClient();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			Product[] products = apiClient.getAllProducts().orElse(new Product[0]);
			cmbProduct.getItems().addAll(products);
			StringConverter<Product> productConverter = new StringConverter<Product>() {
				@Override public String toString(Product product) { return product != null ? product.getName() : ""; }
				@Override public Product fromString(String s) {
					return Arrays.stream(products).filter(p -> s.equals(p.getName())).findFirst().orElse(null);
				}
			};
			cmbProduct.setConverter(productConverter);

			Category[] categories = apiClient.getAllCategories().orElse(new Category[0]);
			cmbCategory.getItems().addAll(categories);
			StringConverter<Category> categoryConverter = new StringConverter<Category>() {
				@Override public String toString(Category category) { return category != null ? category.getName() : ""; }
				@Override public Category fromString(String s) {
					return Arrays.stream(categories).filter(c -> s.equals(c.getName())).findFirst().orElse(null);
				}
			};
			cmbCategory.setConverter(categoryConverter);

			InventoryStatus[] statuses = apiClient.getAllInventoryStatuses().orElse(new InventoryStatus[0]);
			cmbStatus.getItems().addAll(statuses);
			StringConverter<InventoryStatus> statusConverter = new StringConverter<InventoryStatus>() {
				@Override public String toString(InventoryStatus status) { return status != null ? status.getName() : ""; }
				@Override public InventoryStatus fromString(String s) {
					return Arrays.stream(statuses).filter(st -> s.equals(st.getName())).findFirst().orElse(null);
				}
			};
			cmbStatus.setConverter(statusConverter);

			Warehouse[] warehouses = apiClient.getAllWarehouses().orElse(new Warehouse[0]);
			cmbInventory.getItems().addAll(warehouses);
			StringConverter<Warehouse> warehouseConverter = new StringConverter<Warehouse>() {
				@Override public String toString(Warehouse warehouse) { return warehouse != null ? warehouse.getName() : ""; }
				@Override public Warehouse fromString(String s) {
					return Arrays.stream(warehouses).filter(w -> s.equals(w.getName())).findFirst().orElse(null);
				}
			};
			cmbInventory.setConverter(warehouseConverter);

			init();
		} catch (Exception e) {
			showErrorDialog("Initialization Error", "Failed to load data for dropdowns: " + e.getMessage());
			e.printStackTrace();
		}
	}

	protected abstract void init();

	protected InventoryItem toObject(boolean isEdit) {
		InventoryItem inventoryItem;
		if (isEdit && this.selectedItem != null) {
			inventoryItem = this.selectedItem;
		} else {
			inventoryItem = new InventoryItem();
		}
		try {
			if (isEdit) {
				if (txtId.getText() != null && !txtId.getText().isEmpty()) {
					inventoryItem.setId(Integer.parseInt(txtId.getText()));
				}
			}
			inventoryItem.setName(txtName.getText());
			inventoryItem.setDescription(txtDescription.getText());

			Product product = cmbProduct.getSelectionModel().getSelectedItem();
			if (product != null) {
				inventoryItem.setProductId(product.getId());
				inventoryItem.setProductName(product.getName());
			} else {
				throw new IllegalArgumentException("Product must be selected.");
			}

			Category category = cmbCategory.getSelectionModel().getSelectedItem();
			if (category != null) {
				inventoryItem.setCategoryId(category.getId());
				inventoryItem.setCategoryName(category.getName());
			} else {
				throw new IllegalArgumentException("Category must be selected.");
			}

			InventoryStatus status = cmbStatus.getSelectionModel().getSelectedItem();
			if (status != null) {
				inventoryItem.setStatusId(status.getId());
				inventoryItem.setStatusName(status.getName());
			} else {
				throw new IllegalArgumentException("Status must be selected.");
			}

			inventoryItem.setQuantityAvailable(Double.parseDouble(txtQuantityAvailable.getText()));

			Warehouse warehouse = cmbInventory.getSelectionModel().getSelectedItem();
			if (warehouse != null) {
				inventoryItem.setWarehouseId(warehouse.getId());
				inventoryItem.setWarehouseName(warehouse.getName());
			} else {
				throw new IllegalArgumentException("Warehouse must be selected.");
			}

			inventoryItem.setUnitCost(Double.parseDouble(txtPrice.getText()));
		} catch (NumberFormatException e) {
			showErrorDialog("Invalid Number Format", "Please enter valid numbers for Quantity, and Price."); // Removed ID from error msg
			throw e;
		} catch (IllegalArgumentException e) {
			showErrorDialog("Input Error", e.getMessage());
			throw e;
		} catch (Exception e) {
			showErrorDialog("Error converting to object", e.getMessage());
			throw e;
		}
		return inventoryItem;
	}

	protected void setFields(String action) {
		InventoryItem inventoryItem = this.selectedItem;
		if (inventoryItem == null) {
			clearFields(action);
			return;
		}

		txtId.setText(Integer.toString(inventoryItem.getId()));
		txtName.setText(inventoryItem.getName());
		txtDescription.setText(inventoryItem.getDescription());
		txtQuantityAvailable.setText(Double.toString(inventoryItem.getQuantityAvailable()));
		txtPrice.setText(Double.toString(inventoryItem.getUnitCost()));

		boolean isEditableMode = action.equals("Create") || action.equals("Edit");

		try {
			Product product = apiClient.getProduct(inventoryItem.getProductId()).orElse(null);
			if (isEditableMode) {
				cmbProduct.setVisible(true);
				txtProductName.setVisible(false);
				if (product != null) cmbProduct.getSelectionModel().select(product);
				else cmbProduct.getSelectionModel().clearSelection();
			} else {
				cmbProduct.setVisible(false);
				txtProductName.setVisible(true);
				txtProductName.setText(inventoryItem.getProductName() != null ? inventoryItem.getProductName() : "N/A");
			}
		} catch (Exception e) {
			showErrorDialog("Error", "Failed to retrieve product details: " + e.getMessage());
		}

		try {
			Category category = apiClient.getCategory(inventoryItem.getCategoryId()).orElse(null);
			if (isEditableMode) {
				cmbCategory.setVisible(true);
				txtCategoryName.setVisible(false);
				if (category != null) cmbCategory.getSelectionModel().select(category);
				else cmbCategory.getSelectionModel().clearSelection();
			} else {
				cmbCategory.setVisible(false);
				txtCategoryName.setVisible(true);
				txtCategoryName.setText(inventoryItem.getCategoryName() != null ? inventoryItem.getCategoryName() : "N/A");
			}
		} catch (Exception e) {
			showErrorDialog("Error", "Failed to retrieve category details: " + e.getMessage());
		}

		try {
			InventoryStatus status = apiClient.getInventoryStatus(inventoryItem.getStatusId()).orElse(null);
			if (isEditableMode) {
				cmbStatus.setVisible(true);
				txtStatusName.setVisible(false);
				if (status != null) cmbStatus.getSelectionModel().select(status);
				else cmbStatus.getSelectionModel().clearSelection();
			} else {
				cmbStatus.setVisible(false);
				txtStatusName.setVisible(true);
				txtStatusName.setText(inventoryItem.getStatusName() != null ? inventoryItem.getStatusName() : "N/A");
			}
		} catch (Exception e) {
			showErrorDialog("Error", "Failed to retrieve status details: " + e.getMessage());
		}

		try {
			Warehouse warehouse = apiClient.getWarehouse(inventoryItem.getWarehouseId()).orElse(null);
			if (isEditableMode) {
				cmbInventory.setVisible(true);
				txtInventoryName.setVisible(false);
				if (warehouse != null) cmbInventory.getSelectionModel().select(warehouse);
				else cmbInventory.getSelectionModel().clearSelection();
			} else {
				cmbInventory.setVisible(false);
				txtInventoryName.setVisible(true);
				txtInventoryName.setText(inventoryItem.getWarehouseName() != null ? inventoryItem.getWarehouseName() : "N/A");
			}
		} catch (Exception e) {
			showErrorDialog("Error", "Failed to retrieve warehouse details: " + e.getMessage());
		}
	}

	protected void clearFields(String action) {
		txtId.setText("");
		txtName.setText("");
		txtDescription.setText("");
		txtQuantityAvailable.setText("");
		txtPrice.setText("");

		cmbProduct.getSelectionModel().clearSelection();
		txtProductName.setText("");
		boolean isCreateOrEdit = action.equals("Create") || action.equals("Edit");
		cmbProduct.setVisible(isCreateOrEdit);
		txtProductName.setVisible(!isCreateOrEdit);

		cmbCategory.getSelectionModel().clearSelection();
		txtCategoryName.setText("");
		cmbCategory.setVisible(isCreateOrEdit);
		txtCategoryName.setVisible(!isCreateOrEdit);

		cmbStatus.getSelectionModel().clearSelection();
		txtStatusName.setText("");
		cmbStatus.setVisible(isCreateOrEdit);
		txtStatusName.setVisible(!isCreateOrEdit);

		cmbInventory.getSelectionModel().clearSelection();
		txtInventoryName.setText("");
		cmbInventory.setVisible(isCreateOrEdit);
		txtInventoryName.setVisible(!isCreateOrEdit);
	}

	protected void enableFields(boolean enable) {
		txtName.setEditable(enable);
		txtDescription.setEditable(enable);
		cmbProduct.setDisable(!enable);
		txtProductName.setEditable(enable);
		cmbCategory.setDisable(!enable);
		txtCategoryName.setEditable(enable);
		cmbStatus.setDisable(!enable);
		txtStatusName.setEditable(enable);
		txtQuantityAvailable.setEditable(enable);
		cmbInventory.setDisable(!enable);
		txtInventoryName.setEditable(enable);
		txtPrice.setEditable(enable);
	}

	public int getId() {
		if (txtId.getText() == null || txtId.getText().isEmpty()) {
			throw new NumberFormatException("ID field is empty or not set.");
		}
		return Integer.parseInt(txtId.getText());
	}

	protected void showErrorDialog(String message, String expandedMessage) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setHeaderText(message);
		TextArea textArea = new TextArea(expandedMessage);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		alert.getDialogPane().setExpandableContent(new ScrollPane(textArea));
		alert.showAndWait();
	}

	public void onBack(ActionEvent actionEvent) {
		Node node = ((Node) (actionEvent.getSource()));
		Window window = node.getScene().getWindow();
		window.hide();
		stage.setTitle("Manage Inventory Items");
		stage.setScene(manageScene);
		stage.show();
		if (manageInventoryItemsController != null) {
			manageInventoryItemsController.refresh();
		}
	}

	@FXML
	public void onClose(ActionEvent actionEvent) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Exit and lose changes?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			Platform.exit();
		}
	}
}