package Model;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private int playerId;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "position", length = 50)
    private String position;
    
    @Column(name = "jersey_number")
    private int jerseyNumber;
    
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    
    @Column(name = "nationality", length = 50)
    private String nationality;
    
    @Column(name = "is_active")
    private boolean isActive;
    
    @Column(name = "goals")
    private int goals;
    
    @Column(name = "assists")
    private int assists;
    
    @Column(name = "matches_played")
    private int matchesPlayed;

    // Default constructor required by Hibernate
    public Player() {
    }

    // Full constructor for convenience
    public Player(int playerId, String name, String position, int jerseyNumber, 
                 Date dateOfBirth, String nationality, boolean isActive, 
                 int goals, int assists, int matchesPlayed) {
        this.playerId = playerId;
        this.name = name;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.isActive = isActive;
        this.goals = goals;
        this.assists = assists;
        this.matchesPlayed = matchesPlayed;
    }

    // Getters
    public int getPlayerId() { 
        return playerId; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public String getPosition() { 
        return position; 
    }
    
    public int getJerseyNumber() { 
        return jerseyNumber; 
    }
    
    public Date getDateOfBirth() { 
        return dateOfBirth; 
    }
    
    public String getNationality() { 
        return nationality; 
    }
    
    public boolean isActive() { 
        return isActive; 
    }
    
    public int getGoals() { 
        return goals; 
    }
    
    public int getAssists() { 
        return assists; 
    }
    
    public int getMatchesPlayed() { 
        return matchesPlayed; 
    }

    // Setters
    public void setPlayerId(int playerId) { 
        this.playerId = playerId; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public void setPosition(String position) { 
        this.position = position; 
    }
    
    public void setJerseyNumber(int jerseyNumber) { 
        this.jerseyNumber = jerseyNumber; 
    }
    
    public void setDateOfBirth(Date dateOfBirth) { 
        this.dateOfBirth = dateOfBirth; 
    }
    
    public void setNationality(String nationality) { 
        this.nationality = nationality; 
    }
    
    public void setActive(boolean isActive) { 
        this.isActive = isActive; 
    }
    
    public void setGoals(int goals) { 
        this.goals = goals; 
    }
    
    public void setAssists(int assists) { 
        this.assists = assists; 
    }
    
    public void setMatchesPlayed(int matchesPlayed) { 
        this.matchesPlayed = matchesPlayed; 
    }

    // toString() for debugging/logging
    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", jerseyNumber=" + jerseyNumber +
                ", dateOfBirth=" + dateOfBirth +
                ", nationality='" + nationality + '\'' +
                ", isActive=" + isActive +
                ", goals=" + goals +
                ", assists=" + assists +
                ", matchesPlayed=" + matchesPlayed +
                '}';
    }
}