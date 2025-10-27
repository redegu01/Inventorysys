package com.degu.inventory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class ManageInventoryItemsJFXApp extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		System.out.println("Inventory Management JFX App:start ");
		FXMLLoader fxmlLoader = new FXMLLoader(ManageInventoryItemsJFXApp.class.getResource("splash-view.fxml"));
		Parent root = (Parent) fxmlLoader.load();
		SplashViewController splashViewController = fxmlLoader.getController();
		splashViewController.setStage(stage);

		Scene scene = new Scene(root, 420, 420);
		splashViewController.setSplashScene(scene);

		stage.setTitle("Inventory Management System");
		stage.setScene(scene);
		stage.show();
	}
}