package fr.rammex.simpleuhc.commands;

import fr.rammex.simpleuhc.team.TeamManager;
import fr.rammex.simpleuhc.team.gui.TeamCreationGUI;
import fr.rammex.simpleuhc.team.gui.TeamsGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommand implements CommandExecutor {
    private static TeamManager teamManager = new TeamManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        if(args.length != 0){
            String subcommand = args[0].toLowerCase();
            if(subcommand.equals("create")){
                TeamCreationGUI.setupGUI(player);
            } else if (subcommand.equals("invite")){
                if(teamManager.isPlayerTeamLeader(player)){
                    if(args.length >= 2){
                        Player targetPlayerName = Bukkit.getPlayer(args[1]);
                        if(targetPlayerName == null || !targetPlayerName.isOnline()){
                            player.sendMessage("§cLe joueur spécifié est introuvable ou hors ligne.");
                            return true;
                        }
                        String teamName = teamManager.getPlayerTeamName(player);
                        teamManager.invitePlayerToTeam(targetPlayerName, teamName);
                        player.sendMessage("§aVous avez invité " + targetPlayerName.getName() + " à rejoindre l'équipe " + teamName + ".");
                        targetPlayerName.sendMessage("§aVous avez été invité à rejoindre l'équipe " + teamName + " par " + player.getName() + ". Utilisez /team accept " +teamName + " pour accepter l'invitation.");
                    } else {
                        player.sendMessage("§cUsage: /team invite <player>");
                    }
                } else {
                    player.sendMessage("§cVous devez être le chef d'une équipe pour inviter des joueurs.");
                }
            } else if (subcommand.equals("accept")){
                if(args.length >= 2){
                    String teamName = args[1];
                    try {
                        teamManager.addPlayerToTeam(teamName, player);
                        player.sendMessage("§aVous avez rejoint l'équipe " + teamName + ".");
                    } catch (IllegalArgumentException e) {
                        player.sendMessage("§c" + e.getMessage());
                    }
                } else {
                    player.sendMessage("§cUsage: /team accept <team>");
                }
            } else if (subcommand.equals("setleader")){
                if(teamManager.isPlayerTeamLeader(player)){
                    if(args.length >= 2){
                        Player targetPlayerName = Bukkit.getPlayer(args[1]);
                        if(targetPlayerName == null || !targetPlayerName.isOnline()){
                            player.sendMessage("§cLe joueur spécifié est introuvable ou hors ligne.");
                            return true;
                        }
                        String teamName = teamManager.getPlayerTeamName(player);
                        if (!teamManager.isPlayerInTeam(teamName, targetPlayerName)) {
                            player.sendMessage("§cLe joueur spécifié n'est pas dans votre équipe.");
                            return true;
                        }
                        try {
                            teamManager.changeTeamLeader(teamName, targetPlayerName);
                            player.sendMessage("§aVous avez transféré la direction de l'équipe à " + targetPlayerName.getName() + ".");
                            targetPlayerName.sendMessage("§aVous êtes maintenant le chef de l'équipe " + teamName + ".");
                        } catch (IllegalArgumentException e) {
                            player.sendMessage("§c" + e.getMessage());
                        }
                    } else {
                        player.sendMessage("§cUsage: /team setleader <player>");
                    }
                } else {
                    player.sendMessage("§cVous devez être le chef d'une équipe pour transférer la direction.");
                }
            }

            else {
                player.sendMessage("§cUsage: /team <create|invite|setleader>");
            }
        } else {
            TeamsGUI.setupGUI(player);
        }

        return false;
    }
}
