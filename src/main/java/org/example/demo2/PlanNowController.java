// File name: PlanNowController.java

package org.example.demo2;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;

public class PlanNowController {
    @FXML
    private ImageView backButton;

    private javafx.scene.control.TextField usernameField;

    @FXML
    private javafx.scene.control.DatePicker durationPicker;

    @FXML
    public void initialize() {

        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> goBack());
    }

    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("third-page.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) backButton.getScene().getWindow();

            // Set the previous scene to the current stage
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Define other event handlers and methods as needed
}