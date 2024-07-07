package org.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignupController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField signupPasswordField;

    @FXML
    private PasswordField passwordField; // This is the re-entered password field

    @FXML
    private TextField signupPasswordTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField emailField;

    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    private Button signupButton;

    @FXML
    void handleSignupButtonClick(ActionEvent event) throws IOException {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = showPasswordCheckBox.isSelected() ? signupPasswordTextField.getText() : signupPasswordField.getText();
        String rePassword = showPasswordCheckBox.isSelected() ? passwordTextField.getText() : passwordField.getText(); // Get the re-entered password

        // Perform validation (e.g., check if fields are not empty)
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            showAlert("Sign Up Failed", "Please fill in all fields.", AlertType.ERROR);
            return;
        }

        // Check if email contains '@'
        if (!email.contains("@")) {
            showAlert("Sign Up Failed", "Please enter a valid email address that includes '@'.", AlertType.ERROR);
            return;
        }

        // Check if password and re-password match
        if (!password.equals(rePassword)) {
            showAlert("Sign Up Failed", "Passwords do not match. Please make it the same", AlertType.ERROR);
            return;
        }

        // Insert user details into the database
        try {
            insertUser(username, email, password);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Sign Up Failed", "Error inserting user into the database: " + e.getMessage(), AlertType.ERROR);
            return;
        }

        // Show success alert
        showAlert("Sign Up Successful", "Account created successfully!", AlertType.INFORMATION);

        // Load the next page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent secondPage = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(secondPage));
        stage.setTitle("Welcome to login");
        stage.show();
        Stage signUpStage = (Stage) signupButton.getScene().getWindow();
        signUpStage.close();
        System.out.println("Sign up successful!");
    }

    private void insertUser(String username, String email, String password) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/pardist";
        String dbUser = "root"; // Adjust this if you have a different MySQL username
        String dbPassword = "";  // Adjust this if you have a MySQL password

        String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.executeUpdate();
            System.out.println("User inserted successfully.");
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
            throw e; // rethrow the exception for further handling if needed
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void togglePasswordVisibility(ActionEvent event) {
        if (showPasswordCheckBox.isSelected()) {
            signupPasswordTextField.setText(signupPasswordField.getText());
            passwordTextField.setText(passwordField.getText());
            signupPasswordTextField.setVisible(true);
            passwordTextField.setVisible(true);
            signupPasswordField.setVisible(false);
            passwordField.setVisible(false);
        } else {
            signupPasswordField.setText(signupPasswordTextField.getText());
            passwordField.setText(passwordTextField.getText());
            signupPasswordField.setVisible(true);
            passwordField.setVisible(true);
            signupPasswordTextField.setVisible(false);
            passwordTextField.setVisible(false);
        }
    }

    public void handleloginLabelClick(javafx.scene.input.MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
