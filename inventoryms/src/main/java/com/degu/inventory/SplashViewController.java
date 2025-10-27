package com.degu.inventory;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Setter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class SplashViewController implements Initializable {

	@Setter
	Stage stage;
	@Setter
	Scene manageScene = null;
	@Setter
	Scene splashScene = null;

	public ImageView imgHug;
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
	}

	public void onClicked(ActionEvent actionEvent) throws IOException {
		System.out.println("SplashController :onClicked ");
		Node node = ((Node) (actionEvent.getSource()));
		Scene currentScene = node.getScene();
		Window window = currentScene.getWindow();
		window.hide();

		if (manageScene == null) {
			FXMLLoader fxmlLoader = new FXMLLoader(ManageInventoryItemsJFXApp.class.getResource("manage-ecom-view.fxml"));
			Parent root = (Parent) fxmlLoader.load();
			ManageInventoryItemsController controller = fxmlLoader.getController();
			controller.setStage(stage);
			manageScene = new Scene(root, 900, 700);

			controller.setSplashScene(splashScene);
			controller.setManageScene(manageScene);
		}
		stage.setTitle("Inventory Management System");
		stage.setScene(manageScene);
		stage.show();
	}
}