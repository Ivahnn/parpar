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
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    void handleLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        try {
            if (!isValidCredentials(username, password)) {
                System.out.println("Incorrect username or password.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error validating credentials: " + e.getMessage());
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo2/home-page.fxml"));
            Parent homePage = loader.load();

            HomePageController homepageController = loader.getController();
            homepageController.setUsername(username); // Pass the username to the Homepage controller

            Stage stage = new Stage();
            stage.setScene(new Scene(homePage));
            stage.setTitle("Lakbay Likha!");
            stage.show();

            Stage loginStage = (Stage) loginButton.getScene().getWindow();
            loginStage.close();

            System.out.println("Login successful!" + username);
        } catch (IOException e) {
            System.err.println("Failed to load home-page.fxml");
            e.printStackTrace();
        }
    }

    private boolean isValidCredentials(String username, String password) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/pardist";
        String dbUser = "root"; // Adjust this if you have a different MySQL username
        String dbPassword = "";  // Adjust this if you have a MySQL password

        String query = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
            throw e; // rethrow the exception for further handling if needed
        }
    }


    public void handleloginLabelClick(javafx.scene.input.MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("sign-up.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
