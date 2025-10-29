// TeamManager.java
package fr.rammex.simpleuhc.team;

import fr.rammex.simpleuhc.option.OptionSetup;
import fr.rammex.simpleuhc.utils.LangMessages;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
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

    // Inventaires partagés par nom d'équipe
    private static Map<String, Inventory> teamInventories = new HashMap<>();

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
            throw new IllegalArgumentException(LangMessages.getMessage("team.team.name_not_valid", null));
        }
        if (isNameInUse(name)) {
            throw new IllegalArgumentException(LangMessages.getMessage("team.team.name_already_used", null));
        }
        if (players.size() > teamSize) {
            throw new IllegalArgumentException(LangMessages.getMessage("team.team.team_member_over_limite", null));
        }
        Map<TeamColor, String> teamInfo = new HashMap<>();
        teamInfo.put(color, name);
        teams.put(teamInfo, players);
        teamLeaders.put(name, players.get(0)); // Le premier joueur de la liste est le leader

        // Crée un inventaire partagé pour cette équipe
        int invSize = Math.max(9, ((teamSize + 8) / 9) * 9);
        Inventory inv = Bukkit.createInventory(null, invSize, "Inventaire équipe : " + name);
        teamInventories.put(name, inv);
    }

    public void disbandTeam(String teamName) throws IllegalArgumentException {
        for (Map<TeamColor, String> teamInfo : teams.keySet()) {
            if (teamInfo.containsValue(teamName)) {
                teams.remove(teamInfo);
                teamLeaders.remove(teamName);
                teamInventories.remove(teamName); // supprime l'inventaire partagé
                return;
            }
        }
        throw new IllegalArgumentException(LangMessages.getMessage("team.team.not_existing", null));
    }

    public Map<Map<TeamColor, String>, List<Player>> getTeams() {
        return teams;
    }

    public void addPlayerToTeam(String teamName, Player player) throws IllegalArgumentException {
        for (Map<TeamColor, String> teamInfo : teams.keySet()) {
            if (teamInfo.containsValue(teamName)) {
                List<Player> players = teams.get(teamInfo);
                if (players.size() >= teamSize) {
                    throw new IllegalArgumentException(LangMessages.getMessage("team.team.team_full", null));
                }
                if (players.contains(player)) {
                    throw new IllegalArgumentException(LangMessages.getMessage("team.player.player_already_in_team", null));
                }
                players.add(player);
                return;
            }
        }
        throw new IllegalArgumentException(LangMessages.getMessage("team.team.not_existing", null));
    }

    public void removePlayerFromTeam(String teamName, Player player) throws IllegalArgumentException {
        for (Map<TeamColor, String> teamInfo : teams.keySet()) {
            if (teamInfo.containsValue(teamName)) {
                List<Player> players = teams.get(teamInfo);
                if (!players.contains(player)) {
                    throw new IllegalArgumentException(LangMessages.getMessage("team.player.no_in_team", null));
                }
                players.remove(player);
                return;
            }
        }
        throw new IllegalArgumentException(LangMessages.getMessage("team.team.not_existing", null));
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
            throw new IllegalArgumentException(LangMessages.getMessage("team.player.no_invite_pending", null));
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
            throw new IllegalArgumentException(LangMessages.getMessage("team.team.no_in_team", null));
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
                return "§f";
        }
    }

    /**
     * Retourne (et crée si nécessaire) l'inventaire partagé pour une équipe.
     * @param teamName nom de l'équipe
     * @param requestedTeamSize taille maximale de l'équipe (utilisée pour calculer la taille de l'inventaire)
     * @return Inventory partagé
     */
    public Inventory getTeamInventory(String teamName, int requestedTeamSize) {
        Inventory existing = teamInventories.get(teamName);
        int desiredSize = Math.max(9, ((requestedTeamSize + 8) / 9) * 9);
        if (existing == null) {
            Inventory inv = Bukkit.createInventory(null, desiredSize, "Inventaire équipe : " + teamName);
            teamInventories.put(teamName, inv);
            return inv;
        }
        if (existing.getSize() != desiredSize) {
            // recrée en conservant les items existants
            Inventory inv = Bukkit.createInventory(null, desiredSize, "Inventaire équipe : " + teamName);
            int copyLimit = Math.min(existing.getSize(), inv.getSize());
            for (int i = 0; i < copyLimit; i++) {
                inv.setItem(i, existing.getItem(i));
            }
            teamInventories.put(teamName, inv);
            return inv;
        }
        return existing;
    }
}