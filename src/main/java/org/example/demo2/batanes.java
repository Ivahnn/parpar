package org.example.demo2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class batanes implements Initializable {

    @FXML
    private ComboBox<String> hotelComboBox;
    @FXML
    private ComboBox<String> topattractionComboBox;
    @FXML
    private ComboBox<String> activitiesComboBox;
    @FXML
    private ComboBox<String> breakfastComboBox;
    @FXML
    private ComboBox<String> lunchComboBox;
    @FXML
    private ComboBox<String> dinnerComboBox;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add items to the hotel ComboBox
        hotelComboBox.getItems().addAll("Fundacion Pacita Batanes Nature Lodge", "Batanes Resort Hotel", "Amboy Hometel");

        // Add items to the top attraction ComboBox
        topattractionComboBox.getItems().addAll("Vayang Rolling Hills", "Basco Lighthouse","Nakabuang ArchZ");

        activitiesComboBox.getItems().addAll("Biking around Basco", "Hiking to Mount Iraya","Island Hopping to Sabtang Island");

        breakfastComboBox.getItems().addAll("Ivatan Platter", "Daing na Dibang","Tapa with Fried Rice");

        lunchComboBox.getItems().addAll("Uvud Balls", "Lunis (Ivatan Adobo)","Coconut Crab");

        dinnerComboBox.getItems().addAll("Grilled Flying Fish", "Yellow Rice with Local Herbs","Beef Stew with Vegetables");

    }
}