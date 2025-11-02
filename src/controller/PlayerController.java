package controller;

import Service.PlayerService;
import Model.Player;
import java.util.List;

public class PlayerController {
    private final PlayerService playerService;

    public PlayerController() {
        this.playerService = new PlayerService();
    }

    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    public List<Player> searchPlayers(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return playerService.getAllPlayers();
        }
        return playerService.searchPlayersByName(searchTerm);
    }

    public List<Player> getActivePlayers() {
        return playerService.getActivePlayers();
    }

    public Player getPlayerById(int playerId) {
        return playerService.getPlayerById(playerId);
    }

    public boolean addPlayer(Player player) {
        return playerService.savePlayer(player);
    }

    public boolean updatePlayer(Player player) {
        return playerService.updatePlayer(player);
    }

    public boolean deletePlayer(int playerId) {
        return playerService.deletePlayer(playerId);
    }

    public List<Player> getTopScorers(int limit) {
        return playerService.getTopScorers(limit);
    }

    public List<Player> getPlayersByPosition(String position) {
        List<Player> allPlayers = playerService.getAllPlayers();
        if (allPlayers == null) return null;
        
        return allPlayers.stream()
                .filter(p -> p.getPosition().equalsIgnoreCase(position))
                .collect(java.util.stream.Collectors.toList());
    }

   
}