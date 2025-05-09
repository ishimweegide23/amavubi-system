package Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchDAO {

    // Fetch all available matches from the database with seat categories and prices
    public String[] getAvailableMatches() {
        String query = "SELECT opponent, match_date, venue, ticket_price, vip_available_seats, regular_available_seats, upbowl_available_seats FROM matches";
        List<String> matchesList = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String opponent = rs.getString("opponent");
                String matchDate = rs.getString("match_date");
                String venue = rs.getString("venue");
                double ticketPrice = rs.getDouble("ticket_price");
                int vipSeats = rs.getInt("vip_available_seats");
                int regularSeats = rs.getInt("regular_available_seats");
                int upbowlSeats = rs.getInt("upbowl_available_seats");

                // Create a match description including opponent, date, venue, ticket price, and available seats
                String matchDetails = "Rwanda vs " + opponent + " on " + matchDate + 
                                      " at " + venue + " | Price: $" + ticketPrice + 
                                      " | Available Seats - VIP: " + vipSeats + ", Regular: " + regularSeats + 
                                      ", Upbowl: " + upbowlSeats;
                matchesList.add(matchDetails);
            }

            // Convert the list to a String array
            return matchesList.toArray(new String[0]);
        } catch (SQLException e) {
            System.err.println("Error fetching available matches: " + e.getMessage());
            e.printStackTrace();
        }
        return new String[0];  // Return an empty array in case of error
    }

    // Method to get the match_id based on opponent and date
    public int getMatchIdByOpponentAndDate(String opponent, String matchDate) {
        String query = "SELECT match_id, vip_available_seats, regular_available_seats, upbowl_available_seats FROM matches WHERE opponent = ? AND match_date = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, opponent);
            pstmt.setString(2, matchDate);

            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int matchId = rs.getInt("match_id");
                int vipSeats = rs.getInt("vip_available_seats");
                int regularSeats = rs.getInt("regular_available_seats");
                int upbowlSeats = rs.getInt("upbowl_available_seats");

                // Check if there are available seats for booking in each category
                if (vipSeats > 0 || regularSeats > 0 || upbowlSeats > 0) {
                    return matchId;  // Return the match_id if available seats are greater than 0
                } else {
                    System.err.println("No available seats for this match.");
                    return -1;  // No available seats
                }
            } else {
                System.err.println("No match found for opponent: " + opponent + " on " + matchDate);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching match ID: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;  // No match found or no available seats
    }

    // Update available seats after booking a match ticket for a specific category (VIP, Regular, Upbowl)
    public boolean updateAvailableSeats(int matchId, String seatCategory) {
        String query = "";
        
        // Decide which category to update (VIP, Regular, or Upbowl)
        switch (seatCategory) {
            case "VIP":
                query = "UPDATE matches SET vip_available_seats = vip_available_seats - 1 WHERE match_id = ? AND vip_available_seats > 0";
                break;
            case "Regular":
                query = "UPDATE matches SET regular_available_seats = regular_available_seats - 1 WHERE match_id = ? AND regular_available_seats > 0";
                break;
            case "Upbowl":
                query = "UPDATE matches SET upbowl_available_seats = upbowl_available_seats - 1 WHERE match_id = ? AND upbowl_available_seats > 0";
                break;
            default:
                System.err.println("Invalid seat category: " + seatCategory);
                return false;  // Return false if invalid seat category is provided
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, matchId);

            // Execute the update and check how many rows were affected
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;  // Return true if a row was updated (i.e., a ticket was successfully booked)
        } catch (SQLException e) {
            System.err.println("Error updating available seats for " + seatCategory + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;  // Return false if no rows were updated (no available seats or error)
    }

    // Updated method to return ticket price based on seat category
    public double getTicketPriceByCategory(int matchId, String selectedSeatCategory) {
        String query = "SELECT ticket_price FROM matches WHERE match_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, matchId);

            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                double ticketPrice = rs.getDouble("ticket_price");

                // Return price based on the seat category
                switch (selectedSeatCategory) {
                    case "VIP":
                        return ticketPrice * 1.5;  // VIP is 1.5x the base price
                    case "Regular":
                        return ticketPrice;  // Regular is the base price
                    case "Upbowl":
                        return ticketPrice * 0.75;  // Upbowl is 75% of the base price
                    default:
                        System.err.println("Invalid seat category: " + selectedSeatCategory);
                        return 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching ticket price: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;  // Return 0 if no price is found
    }

    // Insert match booking
    public void insertMatchBooking(int matchId, int userId, String seatCategory, Timestamp bookingDate) {
        String sql = "INSERT INTO match_bookings (match_id, user_id, seat_category, booking_date) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set the parameters for the prepared statement
            pstmt.setInt(1, matchId);  // Set match_id
            pstmt.setInt(2, userId);   // Set user_id
            pstmt.setString(3, seatCategory);  // Set seat_category
            pstmt.setTimestamp(4, bookingDate);  // Set booking_date
            
            // Execute the update
            pstmt.executeUpdate();
            System.out.println("Booking successfully inserted.");
        } catch (SQLException e) {
            System.err.println("Error inserting booking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getAvailableSeats(int matchId, String vip) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
