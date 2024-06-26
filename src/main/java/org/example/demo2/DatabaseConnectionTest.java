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

            // Example query
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT TOP 5 * FROM ParDist");

            // Process the result set (if needed)
            while (resultSet.next()) {
                // Example: Retrieve data from the result set
                String column1Value = resultSet.getString("columnName");
                // Process retrieved data as needed
                System.out.println("Column 1 Value: " + column1Value);
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
