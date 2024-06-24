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

    private MediaPlayer mediaPlayer;

    @FXML
    public void initialize() {
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> goBack());

        // Example media file, replace with your actual video file path
        String mediaFile = getClass().getResource("/org/example/demo2/palawan1.mp4").toString();
        Media media = new Media(new File(mediaFile).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        mediaPlayer.setAutoPlay(true); // Auto-play the video
    }

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

    // Define other event handlers and methods as needed
}
