package org.example.demo2;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckoutController {
    @FXML
    private TableView<Itinerary> itineraryTable;
    @FXML
    private TableColumn<Itinerary, String> hotelColumn;
    @FXML
    private TableColumn<Itinerary, String> topAttractionColumn;
    @FXML
    private TableColumn<Itinerary, String> activityColumn;
    @FXML
    private TableColumn<Itinerary, String> breakfastColumn;
    @FXML
    private TableColumn<Itinerary, String> lunchColumn;
    @FXML
    private TableColumn<Itinerary, String> dinnerColumn;
    @FXML
    private TableColumn<Itinerary, String> durationColumn;

    private String username;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @FXML
    public void initialize() {
        hotelColumn.setCellValueFactory(new PropertyValueFactory<>("hotel"));
        topAttractionColumn.setCellValueFactory(new PropertyValueFactory<>("topAttraction"));
        activityColumn.setCellValueFactory(new PropertyValueFactory<>("activity"));
        breakfastColumn.setCellValueFactory(new PropertyValueFactory<>("breakfast"));
        lunchColumn.setCellValueFactory(new PropertyValueFactory<>("lunch"));
        dinnerColumn.setCellValueFactory(new PropertyValueFactory<>("dinner"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
    }

    public void setUsername(String username) {
        this.username = username;
        loadItineraries();
    }

    private void loadItineraries() {
        itineraryTable.getItems().clear();
        executorService.submit(() -> {
            try {
                ItineraryDAO itineraryDAO = new ItineraryDAO();
                List<Itinerary> itineraries = itineraryDAO.getItinerariesForUser(username);
                itineraryTable.getItems().addAll(itineraries); // This should work correctly if itineraries is a List<Itinerary>
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleDeleteButton(MouseEvent event) {
        Itinerary selectedItinerary = itineraryTable.getSelectionModel().getSelectedItem();
        if (selectedItinerary != null) {
            executorService.submit(() -> {
                try {
                    ItineraryDAO itineraryDAO = new ItineraryDAO();
                    itineraryDAO.deleteItinerary(selectedItinerary.getId());
                    loadItineraries();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    private void handleUpdateButton(MouseEvent event) {
        // Implement update functionality here
    }

    @FXML
    private void handlePrintPdfButton() {
        // Implement PDF printing functionality here
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
