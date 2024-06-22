package org.example.demo2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class destination {
    @FXML
    private ImageView backButton;

    @FXML
    private ImageView batanesImage;

    @FXML
    public void initialize() {
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> goBack());
    }

    private void goBack() {
        try {
            // Load the previous scene FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("third-page.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) backButton.getScene().getWindow();

            // Set the previous scene to the current stage
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBatanesClick(MouseEvent event) {
        try {
            // Load the plan-now.fxml scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("batanes.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) batanesImage.getScene().getWindow();

            // Set the plan-now scene to the current stage
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void hoverEnter(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        imageView.setLayoutY(imageView.getLayoutY() - 10); // Move up by 10 pixels
    }

    @FXML
    private void hoverExit(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        imageView.setLayoutY(imageView.getLayoutY() + 10); // Move back to original position
    }
}
