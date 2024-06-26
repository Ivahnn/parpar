package org.example.demo2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

public class ThirdPageController {

    @FXML
    private ImageView imageView;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;

    @FXML
    private Label planNowLabel;

    private final Image[] images = {
            new Image("file:src/main/resources/images/batanes.png"),
            new Image("file:src/main/resources/images/palawan.png"),
            new Image("file:src/main/resources/images/baguio.png"),
            new Image("file:src/main/resources/images/Batangas.png"),
            new Image("file:src/main/resources/images/Bohol.png"),
            new Image("file:src/main/resources/images/Siargao.png"),
    };
    private int currentIndex = 0;

    @FXML
    public void initialize() {
        if (images.length > 0) {
            imageView.setImage(images[0]);  // Initialize the ImageView with the first image
        }

        // Set up a timeline to change images every 3 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> nextImage()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void nextImage() {
        currentIndex = (currentIndex + 1) % images.length;
        imageView.setImage(images[currentIndex]);
    }

    @FXML
    private void handleNextButton() {
        nextImage();
    }

    @FXML
    private void handlePreviousButton() {
        currentIndex = (currentIndex - 1 + images.length) % images.length;
        imageView.setImage(images[currentIndex]);
    }

    @FXML
    private void handleLabelClick(javafx.scene.input.MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("plan-now.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void handleDestinationLabelClick(javafx.scene.input.MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("destination.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void handleloginLabelClick(javafx.scene.input.MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("second-page.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void handleaboutLabelClick(javafx.scene.input.MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("about.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }







}

