package org.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class HelloController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField signupPasswordField;

    @FXML
    private TextField emailField;

    @FXML
    private Button signupButton;

    @FXML
    void handleSignupButtonClick(ActionEvent event) throws IOException {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = signupPasswordField.getText();

        // Perform validation (e.g., check if fields are not empty)
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            // Handle empty fields (show an error message, etc.)
            System.out.println("Please fill in all fields.");
            return;
        }

        // Insert user details into the database
        try {
            insertUser(username, email, password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error inserting user into the database: " + e.getMessage());
            return;
        }

        // Load the next page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("second-page.fxml"));
        Parent secondPage = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(secondPage));
        stage.setTitle("Welcome to the Second Page!");
        stage.show();
        Stage signUpStage = (Stage) signupButton.getScene().getWindow();
        signUpStage.close();

        System.out.println("Sign up successful!");
    }

    private void insertUser(String username, String email, String password) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/user_db";
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

    public void handleloginLabelClick(javafx.scene.input.MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("second-page.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
