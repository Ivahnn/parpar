package org.example.demo2;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
<<<<<<< HEAD
import javafx.application.Platform;
=======
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
>>>>>>> 82cef89 (feat: add test palawan for activities)
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
<<<<<<< HEAD
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
=======
import javafx.scene.input.*;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
>>>>>>> 82cef89 (feat: add test palawan for activities)
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

<<<<<<< HEAD
=======
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

>>>>>>> 82cef89 (feat: add test palawan for activities)
public class PalawanTest implements Initializable {

    @FXML
    private ComboBox<String> hotelComboBox;

    @FXML
<<<<<<< HEAD
    private TableView<Activity> chosenActivitiesTable;

    @FXML
    private TableColumn<Activity, String> chosenActivityColumn;

    @FXML
    private TableColumn<Activity, String> chosenTimeColumn;

    @FXML
    private TableView<Activity> predefinedActivitiesTable;

    @FXML
    private TableColumn<Activity, String> predefinedActivityColumn;

    @FXML
    private TableView<Activity> mealsTable;

    @FXML
    private TableColumn<Activity, String> mealsColumn;
=======
    private TableView<Activity> activitiesTable;

    @FXML
    private TableColumn<Activity, String> activityColumn;

    @FXML
    private TableColumn<Activity, String> timeColumn;
>>>>>>> 82cef89 (feat: add test palawan for activities)

    @FXML
    private TextField customActivityField;

    @FXML
<<<<<<< HEAD
    private TextField customActivityField1;

    @FXML
=======
>>>>>>> 82cef89 (feat: add test palawan for activities)
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
<<<<<<< HEAD
    private Button resetButton;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final ObservableList<Activity> chosenActivities = FXCollections.observableArrayList();
    private final ObservableList<Activity> predefinedActivities = FXCollections.observableArrayList();
    private final ObservableList<Activity> mealsactivities = FXCollections.observableArrayList();
=======
    private Button resetButton; // Add this field

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final ObservableList<Activity> activities = FXCollections.observableArrayList();
>>>>>>> 82cef89 (feat: add test palawan for activities)

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playVideo();

        usernameField.setText(username);
        System.out.println("Username field set to: " + usernameField.getText());

<<<<<<< HEAD
        initializeTables();
        addArrivalButton.setDisable(true);
        chosenActivitiesTable.setDisable(true);
=======
        initializeActivitiesTable();
        addArrivalButton.setDisable(true);
        activitiesTable.setDisable(true);
>>>>>>> 82cef89 (feat: add test palawan for activities)
        initializeHotelComboBox();

        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> goBack());
        resetButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> resetFields()); // Add this handler
    }

    private void initializeHotelComboBox() {
        executorService.submit(() -> {
<<<<<<< HEAD
            Platform.runLater(() -> {
                hotelComboBox.getItems().addAll(
                        "Amanpulo", "El Nido Resorts Pangulasian Island", "Two Seasons Coron Island Resort & Spa",
                        "Club Paradise Palawan", "Lagen Island Resort"
                );
            });
        });
    }

    private void initializeTables() {
        // Initialize chosen activities table
        chosenActivityColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        chosenTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Make time column editable
        chosenTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        chosenTimeColumn.setOnEditCommit(event -> {
            Platform.runLater(() -> {
                Activity activity = event.getRowValue();
                String newValue = event.getNewValue().trim();
                String[] timeRange = newValue.split("-");
                if (timeRange.length == 2 && isValidTimeFormat(timeRange[0].trim()) && isValidTimeFormat(timeRange[1].trim())) {
                    if (compareTimes(timeRange[0].trim(), timeRange[1].trim()) < 0) {
                        String arrivalTime = arrivalTimeField.getText();
                        if (!arrivalTime.isEmpty() && compareTimes(arrivalTime, timeRange[0].trim()) <= 0) {
                            activity.setTime(newValue);
                            activity.setActive(true);
                            sortActivitiesByTime();
                            System.out.println("Time updated and sorted: " + newValue);
                        } else {
                            showAlert("Invalid Time Range", "Start time must be after the arrival time.");
                            activity.setTime("");
                            activity.setActive(false);
                        }
                    } else {
                        showAlert("Invalid Time Range", "Start time must be before end time.");
                        activity.setTime("");
                        activity.setActive(false);
                    }
                } else {
                    showAlert("Invalid Time Format", "Please enter a valid time range in the format HH:mm-HH:mm.");
                    activity.setTime("");
                    activity.setActive(false);
                }
                chosenActivitiesTable.refresh();
            });
        });

        chosenActivitiesTable.setItems(chosenActivities);

        predefinedActivityColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        predefinedActivities.addAll(
=======
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
>>>>>>> 82cef89 (feat: add test palawan for activities)
                new Activity("Island Hopping in El Nido", "", false),
                new Activity("Underground River Tour", "", false),
                new Activity("Snorkeling and Diving in Coron", "", false),
                new Activity("Kayaking in Big Lagoon", "", false),
                new Activity("Swimming in Kayangan Lake", "", false)
        );
<<<<<<< HEAD
        predefinedActivitiesTable.setItems(predefinedActivities);

        mealsColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        mealsactivities.addAll(
                new Activity("Adobo", "", false),
                new Activity("Sinigang", "", false),
                new Activity("Sisig", "", false),
                new Activity("Tinola", "", false),
                new Activity("Pancit", "", false)
        );
        mealsTable.setItems(mealsactivities);
=======

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
>>>>>>> 82cef89 (feat: add test palawan for activities)
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
<<<<<<< HEAD
            showAlert("Invalid Time", "Please enter a valid time in the format HH:mm.");
=======
            showAlert("Invalid Time", "Please enter a valid time in the format 7:00 - 8:00 A.M or P.M");
>>>>>>> 82cef89 (feat: add test palawan for activities)
            return;
        }

        Activity arrivalActivity = new Activity("Arrival", arrivalTime, true);
<<<<<<< HEAD
        chosenActivities.add(0, arrivalActivity);
        sortActivitiesByTime();
        chosenActivitiesTable.setDisable(false);
=======
        activities.add(0, arrivalActivity);
        sortActivitiesByTime();
        activitiesTable.setDisable(false);
>>>>>>> 82cef89 (feat: add test palawan for activities)
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
<<<<<<< HEAD
            if (isValidTimeRange(customTime)) {
                String[] timeRange = customTime.split("-");
                if (compareTimes(arrivalTimeField.getText(), timeRange[0].trim()) <= 0 && !hasTimeConflict(timeRange[0].trim(), timeRange[1].trim()) && !isDuplicateActivity(customActivity)) {
                    chosenActivities.add(new Activity(customActivity, customTime, true));
                    customActivityField.clear();
                    customTimeField.clear();
                    sortActivitiesByTime(); // Sort after adding custom activity
                } else {
                    showAlert("Invalid Time", "Please ensure the time range is after the arrival time and does not overlap with existing activities.");
                }
            } else {
                showAlert("Invalid Time Format", "Please enter a valid time range in the format HH:mm-HH:mm.");
=======
            if (isValidTimeFormat(customTime) && !hasTimeConflict(customTime)) {
                activities.add(new Activity(customActivity, customTime, true));
                customActivityField.clear();
                customTimeField.clear();
                sortActivitiesByTime();
            } else {
                showAlert("Invalid Time", "Please enter a valid time in the format 7:00 - 8:00 A.M or P.M");
>>>>>>> 82cef89 (feat: add test palawan for activities)
            }
        } else {
            showAlert("Missing Information", "Please enter both an activity and a time.");
        }
    }

<<<<<<< HEAD
    @FXML
    private void handleAddCustomActivity1() {
        String customActivity = customActivityField1.getText().trim();

        if (!customActivity.isEmpty()) {
            if (!isDuplicateActivity(customActivity)) {
                chosenActivities.add(new Activity(customActivity, "", true)); // Assuming 'true' for active state
                customActivityField1.clear();
                sortActivitiesByTime(); // Sort after adding custom activity
            } else {
                showAlert("Duplicate Activity", "This activity has already been added.");
            }
        } else {
            showAlert("Missing Information", "Please enter an activity.");
        }
    }

    @FXML
    private void handleAddPredefinedActivity() {
        Activity selectedActivity = predefinedActivitiesTable.getSelectionModel().getSelectedItem();
        if (selectedActivity != null && !isDuplicateActivity(selectedActivity.getName())) {
            chosenActivities.add(new Activity(selectedActivity.getName(), "", false));
            sortActivitiesByTime();
        }
    }

    @FXML
    private void handleAddMealActivity() {
        Activity selectedActivity = mealsTable.getSelectionModel().getSelectedItem();
        if (selectedActivity != null && !isDuplicateActivity(selectedActivity.getName())) {
            chosenActivities.add(new Activity(selectedActivity.getName(), "", false));
            sortActivitiesByTime();
        }
    }

    @FXML
    private void handleRemoveChosenActivity() {
        Activity selectedActivity = chosenActivitiesTable.getSelectionModel().getSelectedItem();
        if (selectedActivity != null && !selectedActivity.getName().equals("Arrival")) {
            chosenActivities.remove(selectedActivity);
        } else {
            showAlert("Invalid Action", "You cannot remove the arrival activity.");
        }
    }

    private boolean isValidTimeFormat(String time) {
        String regex = "^(?:[01]\\d|2[0-3]):[0-5]\\d$"; // 24-hour format HH:mm
        return time.matches(regex);
    }

    private boolean isValidTimeRange(String timeRange) {
        String[] times = timeRange.split("-");
        return times.length == 2 && isValidTimeFormat(times[0].trim()) && isValidTimeFormat(times[1].trim()) && compareTimes(times[0].trim(), times[1].trim()) < 0;
    }

    private int compareTimes(String time1, String time2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime t1 = LocalTime.parse(time1, formatter);
        LocalTime t2 = LocalTime.parse(time2, formatter);
        return t1.compareTo(t2);
    }

    private boolean hasTimeConflict(String startTime, String endTime) {
        for (Activity activity : chosenActivities) {
            if (activity.isActive() && !activity.getName().equals("Arrival")) {
                String[] existingTimes = activity.getTime().split("-");
                String existingStart = existingTimes[0].trim();
                String existingEnd = existingTimes.length > 1 ? existingTimes[1].trim() : existingStart;
                if (compareTimes(startTime, existingEnd) < 0 && compareTimes(existingStart, endTime) < 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isDuplicateActivity(String activityName) {
        for (Activity activity : chosenActivities) {
            if (activity.getName().equalsIgnoreCase(activityName)) {
                return true;
            }
        }
        return false;
    }

    private void sortActivitiesByTime() {
        chosenActivities.sort(Comparator.comparing(activity -> {
            if (activity.getTime().isEmpty()) {
                return LocalTime.MAX;
            } else {
                return LocalTime.parse(activity.getTime().split("-")[0].trim());
            }
        }));
        chosenActivitiesTable.refresh();
=======
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
>>>>>>> 82cef89 (feat: add test palawan for activities)
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
<<<<<<< HEAD
            FXMLLoader loader = new FXMLLoader(getClass().getResource("destination.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            destination destinationController = loader.getController();
            destinationController.setUsername(username);
=======
            // Load the previous scene FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("destination.fxml"));
            Parent root = loader.load();
            destination destinationController = loader.getController();
            destinationController.setUsername(username);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
>>>>>>> 82cef89 (feat: add test palawan for activities)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void saveToDatabase() {
<<<<<<< HEAD
        String url = "jdbc:mysql://localhost:3306/pardist";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            connection.setAutoCommit(false);

            String itinerarySql = "INSERT INTO itinerary (userId, location, hotel, day) VALUES ((SELECT userId FROM users WHERE username = ?), ?, ?, ?)";
            PreparedStatement itineraryStatement = connection.prepareStatement(itinerarySql, Statement.RETURN_GENERATED_KEYS);
            itineraryStatement.setString(1, username);
            itineraryStatement.setString(2, "Palawan");  // Fixed location
            itineraryStatement.setString(3, hotelComboBox.getValue());
            itineraryStatement.setDate(4, java.sql.Date.valueOf(arrivalDatePicker.getValue()));
            itineraryStatement.executeUpdate();

            ResultSet generatedKeys = itineraryStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int itineraryId = generatedKeys.getInt(1);

                String activitySql = "INSERT INTO activity (itinerary_id, name, time, active) VALUES (?, ?, ?, ?)";
                PreparedStatement activityStatement = connection.prepareStatement(activitySql);

                for (Activity activity : chosenActivities) {
                    activityStatement.setInt(1, itineraryId);
                    activityStatement.setString(2, activity.getName());
                    activityStatement.setString(3, activity.getTime());
                    activityStatement.setBoolean(4, activity.isActive());
                    activityStatement.addBatch();
                }

                activityStatement.executeBatch();
            }

            connection.commit();
            showAlert("Success", "Data saved to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while saving data to the database.");
        }
    }
=======
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
>>>>>>> 82cef89 (feat: add test palawan for activities)

    @FXML
    private void resetFields() {
        arrivalDatePicker.setValue(null);
        arrivalTimeField.clear();
        customActivityField.clear();
        customTimeField.clear();
<<<<<<< HEAD
        chosenActivities.clear();
        chosenActivitiesTable.setDisable(true);
        addArrivalButton.setDisable(true);
        System.out.println("Fields have been reset.");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
=======
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
>>>>>>> 82cef89 (feat: add test palawan for activities)
}
