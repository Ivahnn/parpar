package org.example.demo2;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class bohol implements Initializable {

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

    @FXML
    private TextField usernameField;
    private String username;
    public void setUsername(String username) {
        this.username = username;
        System.out.println("Username set to: " + username); // Add this debug statement
        if (usernameField != null) {
            usernameField.setText(username);
        }
    }

    @FXML
    private DatePicker durationField;

    @FXML
    private ImageView backButton; // Corrected annotation

    @FXML
    private MediaView mediaView;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playVideo();

        usernameField.setText(username); // Verify that username is not empty here
        System.out.println("Username field set to: " + usernameField.getText());

        // Initialize ComboBoxes asynchronously
        executorService.submit(() -> {
            hotelComboBox.getItems().addAll(
                    "The Bellevue Resort", "Amorita Resort", "Henann Resort Alona Beach", "Bohol Beach Club", "South Palms Resort Panglao"
            );
        });

        executorService.submit(() -> {
            topattractionComboBox.getItems().addAll(
                    "Chocolate Hills", "Tarsier Conservation Area", "Loboc River Cruise", "Hinagdanan Cave", "Panglao Island"
            );
        });

        executorService.submit(() -> {
            activitiesComboBox.getItems().addAll(
                    "Scuba Diving and Snorkeling", "Chocolate Hills ATV Tour", "Loboc River Stand-Up Paddleboarding", "Firefly Watching Tour", "Island Hopping"
            );
        });

        executorService.submit(() -> {
            breakfastComboBox.getItems().addAll(
                    "Sikwate and Puto Maya", "Budbud Kabog", "Binignit", "Chorizo de Bohol", "Tsokolate Tablea"
            );
        });

        executorService.submit(() -> {
            lunchComboBox.getItems().addAll(
                    "Chicken Halang-Halang", "Tinunuang Isda", "Pansit Boholano", "Sutukil", "Linabog"
            );
        });

        executorService.submit(() -> {
            dinnerComboBox.getItems().addAll(
                    "Lechon Boholano", "Kinilaw", "Inun-unan", "Nilubihang Kagang", "Grilled Squid"
            );
        });

        // Event handler for back button
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> goBack());
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/pardist";
        String dbUser = "root";
        String dbPassword = "";
        return DriverManager.getConnection(url, dbUser, dbPassword);
    }

    @FXML
    private void handlePrintPdfButton() {
        String location = "Bohol";
        String selectedHotel = hotelComboBox.getValue();
        String selectedAttraction = topattractionComboBox.getValue();
        String selectedActivity = activitiesComboBox.getValue();
        String selectedBreakfast = breakfastComboBox.getValue();
        String selectedLunch = lunchComboBox.getValue();
        String selectedDinner = dinnerComboBox.getValue();
        String username = usernameField.getText();
        String duration = (durationField.getValue() != null) ? durationField.getValue().toString() : "N/A";

        // Generate PDF in a separate thread
        executorService.submit(() -> {
            try {
                String pdfPath = "Travellers4.pdf";
                PdfWriter writer = new PdfWriter(pdfPath);
                com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
                Document document = new Document(pdfDoc);

                document.add(new Paragraph("Travel Plan"));
                document.add(new Paragraph("Location: " + location));
                document.add(new Paragraph("Traveler's Name: " + username));
                document.add(new Paragraph("Duration: " + duration));
                document.add(new Paragraph("Hotel: " + selectedHotel));
                document.add(new Paragraph("Top Attraction: " + selectedAttraction));
                document.add(new Paragraph("Activities: " + selectedActivity));
                document.add(new Paragraph("Breakfast: " + selectedBreakfast));
                document.add(new Paragraph("Lunch: " + selectedLunch));
                document.add(new Paragraph("Dinner: " + selectedDinner));

                document.close();
                System.out.println("PDF created at: " + pdfPath);
                insertItinerary(username, location, selectedHotel, selectedAttraction, selectedActivity,
                        selectedBreakfast, selectedLunch, selectedDinner, duration);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void insertItinerary(String username, String location, String hotel, String topAttraction, String activity,
                                 String breakfast, String lunch, String dinner, String duration) {
        String sql = "INSERT INTO itinerary (userId, location, hotel, topAttraction, activity, breakfast, lunch, dinner, day) " +
                "VALUES ((SELECT userId FROM users WHERE username = ?), ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, location);
            pstmt.setString(3, hotel);
            pstmt.setString(4, topAttraction);
            pstmt.setString(5, activity);
            pstmt.setString(6, breakfast);
            pstmt.setString(7, lunch);
            pstmt.setString(8, dinner);
            pstmt.setDate(9, java.sql.Date.valueOf(LocalDate.parse(duration))); // Convert duration to java.sql.Date

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Itinerary inserted successfully.");
            } else {
                System.out.println("Failed to insert itinerary.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to handle back button click
    private void goBack() {
        try {
            // Load the previous scene FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("destination.fxml"));
            Parent root = loader.load();
            destination destinationController = loader.getController();
            destinationController.setUsername(username); // Pass the username here
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void playVideo() {
        String videoPath = getClass().getResource("/images/BOHOL FRAME.mp4").toExternalForm();
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        });
        mediaPlayer.play();
    }




    // Shutdown the executor service when done to free up resources
    public void shutdown() {
        executorService.shutdown();
    }
}
