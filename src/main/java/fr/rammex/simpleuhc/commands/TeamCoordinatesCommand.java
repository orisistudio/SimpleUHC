package fr.rammex.simpleuhc.commands;

import fr.rammex.simpleuhc.team.TeamColor;
import fr.rammex.simpleuhc.team.TeamManager;
import fr.rammex.simpleuhc.utils.LangMessages;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * Commande /tcc pour envoyer ses coordonnées dans le chat d'équipe
 */
public class TeamCoordinatesCommand implements CommandExecutor {

    private final TeamManager teamManager;

    public TeamCoordinatesCommand() {
        this.teamManager = new TeamManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cCette commande ne peut être exécutée que par un joueur.");
            return true;
        }

        Player player = (Player) sender;

        if (!teamManager.isPlayerInAnyTeam(player)) {
            player.sendMessage(LangMessages.getMessage("team.chat.not_in_team", null));
            return true;
        }

        Location loc = player.getLocation();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        String teamName = teamManager.getPlayerTeamName(player);
        if (teamName == null) {
            player.sendMessage(LangMessages.getMessage("team.chat.not_in_team", null));
            return true;
        }

        TeamColor teamColor = teamManager.getTeamColor(teamName);
        String colorCode = teamManager.ConvertTeamColorToMinecraftCode(teamColor);

        String coordinatesMessage = String.format("§7[§eTeam§7] %s%s §8» §f%s §7: §aX: %d §aY: %d §aZ: %d",
                colorCode, teamName, player.getName(), x, y, z);

        sendMessageToTeam(teamName, coordinatesMessage);

        return true;
    }

    private void sendMessageToTeam(String teamName, String message) {
        Map<Map<TeamColor, String>, List<Player>> teams = teamManager.getTeams();

        for (Map.Entry<Map<TeamColor, String>, List<Player>> entry : teams.entrySet()) {
            if (entry.getKey().containsValue(teamName)) {
                List<Player> teamMembers = entry.getValue();

                for (Player member : teamMembers) {
                    if (member != null && member.isOnline()) {
                        member.sendMessage(message);
                    }
                }
                return;
            }
        }
    }
}
