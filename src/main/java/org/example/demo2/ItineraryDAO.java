package org.example.demo2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItineraryDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/ParDist";
    private static final String USER = "your_mysql_username";
    private static final String PASSWORD = "your_mysql_password";

    public List<Itinerary> getItineraries(String username) throws SQLException {
        List<Itinerary> itineraries = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM Itinerary WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Itinerary itinerary = new Itinerary();
                        itinerary.setId(resultSet.getInt("id"));
                        itinerary.setHotel(resultSet.getString("hotel"));
                        itinerary.setTopAttraction(resultSet.getString("topAttraction"));
                        itinerary.setActivity(resultSet.getString("activity"));
                        itinerary.setBreakfast(resultSet.getString("breakfast"));
                        itinerary.setLunch(resultSet.getString("lunch"));
                        itinerary.setDinner(resultSet.getString("dinner"));
                        itinerary.setDuration(resultSet.getString("duration"));
                        itineraries.add(itinerary);
                    }
                }
            }
        }
        return itineraries;
    }

    public void deleteItinerary(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "DELETE FROM Itinerary WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        }
    }

    // Implement other CRUD operations as needed
}
