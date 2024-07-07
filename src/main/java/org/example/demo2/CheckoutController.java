package org.example.demo2;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckoutController implements Initializable {

    @FXML
    private TableView<Itinerary> itineraryTable;

    @FXML
    private ImageView backButton;

    @FXML
    private TableColumn<Itinerary, String> locationColumn;

    @FXML
    private TableColumn<Itinerary, String> hotelColumn;

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
        activityColumn.setCellValueFactory(new PropertyValueFactory<>("activitySummary"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> goBack());
    }

    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home-page.fxml"));
            Parent root = loader.load();

            HomePageController homepageController = loader.getController();
            homepageController.setUsername(username); // Pass the username to the Homepage controller

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadItineraries() {
        ObservableList<Itinerary> itineraries = FXCollections.observableArrayList();

        String sql = "SELECT i.id, i.userId, i.location, i.hotel, i.day, a.id AS activity_id, a.name, a.time, a.active " +
                "FROM itinerary i " +
                "LEFT JOIN activity a ON i.id = a.itinerary_id " +
                "WHERE i.userId = (SELECT userId FROM users WHERE username = ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            Itinerary currentItinerary = null;
            List<Activity> activities = new ArrayList<>();

            while (rs.next()) {
                int itineraryId = rs.getInt("id");
                if (currentItinerary == null || currentItinerary.getId() != itineraryId) {
                    if (currentItinerary != null) {
                        currentItinerary.setActivities(activities);
                        itineraries.add(currentItinerary);
                        activities = new ArrayList<>();
                    }
                    currentItinerary = new Itinerary(
                            itineraryId,
                            rs.getInt("userId"),
                            rs.getString("location"),
                            rs.getString("hotel"),
                            rs.getDate("day").toLocalDate(),
                            activities
                    );
                }
                if (rs.getString("activity_id") != null) {
                    activities.add(new Activity(
                            rs.getString("name"),
                            rs.getString("time"),
                            rs.getBoolean("active")
                    ));
                }
            }
            if (currentItinerary != null) {
                currentItinerary.setActivities(activities);
                itineraries.add(currentItinerary);
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

        String deleteActivitiesSql = "DELETE FROM activity WHERE itinerary_id = ?";
        String deleteItinerarySql = "DELETE FROM itinerary WHERE id = ? AND userId = (SELECT userId FROM users WHERE username = ?)";

        // Add logging to see what we are trying to delete
        System.out.println("Deleting itinerary with ID: " + selectedItinerary.getId() + " for user: " + username);

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement deleteActivitiesStmt = conn.prepareStatement(deleteActivitiesSql);
                 PreparedStatement deleteItineraryStmt = conn.prepareStatement(deleteItinerarySql)) {

                // Delete dependent activities
                deleteActivitiesStmt.setInt(1, selectedItinerary.getId());
                deleteActivitiesStmt.executeUpdate();
                System.out.println("Associated activities deleted.");

                // Delete the itinerary
                deleteItineraryStmt.setInt(1, selectedItinerary.getId());
                deleteItineraryStmt.setString(2, username);
                int affectedRows = deleteItineraryStmt.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Itinerary deleted successfully.");
                    itineraryTable.getItems().remove(selectedItinerary);
                    showAlert("Success", "Itinerary deleted successfully.");
                    conn.commit(); // Commit transaction
                } else {
                    System.out.println("No rows affected. Deletion failed.");
                    showAlert("Error", "Failed to delete itinerary. Please ensure you are the owner of the itinerary.");
                    conn.rollback(); // Rollback transaction
                }

            } catch (SQLException e) {
                System.err.println("SQL error occurred while deleting the itinerary: " + e.getMessage());
                showAlert("Error", "An error occurred while deleting the itinerary: " + e.getMessage());
                conn.rollback(); // Rollback transaction
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while establishing the database connection: " + e.getMessage());
            showAlert("Error", "An error occurred while establishing the database connection: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
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
                String pdfPath = "C:/Users/Ivahnn/Downloads/Itinerary_" + itinerary.getId() + ".pdf";
                PdfWriter writer = new PdfWriter(pdfPath);
                PdfDocument pdfDoc = new PdfDocument(writer);
                pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new HeaderEventHandler(username));

                Document document = new Document(pdfDoc);

                document.add(new Paragraph(""));
                document.add(new Paragraph(""));
                document.add(new Paragraph(""));
                document.add(new Paragraph(""));
                document.add(new Paragraph(""));
                document.add(new Paragraph("Travel Plan"));
                document.add(new Paragraph("Location: " + itinerary.getLocation()));
                document.add(new Paragraph("Traveler's Name: " + username));
                document.add(new Paragraph("Day: " + itinerary.getDay()));
                document.add(new Paragraph("Hotel: " + itinerary.getHotel()));
                document.add(new Paragraph("Activities: " + itinerary.getActivitySummary()));

                document.close();
                System.out.println("PDF created at: " + pdfPath);

                openPDFInViewer(pdfPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void printAllItineraries(ObservableList<Itinerary> itineraries) {
        executorService.submit(() -> {
            try {
                String pdfPath = "C:/Users/Ivahnn/Downloads/All_Itineraries.pdf";
                PdfWriter writer = new PdfWriter(pdfPath);
                PdfDocument pdfDoc = new PdfDocument(writer);
                pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new HeaderEventHandler(username));

                Document document = new Document(pdfDoc);

                for (Itinerary itinerary : itineraries) {
                    document.add(new Paragraph(""));
                    document.add(new Paragraph(""));
                    document.add(new Paragraph(""));
                    document.add(new Paragraph(""));
                    document.add(new Paragraph(""));
                    document.add(new Paragraph("Travel Plan"));
                    document.add(new Paragraph("Location: " + itinerary.getLocation()));
                    document.add(new Paragraph("Traveler's Name: " + username));
                    document.add(new Paragraph("Day: " + itinerary.getDay()));
                    document.add(new Paragraph("Hotel: " + itinerary.getHotel()));
                    document.add(new Paragraph("Activities: " + itinerary.getActivitySummary()));
                    document.add(new Paragraph("\n"));
                }

                document.close();
                System.out.println("PDF created at: " + pdfPath);

                openPDFInViewer(pdfPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void openPDFInViewer(String pdfPath) {
        try {
            File pdfFile = new File(pdfPath);
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    showAlert("Error", "Awt Desktop is not supported!");
                }
            } else {
                showAlert("Error", "File does not exist!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open PDF: " + e.getMessage());
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

    private static class HeaderEventHandler implements IEventHandler {
        private final String username;

        public HeaderEventHandler(String username) {
            this.username = username;
        }

        @Override

        public void handleEvent(Event event) {
            PdfDocumentEvent pdfEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = pdfEvent.getDocument();
            PdfPage page = pdfEvent.getPage();

            PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
            try {
                String imagePath = "file:src/main/resources/images/header2.png";
                ImageData imageData = ImageDataFactory.create(imagePath);
                canvas.addImage(imageData, 40, page.getPageSize().getTop() - 100, imageData.getWidth(), false);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
