package fr.rammex.simpleuhc.commands;

import fr.rammex.simpleuhc.team.TeamChatManager;
import fr.rammex.simpleuhc.team.TeamManager;
import fr.rammex.simpleuhc.utils.LangMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Commande /tc pour basculer entre le chat global et le chat d'\u00e9quipe
 */
public class TeamChatCommand implements CommandExecutor {

    private final TeamManager teamManager;

    public TeamChatCommand() {
        this.teamManager = new TeamManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("\u00a7cCette commande ne peut \u00eatre ex\u00e9cut\u00e9e que par un joueur.");
            return true;
        }

        Player player = (Player) sender;

        // V\u00e9rifier si le joueur est dans une \u00e9quipe
        if (!teamManager.isPlayerInAnyTeam(player)) {
            player.sendMessage(LangMessages.getMessage("team.chat.not_in_team", null));
            return true;
        }

        // Basculer le mode team chat
        TeamChatManager.toggleTeamChat(player);

        // Informer le joueur
        if (TeamChatManager.isInTeamChatMode(player)) {
            player.sendMessage(LangMessages.getMessage("team.chat.enabled", null));
        } else {
            player.sendMessage(LangMessages.getMessage("team.chat.disabled", null));
        }

        return true;
    }
}
