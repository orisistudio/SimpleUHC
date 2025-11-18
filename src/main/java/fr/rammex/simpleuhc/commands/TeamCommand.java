package fr.rammex.simpleuhc.commands;

import fr.rammex.simpleuhc.team.TeamManager;
import fr.rammex.simpleuhc.team.gui.TeamCreationGUI;
import fr.rammex.simpleuhc.team.gui.TeamsGUI;
import fr.rammex.simpleuhc.utils.LangMessages;
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
                            player.sendMessage(LangMessages.getMessage("commands.team.player_not_found", null));
                            return true;
                        }
                        String teamName = teamManager.getPlayerTeamName(player);
                        teamManager.invitePlayerToTeam(targetPlayerName, teamName);
                        player.sendMessage(
                                LangMessages.getMessage("commands.team.player_invited", null)
                                        .replace("{targetPlayerName}", targetPlayerName.getName())
                                        .replace("{team}", teamName)
                        );
                        targetPlayerName.sendMessage(
                                LangMessages.getMessage("commands.team.invite_received", null)
                                        .replace("{team}", teamName)
                                        .replace("{player}", player.getName())
                        );
                    } else {
                        player.sendMessage("§cUsage: /team invite <player>");
                    }
                } else {
                    player.sendMessage(LangMessages.getMessage("commands.team.not_team_leader_invite", null));
                }
            } else if (subcommand.equals("accept")){
                if(args.length >= 2){
                    String teamName = args[1];
                    try {
                        // Utiliser acceptTeamInvite au lieu de addPlayerToTeam pour v\u00e9rifier l'invitation
                        teamManager.acceptTeamInvite(player, teamName);
                        player.sendMessage(
                                LangMessages.getMessage("commands.team.team_joined", null)
                                        .replace("{team}", teamName)
                        );
                    } catch (IllegalArgumentException e) {
                        player.sendMessage("\u00a7c" + e.getMessage());
                    }
                } else {
                    player.sendMessage("\u00a7cUsage: /team accept <team>");
                }
            } else if (subcommand.equals("setleader")){
                if(teamManager.isPlayerTeamLeader(player)){
                    if(args.length >= 2){
                        Player targetPlayerName = Bukkit.getPlayer(args[1]);
                        if(targetPlayerName == null || !targetPlayerName.isOnline()){
                            player.sendMessage(LangMessages.getMessage("commands.team.player_not_found", null));
                            return true;
                        }
                        String teamName = teamManager.getPlayerTeamName(player);
                        if (!teamManager.isPlayerInTeam(teamName, targetPlayerName)) {
                            player.sendMessage(LangMessages.getMessage("team.player.no_in_team", null));
                            return true;
                        }
                        try {
                            teamManager.changeTeamLeader(teamName, targetPlayerName);
                            player.sendMessage(
                                    LangMessages.getMessage("commands.team.team_leader_transferred", null)
                                            .replace("{targetPlayerName}", targetPlayerName.getName())
                            );
                            targetPlayerName.sendMessage(
                                    LangMessages.getMessage("commands.team.new_team_leader", null)
                                            .replace("{teamName}", teamName)
                            );
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