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

        // Vérifier si le joueur est dans une équipe
        if (!teamManager.isPlayerInAnyTeam(player)) {
            player.sendMessage(LangMessages.getMessage("team.chat.not_in_team", null));
            return true;
        }

        // Récupérer les coordonnées du joueur
        Location loc = player.getLocation();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        // Récupérer le nom de l'équipe du joueur
        String teamName = teamManager.getPlayerTeamName(player);
        if (teamName == null) {
            player.sendMessage(LangMessages.getMessage("team.chat.not_in_team", null));
            return true;
        }

        // Récupérer la couleur de l'équipe
        TeamColor teamColor = teamManager.getTeamColor(teamName);
        String colorCode = teamManager.ConvertTeamColorToMinecraftCode(teamColor);

        // Formater le message avec les coordonnées
        String coordinatesMessage = String.format("§7[§eTeam§7] %s%s §8» §f%s §7: §aX: %d §aY: %d §aZ: %d",
                colorCode, teamName, player.getName(), x, y, z);

        // Envoyer le message à tous les membres de l'équipe
        sendMessageToTeam(teamName, coordinatesMessage);

        return true;
    }

    /**
     * Envoie un message à tous les membres d'une équipe
     */
    private void sendMessageToTeam(String teamName, String message) {
        Map<Map<TeamColor, String>, List<Player>> teams = teamManager.getTeams();

        for (Map.Entry<Map<TeamColor, String>, List<Player>> entry : teams.entrySet()) {
            // Vérifier si c'est la bonne équipe
            if (entry.getKey().containsValue(teamName)) {
                List<Player> teamMembers = entry.getValue();

                // Envoyer le message à tous les membres de l'équipe
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
