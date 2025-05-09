package Model;

import java.util.Date;

public class Player {
    private int playerId;
    private String name;
    private String position;
    private int jerseyNumber;
    private Date dateOfBirth;
    private String nationality;
    private boolean isActive;
    private int goals;
    private int assists;
    private int matchesPlayed;

    // Constructor
    public Player(int playerId, String name, String position, int jerseyNumber, Date dateOfBirth, 
                  String nationality, boolean isActive, int goals, int assists, int matchesPlayed) {
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
    public int getPlayerId() { return playerId; }
    public String getName() { return name; }
    public String getPosition() { return position; }
    public int getJerseyNumber() { return jerseyNumber; }
    public Date getDateOfBirth() { return dateOfBirth; }
    public String getNationality() { return nationality; }
    public boolean isActive() { return isActive; }
    public int getGoals() { return goals; }
    public int getAssists() { return assists; }
    public int getMatchesPlayed() { return matchesPlayed; }

    // Setters
    public void setPlayerId(int playerId) { this.playerId = playerId; }
    public void setName(String name) { this.name = name; }
    public void setPosition(String position) { this.position = position; }
    public void setJerseyNumber(int jerseyNumber) { this.jerseyNumber = jerseyNumber; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public void setActive(boolean isActive) { this.isActive = isActive; }
    public void setGoals(int goals) { this.goals = goals; }
    public void setAssists(int assists) { this.assists = assists; }
    public void setMatchesPlayed(int matchesPlayed) { this.matchesPlayed = matchesPlayed; }
}
