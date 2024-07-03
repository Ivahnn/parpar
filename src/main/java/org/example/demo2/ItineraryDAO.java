package org.example.demo2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItineraryDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/ParDist";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public List<Itinerary> getItinerariesForUser(String username) throws SQLException {
        List<Itinerary> itineraries = new ArrayList<>();
        String sql = "SELECT * FROM Itinerary WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Itinerary itinerary = new Itinerary();
                    itinerary.setId(rs.getInt("id"));
                    itinerary.setUsername(rs.getString("username"));
                    itinerary.setHotel(rs.getString("hotel"));
                    itinerary.setTopAttraction(rs.getString("topAttraction"));
                    itinerary.setActivity(rs.getString("activity"));
                    itinerary.setBreakfast(rs.getString("breakfast"));
                    itinerary.setLunch(rs.getString("lunch"));
                    itinerary.setDinner(rs.getString("dinner"));
                    itinerary.setDuration(rs.getString("duration"));
                    itineraries.add(itinerary);
                }
            }
        }
        return itineraries;
    }

    public void insertItinerary(Itinerary itinerary) throws SQLException {
        String sql = "INSERT INTO Itinerary (username, hotel, topAttraction, activity, breakfast, lunch, dinner, duration) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, itinerary.getUsername());
            pstmt.setString(2, itinerary.getHotel());
            pstmt.setString(3, itinerary.getTopAttraction());
            pstmt.setString(4, itinerary.getActivity());
            pstmt.setString(5, itinerary.getBreakfast());
            pstmt.setString(6, itinerary.getLunch());
            pstmt.setString(7, itinerary.getDinner());
            pstmt.setString(8, itinerary.getDuration());
            pstmt.executeUpdate();
        }
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
