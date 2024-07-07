package org.example.demo2;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.application.Platform;
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
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class batangas implements Initializable {

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
    private DatePicker arrivalDatePicker;

    @FXML
    private TextField arrivalTimeField;

    @FXML
    private TextField usernameField;
    private String username;

    public void setUsername(String username) {
        this.username = username;
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

        initializeTables();
        addArrivalButton.setDisable(true);
        chosenActivitiesTable.setDisable(true);
        initializeHotelComboBox();

        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> goBack());
        resetButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> resetFields());
    }

    private void initializeHotelComboBox() {
        executorService.submit(() -> {
            Platform.runLater(() -> {
                hotelComboBox.getItems().addAll(
                        "Club Punta Fuego", "Acuatico Beach Resort & Hotel", "The Farm at San Benito",
                        "Taal Vista Hotel", "Vivere Azure"
                );
            });
        });
    }

    private void initializeTables() {
        chosenActivityColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        chosenTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

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
                new Activity("Scuba Diving in Anilao", "", false),
                new Activity("Hiking Taal Volcano", "", false),
                new Activity("Beach Hopping in Laiya", "", false),
                new Activity("Island Hopping to Fortune Island", "", false),
                new Activity("Trekking Mt. Batulao", "", false)
        );
        predefinedActivitiesTable.setItems(predefinedActivities);

        mealsColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        mealsactivities.addAll(
                new Activity("Bulalo (Beef Bone Marrow Soup)", "", false),
                new Activity("Tamales Batangas", "", false),
                new Activity("Panutsa (Sugar Cane Sweet)", "", false),
                new Activity("Kapeng Barako", "", false),
                new Activity("Goto Batangas", "", false),
                new Activity("Adobo sa Dilaw", "", false),
                new Activity("Lomi", "", false),
                new Activity("Sinigang na Baboy sa Bayabas", "", false),
                new Activity("Sinaing na Tulingan", "", false),
                new Activity("Bulalo Steak", "", false),
                new Activity("Adobong Dilaw na Manok", "", false),
                new Activity("Maliputo", "", false),
                new Activity("Sinigang na Hipon", "", false),
                new Activity("Batangas Goto", "", false),
                new Activity("Batangas Express", "", false)
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
                    sortActivitiesByTime();
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
                chosenActivities.add(new Activity(customActivity, "", true));
                customActivityField1.clear();
                sortActivitiesByTime();
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
        String regex = "^(?:[01]\\d|2[0-3]):[0-5]\\d$";
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
    }

    private void playVideo() {
        String videoPath = getClass().getResource("/images/BATANGAS FRAME.mp4").toExternalForm();
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
        for (Activity activity : chosenActivities) {
            if (activity.getTime().isEmpty()) {
                showAlert("Invalid Activity", "All activities must have a valid time.");
                return;
            }
        }

        String url = "jdbc:mysql://localhost:3306/pardist";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            connection.setAutoCommit(false);

            String checkItinerarySql = "SELECT COUNT(*) FROM itinerary WHERE userId = (SELECT userId FROM users WHERE username = ?) AND day = ?";
            PreparedStatement checkItineraryStmt = connection.prepareStatement(checkItinerarySql);
            checkItineraryStmt.setString(1, username);
            checkItineraryStmt.setDate(2, java.sql.Date.valueOf(arrivalDatePicker.getValue()));
            ResultSet checkItineraryRs = checkItineraryStmt.executeQuery();
            if (checkItineraryRs.next() && checkItineraryRs.getInt(1) > 0) {
                showAlert("Duplicate Itinerary", "An itinerary for the same day already exists.");
                return;
            }

            String itinerarySql = "INSERT INTO itinerary (userId, location, hotel, day) VALUES ((SELECT userId FROM users WHERE username = ?), ?, ?, ?)";
            PreparedStatement itineraryStatement = connection.prepareStatement(itinerarySql, Statement.RETURN_GENERATED_KEYS);
            itineraryStatement.setString(1, username);
            itineraryStatement.setString(2, "Batangas");
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

    @FXML
    private void resetFields() {
        arrivalDatePicker.setValue(null);
        arrivalTimeField.clear();
        customActivityField.clear();
        customTimeField.clear();
        chosenActivities.clear();
        chosenActivitiesTable.setDisable(true);
        addArrivalButton.setDisable(true);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
