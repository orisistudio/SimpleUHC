package fr.rammex.simpleuhc.commands;

import fr.rammex.simpleuhc.SimpleUHC;
import fr.rammex.simpleuhc.team.TeamManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TeamInventoryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande doit être exécutée par un joueur.");
            return true;
        }

        Player player = (Player) sender;

        TeamManager teamManager = new TeamManager();

        if (!TeamManager.isTeamActivated()) {
            player.sendMessage("Les équipes sont désactivées.");
            return true;
        }

        if (!SimpleUHC.instance.getModuleManager().isModuleEnabled("TeamInventory")) {
            player.sendMessage("Le module d'inventaire d'équipe est désactivé.");
            return true;
        }

        String teamName = teamManager.getPlayerTeamName(player);
        if (teamName == null) {
            player.sendMessage("Vous n'êtes dans aucune équipe.");
            return true;
        }

        int maxTeamSize = teamManager.getTeamSize();
        Inventory inv = teamManager.getTeamInventory(teamName, maxTeamSize);

        player.openInventory(inv);
        return true;
    }
}