package org.example.demo2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnectionTest {

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Attempt to establish connection
            connection = DatabaseConnection.getConnection();
            System.out.println("Connected to SQL Server database!");

            // Create a statement
            statement = connection.createStatement();

            // Execute query to retrieve data from Users table
            resultSet = statement.executeQuery("SELECT * FROM Users");

            System.out.println("Users:");
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                System.out.println("Username: " + username + ", Password: " + password + ", Email: " + email);
            }

            // Execute query to retrieve data from Itinerary table
            resultSet = statement.executeQuery("SELECT * FROM Itinerary");

            System.out.println("Itinerary:");
            while (resultSet.next()) {
                int userId = resultSet.getInt("userId");
                String hotel = resultSet.getString("hotel");
                String topAttraction = resultSet.getString("topAttraction");
                String activity = resultSet.getString("activity");
                String breakfast = resultSet.getString("breakfast");
                String lunch = resultSet.getString("lunch");
                String dinner = resultSet.getString("dinner");
                String duration = resultSet.getString("duration");
                System.out.println("User ID: " + userId + ", Hotel: " + hotel + ", Top Attraction: " + topAttraction + ", Activity: " + activity + ", Breakfast: " + breakfast + ", Lunch: " + lunch + ", Dinner: " + dinner + ", Duration: " + duration);
            }

        } catch (SQLException e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
        } finally {
            // Close resources in finally block
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

//CREATE TABLE Users (
//    userId INT PRIMARY KEY AUTO_INCREMENT,
//    username NVARCHAR(50),
//    password NVARCHAR(50),
//    email NVARCHAR(50) -- Removed the trailing comma
//);
// CREATE TABLE Itinerary (     id INT PRIMARY KEY AUTO_INCREMENT,     userId INT,  location VARCHAR(100)   ,hotel VARCHAR(100),     topAttraction VARCHAR(100),     activity VARCHAR(100),     breakfast VARCHAR(100),     lunch VARCHAR(100),     dinner VARCHAR(100),     day DATE,     FOREIGN KEY (userId) REFERENCES Users(userId) );