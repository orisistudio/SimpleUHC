package fr.rammex.simpleuhc.utils;

import fr.rammex.simpleuhc.team.TeamManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class TeamPlaceHolder extends PlaceholderExpansion {
    TeamManager teamManager = new TeamManager();
    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "simpleuhc";
    }

    @Override
    public String getAuthor() {
        return ".rammex";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        if (identifier.startsWith("team_name")){
            String teamName = teamManager.getPlayerTeamName(player);
            if (teamName != null) {
                return teamName;
            } else {
                return "";
            }

        }

        if (identifier.startsWith("team_color")){
            String teamColor = teamManager.ConvertTeamColorToMinecraftCode(teamManager.getTeamColor(teamManager.getPlayerTeamName(player)));
            if (teamColor != null) {
                return teamColor;
            } else {
                return "";
            }

        }
        return null;
    }
}
