package org.example.demo2;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.application.Platform;
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
import java.time.LocalTime;
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


    @FXML
    private TextField customActivityField;

    @FXML
    private TextField customActivityField1;

    @FXML
    private TextField customTimeField;
    @FXML

    private TextField customTimeField1;

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
    private Button resetButton;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final ObservableList<Activity> chosenActivities = FXCollections.observableArrayList();
    private final ObservableList<Activity> predefinedActivities = FXCollections.observableArrayList();
    private final ObservableList<Activity> mealsactivities = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playVideo();

        usernameField.setText(username);
        System.out.println("Username field set to: " + usernameField.getText());

        initializeTables();
        addArrivalButton.setDisable(true);
        chosenActivitiesTable.setDisable(true);
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
                new Activity("Island Hopping in El Nido", "", false),
                new Activity("Underground River Tour", "", false),
                new Activity("Snorkeling and Diving in Coron", "", false),
                new Activity("Kayaking in Big Lagoon", "", false),
                new Activity("Swimming in Kayangan Lake", "", false)
        );
        predefinedActivitiesTable.setItems(predefinedActivities);

        mealsColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        mealsactivities.addAll(
                new Activity("Adobo", "", false),
                new Activity("sinigang", "", false),
                new Activity("sisig", "", false),
                new Activity("tinola", "", false),
                new Activity("pancit", "", false)
        );
        mealsTable.setItems(mealsactivities);

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
            showAlert("Invalid Time", "Please enter a valid time in the format HH:mm.");
            return;
        }

        Activity arrivalActivity = new Activity("Arrival", arrivalTime, true);
        chosenActivities.add(0, arrivalActivity);
        sortActivitiesByTime();
        chosenActivitiesTable.setDisable(false);
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
            }
        } else {
            showAlert("Missing Information", "Please enter both an activity and a time.");
        }
    }

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
        chosenActivities.sort(Comparator.comparing(activity -> LocalTime.parse(activity.getTime().split("-")[0].trim())));
        chosenActivitiesTable.refresh();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("destination.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            destination destinationController = loader.getController();
            destinationController.setUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void saveToDatabase() {
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO activity (username, activity, time, date) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            for (Activity activity : chosenActivities) {
                statement.setString(1, username);
                statement.setString(2, activity.getName());
                statement.setString(3, activity.getTime());
                statement.setDate(4, java.sql.Date.valueOf(arrivalDatePicker.getValue()));
                statement.addBatch();
            }
            statement.executeBatch();
            showAlert("Success", "Data saved to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while saving data to the database.");
        }
    }

    @FXML
    private void resetFields() {
        arrivalDatePicker.setValue(null);
        arrivalTimeField.clear();
        customActivityField.clear();
        customTimeField.clear();
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

        public void setName(String name) {
            this.name.set(name);
        }

        public StringProperty nameProperty() {
            return name;
        }

        public String getTime() {
            return time.get();
        }

        public void setTime(String time) {
            this.time.set(time);
        }

        public StringProperty timeProperty() {
            return time;
        }

        public boolean isActive() {
            return active.get();
        }

        public void setActive(boolean active) {
            this.active.set(active);
        }

        public BooleanProperty activeProperty() {
            return active;
        }
    }
}