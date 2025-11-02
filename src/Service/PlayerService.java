package Service;

import Dao.PlayerDAO;
import Model.Player;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerService {
    private final PlayerDAO playerDAO;

    public PlayerService() {
        this.playerDAO = new PlayerDAO();
    }

    public List<Player> getAllPlayers() {
        return playerDAO.getAllPlayers();
    }

    public List<Player> searchPlayersByName(String searchTerm) {
        return playerDAO.searchPlayersByName(searchTerm);
    }

    public List<Player> getActivePlayers() {
        List<Player> allPlayers = getAllPlayers();
        if (allPlayers == null) return null;
        
        return allPlayers.stream()
                .filter(Player::isActive)
                .collect(Collectors.toList());
    }

    public Player getPlayerById(int playerId) {
        return playerDAO.getPlayerById(playerId);
    }

    public boolean savePlayer(Player player) {
        return playerDAO.saveOrUpdatePlayer(player);
    }

    public boolean updatePlayer(Player player) {
        return playerDAO.saveOrUpdatePlayer(player);
    }

    public boolean deletePlayer(int playerId) {
        return playerDAO.deletePlayer(playerId);
    }

    public List<Player> getTopScorers(int limit) {
        List<Player> allPlayers = getAllPlayers();
        if (allPlayers == null) return null;
        
        return allPlayers.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getGoals(), p1.getGoals()))
                .limit(limit)
                .collect(Collectors.toList());
    }
}