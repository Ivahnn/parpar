package org.example.demo2;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class PalawanTest implements Initializable {

    @FXML
    private ComboBox<String> hotelComboBox;

    @FXML
    private TableView<Activity> activitiesTable;

    @FXML
    private TableColumn<Activity, String> activityColumn;

    @FXML
    private TableColumn<Activity, String> timeColumn;

    @FXML
    private TextField customActivityField;

    @FXML
    private TextField customTimeField;

    @FXML
    private DatePicker arrivalDatePicker;

    @FXML
    private TextField arrivalTimeField;

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
    private MediaView mediaView;

    @FXML
    private ImageView backButton;

    @FXML
    private Button addArrivalButton;

    @FXML
    private Button resetButton; // Add this field

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final ObservableList<Activity> activities = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playVideo();

        usernameField.setText(username);
        System.out.println("Username field set to: " + usernameField.getText());

        initializeActivitiesTable();
        addArrivalButton.setDisable(true);
        activitiesTable.setDisable(true);
        initializeHotelComboBox();

        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> goBack());
        resetButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> resetFields()); // Add this handler
    }

    private void initializeHotelComboBox() {
        executorService.submit(() -> {
            hotelComboBox.getItems().addAll(
                    "Amanpulo", "El Nido Resorts Pangulasian Island", "Two Seasons Coron Island Resort & Spa",
                    "Club Paradise Palawan", "Lagen Island Resort"
            );
        });
    }

    private void initializeActivitiesTable() {
        activityColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Make time column editable
        timeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        timeColumn.setOnEditCommit(event -> {
            Activity activity = event.getRowValue();
            if (isValidTimeFormat(event.getNewValue())) {
                activity.setTime(event.getNewValue());
                activity.setActive(true);
                sortActivitiesByTime(); // Sort immediately after setting time
            } else {
                showAlert("Invalid Time", "Please enter a valid time in the format 7:00 - 8:00 A.M or P.M");
                activity.setTime("");
                activity.setActive(false);
            }
            activitiesTable.refresh();
        });

        activities.addAll(
                new Activity("Island Hopping in El Nido", "", false),
                new Activity("Underground River Tour", "", false),
                new Activity("Snorkeling and Diving in Coron", "", false),
                new Activity("Kayaking in Big Lagoon", "", false),
                new Activity("Swimming in Kayangan Lake", "", false)
        );

        activitiesTable.setItems(activities);

        // Enable drag and drop
        activitiesTable.setRowFactory(tv -> {
            TableRow<Activity> row = new TableRow<>();
            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(DataFormat.PLAIN_TEXT, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(DataFormat.PLAIN_TEXT)) {
                    event.acceptTransferModes(TransferMode.MOVE);
                    event.consume();
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(DataFormat.PLAIN_TEXT)) {
                    int draggedIndex = Integer.parseInt((String) db.getContent(DataFormat.PLAIN_TEXT));
                    Activity draggedActivity = activities.remove(draggedIndex);
                    int dropIndex = row.isEmpty() ? activities.size() : row.getIndex();
                    activities.add(dropIndex, draggedActivity);
                    event.setDropCompleted(true);
                    activitiesTable.getSelectionModel().select(dropIndex);
                    event.consume();
                }
            });

            return row;
        });
    }

    @FXML
    private void handleAddArrivalActivity() {
        LocalDate arrivalDate = arrivalDatePicker.getValue();
        String arrivalTime = arrivalTimeField.getText();

        if (arrivalDate == null || arrivalTime.isEmpty()) {
            showAlert("Missing Information", "Please select an arrival date and time.");
            return;
        }

        if (!isValidTimeFormat(arrivalTime)) {
            showAlert("Invalid Time", "Please enter a valid time in the format 7:00 - 8:00 A.M or P.M");
            return;
        }

        Activity arrivalActivity = new Activity("Arrival", arrivalTime, true);
        activities.add(0, arrivalActivity);
        sortActivitiesByTime();
        activitiesTable.setDisable(false);
        addArrivalButton.setDisable(true);
    }

    @FXML
    private void handleArrivalTimeChanged() {
        addArrivalButton.setDisable(arrivalDatePicker.getValue() == null || arrivalTimeField.getText().isEmpty());
    }

    @FXML
    private void handleAddCustomActivity() {
        String customActivity = customActivityField.getText().trim();
        String customTime = customTimeField.getText().trim();

        if (!customActivity.isEmpty() && !customTime.isEmpty()) {
            if (isValidTimeFormat(customTime) && !hasTimeConflict(customTime)) {
                activities.add(new Activity(customActivity, customTime, true));
                customActivityField.clear();
                customTimeField.clear();
                sortActivitiesByTime();
            } else {
                showAlert("Invalid Time", "Please enter a valid time in the format 7:00 - 8:00 A.M or P.M");
            }
        } else {
            showAlert("Missing Information", "Please enter both an activity and a time.");
        }
    }

    private boolean isValidTimeFormat(String time) {
        String regex = "^(1[0-2]|0?[1-9]):[0-5][0-9] - (1[0-2]|0?[1-9]):[0-5][0-9] (A\\.M|P\\.M)$";
        return time.matches(regex);
    }

    private boolean hasTimeConflict(String newTime) {
        // Implement time conflict checking logic here
        return false; // Placeholder
    }

    private void sortActivitiesByTime() {
        activities.sort(Comparator.comparing(Activity::getTime, this::compareTimes));
        activitiesTable.refresh();
    }

    private int compareTimes(String time1, String time2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm - h:mm a");
        try {
            return LocalDate.parse(time1, formatter).compareTo(LocalDate.parse(time2, formatter));
        } catch (DateTimeParseException e) {
            return 0;
        }
    }

    private void playVideo() {
        String videoPath = getClass().getResource("/images/PALAWAN FRAME.mp4").toExternalForm();
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        });
        mediaPlayer.play();
    }

    private void goBack() {
        try {
            // Load the previous scene FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("destination.fxml"));
            Parent root = loader.load();
            destination destinationController = loader.getController();
            destinationController.setUsername(username);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void saveToDatabase() {
        String url = "jdbc:mysql://localhost:3306/your_database_name";
        String user = "your_username";
        String password = "your_password";

        String insertSQL = "INSERT INTO activities (name, time, active) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                for (Activity activity : activities) {
                    pstmt.setString(1, activity.getName());
                    pstmt.setString(2, activity.getTime());
                    pstmt.setBoolean(3, activity.isActive());
                    pstmt.executeUpdate();
                }
                showAlert("Success", "Activities have been saved to the database.");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while saving to the database.");
            e.printStackTrace();
        }
    }
//
//    @FXML
//    private void handleSavePDF() {
//        String path = "itinerary.pdf";
//        try (PdfWriter writer = new PdfWriter(path);
//             Document document = new Document(writer)) {
//            document.add(new Paragraph("Itinerary for Palawan"));
//
//            for (Activity activity : activities) {
//                document.add(new Paragraph(activity.getName() + " - " + activity.getTime()));
//            }
//
//            showAlert("Success", "Itinerary saved as PDF.");
//        } catch (FileNotFoundException e) {
//            showAlert("File Error", "An error occurred while saving the PDF.");
//            e.printStackTrace();
//        }
//    }

    @FXML
    private void resetFields() {
        arrivalDatePicker.setValue(null);
        arrivalTimeField.clear();
        customActivityField.clear();
        customTimeField.clear();
        activities.clear();
        activities.addAll(
                new Activity("Island Hopping in El Nido", "", false),
                new Activity("Underground River Tour", "", false),
                new Activity("Snorkeling and Diving in Coron", "", false),
                new Activity("Kayaking in Big Lagoon", "", false),
                new Activity("Swimming in Kayangan Lake", "", false)
        );
        activitiesTable.setDisable(true);
        addArrivalButton.setDisable(true);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Activity {
        private final StringProperty name;
        private final StringProperty time;
        private final BooleanProperty active;

        public Activity(String name, String time, boolean active) {
            this.name = new SimpleStringProperty(name);
            this.time = new SimpleStringProperty(time);
            this.active = new SimpleBooleanProperty(active);
        }

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getTime() {
            return time.get();
        }

        public StringProperty timeProperty() {
            return time;
        }

        public void setTime(String time) {
            this.time.set(time);
        }

        public boolean isActive() {
            return active.get();
        }

        public BooleanProperty activeProperty() {
            return active;
        }

        public void setActive(boolean active) {
            this.active.set(active);
        }
    }
}
