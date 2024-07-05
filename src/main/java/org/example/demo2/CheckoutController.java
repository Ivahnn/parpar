package org.example.demo2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CheckoutController implements Initializable {

    @FXML
    private TableView<Itinerary> itineraryTable;

    @FXML
    private TableColumn<Itinerary, String> locationColumn;

    @FXML
    private TableColumn<Itinerary, String> hotelColumn;

    @FXML
    private TableColumn<Itinerary, String> attractionColumn;

    @FXML
    private TableColumn<Itinerary, String> activityColumn;

    @FXML
    private TableColumn<Itinerary, LocalDate> dayColumn;

    private String username;

    public void setUsername(String username) {
        this.username = username;
        loadItineraries();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        hotelColumn.setCellValueFactory(new PropertyValueFactory<>("hotel"));
        attractionColumn.setCellValueFactory(new PropertyValueFactory<>("topAttraction"));
        activityColumn.setCellValueFactory(new PropertyValueFactory<>("activity"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
    }

    private void loadItineraries() {
        ObservableList<Itinerary> itineraries = FXCollections.observableArrayList();

        String sql = "SELECT location, hotel, topAttraction, activity, day FROM Itinerary " +
                "WHERE userId = (SELECT userId FROM Users WHERE username = ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Itinerary itinerary = new Itinerary(
                        rs.getString("location"),
                        rs.getString("hotel"),
                        rs.getString("topAttraction"),
                        rs.getString("activity"),
                        rs.getDate("day").toLocalDate()
                );
                itineraries.add(itinerary);
            }

            itineraryTable.setItems(itineraries);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("destination.fxml"));
            Parent root = loader.load();
            destination destinationController = loader.getController();
            destinationController.setUsername(username);
            Stage stage = (Stage) itineraryTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/ParDist";
        String dbUser = "root";
        String dbPassword = "";
        return DriverManager.getConnection(url, dbUser, dbPassword);
    }
}