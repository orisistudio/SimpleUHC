package fr.rammex.simpleuhc.team;

import fr.rammex.simpleuhc.option.OptionSetup;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamManager {
    private static boolean teamActivated = (boolean) OptionSetup.getOption("Game Team").getValue();
    private int teamSize = (int) OptionSetup.getOption("Game Team Size").getValue();
    private Map<Player, String> teamInvites = new HashMap<>(); // Map pour gérer les invitations des joueurs aux équipes, Le string est le nom de la team
    private static Map<Map<TeamColor,String>, List<Player>> teams = new HashMap<>(); // Map première map pour la couleur et Nom de la team et la deuxième liste pour les joueurs dans la team
    private static Map<String, Player> teamLeaders = new HashMap<>(); // Map pour gérer les leaders des équipes, Le string est le nom de la team

    public int getTeamSize() {
        return teamSize;
    }

    private static boolean isNameInUse(String name) {
        for (Map<TeamColor, String> teamInfo : teams.keySet()) {
            if (teamInfo.containsValue(name)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 16 && name.matches("^[a-zA-Z0-9_]+$");
    }

    public static void createTeam(TeamColor color, String name, List<Player> players, int teamSize) throws IllegalArgumentException {
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
        teamLeaders.put(name, players.get(0)); // Le premier joueur de la liste est le leader
    }

    public void disbandTeam(String teamName) throws IllegalArgumentException {
        for (Map<TeamColor, String> teamInfo : teams.keySet()) {
            if (teamInfo.containsValue(teamName)) {
                teams.remove(teamInfo);
                teamLeaders.remove(teamName);
                return;
            }
        }
        throw new IllegalArgumentException("L'équipe n'existe pas.");
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

    public void removePlayerFromTeam(String teamName, Player player) throws IllegalArgumentException {
        for (Map<TeamColor, String> teamInfo : teams.keySet()) {
            if (teamInfo.containsValue(teamName)) {
                List<Player> players = teams.get(teamInfo);
                if (!players.contains(player)) {
                    throw new IllegalArgumentException("Le joueur n'est pas dans l'équipe.");
                }
                players.remove(player);
                return;
            }
        }
        throw new IllegalArgumentException("L'équipe n'existe pas.");
    }

    public static boolean isTeamActivated(){
        return (boolean) OptionSetup.getOption("Game Team").getValue();
    }

    public void invitePlayerToTeam(Player player, String teamName) {
        teamInvites.put(player, teamName);
    }

    public void acceptTeamInvite(Player player, String teamName) {
        if (teamInvites.containsKey(player) && teamInvites.get(player).equals(teamName)) {
            addPlayerToTeam(teamName, player);
            teamInvites.remove(player);
        } else {
            throw new IllegalArgumentException("Aucune invitation en attente pour cette équipe.");
        }
    }

    public boolean hasPendingInvite(Player player) {
        return teamInvites.containsKey(player);
    }

    public boolean isPlayerInTeam(String teamName, Player player) {
        for (Map<TeamColor, String> teamInfo : teams.keySet()) {
            if (teamInfo.containsValue(teamName)) {
                List<Player> players = teams.get(teamInfo);
                return players.contains(player);
            }
        }
        return false;
    }

    public boolean isPlayerInvitedToTeam(String teamName, Player player) {
        return teamInvites.containsKey(player) && teamInvites.get(player).equals(teamName);
    }

    public boolean isPlayerInAnyTeam(Player player) {
        for (List<Player> players : teams.values()) {
            if (players.contains(player)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPlayerTeamLeader(Player player) {
        return teamLeaders.containsValue(player);
    }

    public String getPlayerTeamName(Player player) {
        for (Map.Entry<Map<TeamColor, String>, List<Player>> entry : teams.entrySet()) {
            if (entry.getValue().contains(player)) {
                return entry.getKey().values().iterator().next();
            }
        }
        return null;
    }

    public boolean isPlayerAloneInTeam(Player player) {
        String teamName = getPlayerTeamName(player);
        if (teamName != null) {
            for (Map.Entry<Map<TeamColor, String>, List<Player>> entry : teams.entrySet()) {
                if (entry.getKey().containsValue(teamName)) {
                    return entry.getValue().size() == 1;
                }
            }
        }
        return false;
    }

    public void changeTeamLeader(String teamName, Player newLeader) throws IllegalArgumentException {
        if (!isPlayerInTeam(teamName, newLeader)) {
            throw new IllegalArgumentException("Le joueur n'est pas dans l'équipe.");
        }
        teamLeaders.put(teamName, newLeader);
    }

    public TeamColor getTeamColor(String teamName) {
        for (Map<TeamColor, String> teamInfo : teams.keySet()) {
            if (teamInfo.containsValue(teamName)) {
                return teamInfo.keySet().iterator().next();
            }
        }
        return null;
    }

    public String ConvertTeamColorToMinecraftCode(TeamColor color){
        switch (color) {
            case RED:
                return "§c";
            case BLUE:
                return "§9";
            case GREEN:
                return "§a";
            case YELLOW:
                return "§e";
            case PURPLE:
                return "§5";
            case ORANGE:
                return "§6";
            case PINK:
                return "§d";
            case CYAN:
                return "§b";
            case WHITE:
                return "§f";
            case BLACK:
                return "§0";
            default:
                return "§f"; // Default to white if color not recognized
        }
    }
}
