package fr.rammex.simpleuhc.team;

import fr.rammex.simpleuhc.option.OptionSetup;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamManager {
    private boolean teamActivated = (boolean) OptionSetup.getOption("Game Team").getValue();
    private int teamSize = (int) OptionSetup.getOption("Game Team Size").getValue();
    private Map<Map<TeamColor,String>, List<Player>> teams = new HashMap<>(); // Map première map pour la couleur et Nom de la team et la deuxième liste pour les joueurs dans la team


    public boolean isTeamActivated() {
        return teamActivated;
    }

    public int getTeamSize() {
        return teamSize;
    }

    private boolean isNameInUse(String name) {
        for (Map<TeamColor, String> teamInfo : teams.keySet()) {
            if (teamInfo.containsValue(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 16 && name.matches("^[a-zA-Z0-9_]+$");
    }

    public void createTeam(TeamColor color, String name, List<Player> players, int teamSize) throws IllegalArgumentException {
        if (!isNameValid(name)) {
            throw new IllegalArgumentException("Le nom de l'équipe n'est pas valide.");
        }
        if (isNameInUse(name)) {
            throw new IllegalArgumentException("Le nom de l'équipe est déjà utilisé.");
        }
        if (players.size() > teamSize) {
            throw new IllegalArgumentException("Le nombre de joueurs dépasse la taille maximale de l'équipe.");
        }
        Map<TeamColor, String> teamInfo = new HashMap<>();
        teamInfo.put(color, name);
        teams.put(teamInfo, players);
    }

    public Map<Map<TeamColor, String>, List<Player>> getTeams() {
        return teams;
    }

    public void addPlayerToTeam(String teamName, Player player) throws IllegalArgumentException {
        for (Map<TeamColor, String> teamInfo : teams.keySet()) {
            if (teamInfo.containsValue(teamName)) {
                List<Player> players = teams.get(teamInfo);
                if (players.size() >= teamSize) {
                    throw new IllegalArgumentException("L'équipe est déjà pleine.");
                }
                if (players.contains(player)) {
                    throw new IllegalArgumentException("Le joueur est déjà dans l'équipe.");
                }
                players.add(player);
                return;
            }
        }
        throw new IllegalArgumentException("L'équipe n'existe pas.");
    }
}
