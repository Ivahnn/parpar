package org.example.demo2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
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

public class HomePageController {
   private String username;

   public void setUsername(String username) {
      this.username = username;
      usernameLabel.setText(username);
   }

   @FXML
   private ImageView imageView;

   @FXML
   private Label usernameLabel;

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
         FXMLLoader loader = new FXMLLoader(getClass().getResource("checkout.fxml"));
         Parent root = loader.load();
         CheckoutController checkoutController = loader.getController();
         checkoutController.setUsername(username);
         Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         stage.setScene(new Scene(root));
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @FXML
   public void handleDestinationLabelClick(javafx.scene.input.MouseEvent event) {
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("destination.fxml"));
         Parent root = loader.load();
         destination destinationController = loader.getController();
         destinationController.setUsername(username); // Pass the username here
         Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         stage.setScene(new Scene(root));
         stage.show();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @FXML
   public void handleaboutLabelClick(javafx.scene.input.MouseEvent event) {
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("about.fxml"));
         Parent root = loader.load();
         about aboutController = loader.getController();
         aboutController.setUsername(username); // Pass the username here
         Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         stage.setScene(new Scene(root));
         stage.show();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @FXML
   private void handleLogoutButtonClick(ActionEvent event) {
      try {
         Parent loginPage = FXMLLoader.load(getClass().getResource("login.fxml"));
         Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         stage.setScene(new Scene(loginPage));
         stage.setTitle("Login");
         stage.show();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
