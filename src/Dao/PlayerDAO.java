package Dao;

import Model.Player;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {
    
    // Method to retrieve all players from the database
    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        String query = "SELECT * FROM players";  // Ensure table and column names match

        // Using try-with-resources to automatically close resources
        try (Connection conn = DatabaseConnection.getConnection();  
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Loop through the result set and add each player to the list
            while (rs.next()) {
                Player player = new Player(
                    rs.getInt("player_id"),       // Player ID (Primary Key)
                    rs.getString("name"),         // Player Name
                    rs.getString("position"),     // Player Position
                    rs.getInt("jersey_number"),   // Jersey Number
                    rs.getDate("date_of_birth"),  // Date of Birth
                    rs.getString("nationality"),  // Nationality
                    rs.getBoolean("is_active"),   // Active Status
                    rs.getInt("goals"),           // Goals Scored
                    rs.getInt("assists"),         // Assists Made
                    rs.getInt("matches_played")   // Matches Played
                );
                players.add(player);  // Add the player to the list
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log the error for debugging
        }
        return players;  // Return the list of players
    }

    // New method added for searching players by name
  public List<Player> searchPlayersByName(String searchTerm) {
    List<Player> players = new ArrayList<>();
    String query = "SELECT * FROM players WHERE LOWER(name) LIKE LOWER(?)";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        pstmt.setString(1, "%" + searchTerm + "%");
        
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Player player = new Player(
                    rs.getInt("player_id"),
                    rs.getString("name"),
                    rs.getString("position"),
                    rs.getInt("jersey_number"),
                    rs.getDate("date_of_birth"),
                    rs.getString("nationality"),
                    rs.getBoolean("is_active"),
                    rs.getInt("goals"),
                    rs.getInt("assists"),
                    rs.getInt("matches_played")
                );
                players.add(player);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return players;
}
}