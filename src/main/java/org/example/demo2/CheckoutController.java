package org.example.demo2;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private final ExecutorService executorService = Executors.newCachedThreadPool();

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

        String sql = "SELECT id, location, hotel, topAttraction, activity, breakfast, lunch, dinner, day FROM itinerary " +
                "WHERE userId = (SELECT userId FROM users WHERE username = ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Itinerary itinerary = new Itinerary(
                        rs.getInt("id"),
                        rs.getString("location"),
                        rs.getString("hotel"),
                        rs.getString("topAttraction"),
                        rs.getString("activity"),
                        rs.getString("breakfast"),
                        rs.getString("lunch"),
                        rs.getString("dinner"),
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
    private void handleDeleteButton() {
        Itinerary selectedItinerary = itineraryTable.getSelectionModel().getSelectedItem();
        if (selectedItinerary == null) {
            showAlert("No Selection", "Please select an itinerary to delete.");
            return;
        }

        String sql = "DELETE FROM itinerary WHERE id = ? AND userId = (SELECT userId FROM users WHERE username = ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, selectedItinerary.getId());
            pstmt.setString(2, username);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                itineraryTable.getItems().remove(selectedItinerary);
                showAlert("Success", "Itinerary deleted successfully.");
            } else {
                showAlert("Error", "Failed to delete itinerary.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while deleting the itinerary.");
        }
    }

    @FXML
    private void handlePrintSelectedButton() {
        Itinerary selectedItinerary = itineraryTable.getSelectionModel().getSelectedItem();
        if (selectedItinerary == null) {
            showAlert("No Selection", "Please select an itinerary to print.");
            return;
        }

        printItinerary(selectedItinerary);
    }

    @FXML
    private void handlePrintAllButton() {
        ObservableList<Itinerary> itineraries = itineraryTable.getItems();
        if (itineraries.isEmpty()) {
            showAlert("No Itineraries", "There are no itineraries to print.");
            return;
        }

        printAllItineraries(itineraries);
    }

    private void printItinerary(Itinerary itinerary) {
        executorService.submit(() -> {
            try {
                String pdfPath = "Itinerary_" + itinerary.getId() + ".pdf";
                PdfWriter writer = new PdfWriter(pdfPath);
                com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
                Document document = new Document(pdfDoc);

                document.add(new Paragraph("Travel Plan"));
                document.add(new Paragraph("Location: " + itinerary.getLocation()));
                document.add(new Paragraph("Traveler's Name: " + username));
                document.add(new Paragraph("Day: " + itinerary.getDay()));
                document.add(new Paragraph("Hotel: " + itinerary.getHotel()));
                document.add(new Paragraph("Top Attraction: " + itinerary.getTopAttraction()));
                document.add(new Paragraph("Activities: " + itinerary.getActivity()));
                document.add(new Paragraph("Breakfast: " + itinerary.getBreakfast()));
                document.add(new Paragraph("Lunch: " + itinerary.getLunch()));
                document.add(new Paragraph("Dinner: " + itinerary.getDinner()));

                document.close();
                System.out.println("PDF created at: " + pdfPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void printAllItineraries(ObservableList<Itinerary> itineraries) {
        executorService.submit(() -> {
            try {
                String pdfPath = "All_Itineraries.pdf";
                PdfWriter writer = new PdfWriter(pdfPath);
                com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
                Document document = new Document(pdfDoc);

                for (Itinerary itinerary : itineraries) {
                    document.add(new Paragraph("Travel Plan"));
                    document.add(new Paragraph("Location: " + itinerary.getLocation()));
                    document.add(new Paragraph("Traveler's Name: " + username));
                    document.add(new Paragraph("Day: " + itinerary.getDay()));
                    document.add(new Paragraph("Hotel: " + itinerary.getHotel()));
                    document.add(new Paragraph("Top Attraction: " + itinerary.getTopAttraction()));
                    document.add(new Paragraph("Activities: " + itinerary.getActivity()));
                    document.add(new Paragraph("Breakfast: " + itinerary.getBreakfast()));
                    document.add(new Paragraph("Lunch: " + itinerary.getLunch()));
                    document.add(new Paragraph("Dinner: " + itinerary.getDinner()));
                    document.add(new Paragraph("\n"));
                }

                document.close();
                System.out.println("PDF created at: " + pdfPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
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
        String url = "jdbc:mysql://localhost:3306/pardist";
        String dbUser = "root";
        String dbPassword = "";
        return DriverManager.getConnection(url, dbUser, dbPassword);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void shutdown() {
        executorService.shutdown();
    }
}