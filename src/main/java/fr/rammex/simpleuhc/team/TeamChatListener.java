package fr.rammex.simpleuhc.team;

import fr.rammex.simpleuhc.utils.LangMessages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Listener pour g\u00e9rer le chat d'\u00e9quipe
 */
public class TeamChatListener implements Listener {

    private final TeamManager teamManager;

    public TeamChatListener() {
        this.teamManager = new TeamManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();

        // V\u00e9rifier si le joueur est en mode team chat
        if (!TeamChatManager.isInTeamChatMode(sender)) {
            return; // Laisser le chat normal se d\u00e9rouler
        }

        // V\u00e9rifier si le joueur est dans une \u00e9quipe
        if (!teamManager.isPlayerInAnyTeam(sender)) {
            sender.sendMessage(LangMessages.getMessage("team.chat.not_in_team", null));
            event.setCancelled(true);
            return;
        }

        // R\u00e9cup\u00e9rer le nom de l'\u00e9quipe du joueur
        String teamName = teamManager.getPlayerTeamName(sender);
        if (teamName == null) {
            sender.sendMessage(LangMessages.getMessage("team.chat.not_in_team", null));
            event.setCancelled(true);
            return;
        }

        // R\u00e9cup\u00e9rer la couleur de l'\u00e9quipe
        TeamColor teamColor = teamManager.getTeamColor(teamName);
        String colorCode = teamManager.ConvertTeamColorToMinecraftCode(teamColor);

        // Formater le message pour le team chat
        String formattedMessage = LangMessages.getMessage("team.chat.format", null)
                .replace("{color}", colorCode)
                .replace("{team}", teamName)
                .replace("{player}", sender.getName())
                .replace("{message}", event.getMessage());

        // Annuler l'\u00e9v\u00e9nement pour \u00e9viter le broadcast global
        event.setCancelled(true);

        // Envoyer le message uniquement aux membres de l'\u00e9quipe
        sendMessageToTeam(teamName, formattedMessage, sender);
    }

    /**
     * Envoie un message \u00e0 tous les membres d'une \u00e9quipe
     */
    private void sendMessageToTeam(String teamName, String message, Player sender) {
        Map<Map<TeamColor, String>, List<Player>> teams = teamManager.getTeams();

        for (Map.Entry<Map<TeamColor, String>, List<Player>> entry : teams.entrySet()) {
            // V\u00e9rifier si c'est la bonne \u00e9quipe
            if (entry.getKey().containsValue(teamName)) {
                List<Player> teamMembers = entry.getValue();

                // Envoyer le message \u00e0 tous les membres de l'\u00e9quipe
                for (Player member : teamMembers) {
                    if (member != null && member.isOnline()) {
                        member.sendMessage(message);
                    }
                }
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Nettoyer les donn\u00e9es du joueur qui se d\u00e9connecte
        TeamChatManager.cleanup(event.getPlayer());
    }
}
