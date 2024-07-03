package org.example.demo2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class destination {

    public String username;
    public void setUsername(String username){
        this.username = username;
    }

    @FXML
    private ImageView backButton;

    @FXML
    private ImageView batanesImage;

    @FXML
    private ImageView baguioImage;

    @FXML
    private ImageView batangasImage;

    @FXML
    private ImageView boholImage;

    @FXML
    private ImageView cebuImage;

    @FXML
    private ImageView palawanImage;

    @FXML
    private ImageView siargaoImage;

    @FXML
    private ImageView zambalesImage;



    @FXML
    public void initialize() {
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> goBack());
    }

    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home-page.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBatanesClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("batanes.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) batanesImage.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBaguioClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("baguio.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) baguioImage.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBatangasClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("batangas.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) batangasImage.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBoholClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("bohol.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) boholImage.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCebuClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cebu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) cebuImage.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePalawanClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("palawan.fxml"));
            Parent root = loader.load();

            palawan palawan = loader.getController();

            palawan.setUsername(username);

            Stage stage = (Stage) palawanImage.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSiargaoClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("siargao.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) siargaoImage.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleZambalesClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("zambales.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) zambalesImage.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
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
