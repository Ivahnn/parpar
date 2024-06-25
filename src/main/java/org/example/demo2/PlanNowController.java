package org.example.demo2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class PlanNowController {

    @FXML
    private ImageView backButton;

    @FXML
    private TextField usernameField;

    @FXML
    private DatePicker durationPicker;

    @FXML
    private Button printPdfButton;

    @FXML
    private MediaView mediaView;


    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("second-page.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) backButton.getScene().getWindow();

            // Set the previous scene to the current stage
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


