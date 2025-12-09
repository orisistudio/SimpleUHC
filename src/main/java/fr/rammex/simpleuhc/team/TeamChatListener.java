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

public class TeamChatListener implements Listener {

    private final TeamManager teamManager;

    public TeamChatListener() {
        this.teamManager = new TeamManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();

        if (!TeamChatManager.isInTeamChatMode(sender)) {
            return;
        }

        if (!teamManager.isPlayerInAnyTeam(sender)) {
            sender.sendMessage(LangMessages.getMessage("team.chat.not_in_team", null));
            event.setCancelled(true);
            return;
        }

        String teamName = teamManager.getPlayerTeamName(sender);
        if (teamName == null) {
            sender.sendMessage(LangMessages.getMessage("team.chat.not_in_team", null));
            event.setCancelled(true);
            return;
        }

        TeamColor teamColor = teamManager.getTeamColor(teamName);
        String colorCode = teamManager.ConvertTeamColorToMinecraftCode(teamColor);

        String formattedMessage = LangMessages.getMessage("team.chat.format", null)
                .replace("{color}", colorCode)
                .replace("{team}", teamName)
                .replace("{player}", sender.getName())
                .replace("{message}", event.getMessage());

        event.setCancelled(true);

        sendMessageToTeam(teamName, formattedMessage, sender);
    }

    private void sendMessageToTeam(String teamName, String message, Player sender) {
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

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        TeamChatManager.cleanup(event.getPlayer());
    }
}
