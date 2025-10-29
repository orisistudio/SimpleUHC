package fr.rammex.simpleuhc.commands;

import fr.rammex.simpleuhc.SimpleUHC;
import fr.rammex.simpleuhc.world.WorldManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SimpleUHCcommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }

        Player player = (Player) sender;

        String message = "§6§lSimpleUHC §r§7- §eA simple UHC plugin by Rammex\n\n" +
                "§eCommands:\n" +
                "§6/uhc start §7- §eStart the UHC game\n" +
                "§6/uhc stop §7- §eStop the UHC game\n" +
                "§6/uhc tp §7- §eTeleport to the UHC world\n" +
                "§6/team §7- §eOpen the team management GUI\n";

        if(args.length == 0){
            player.sendMessage(message);
            return true;
        } else {
            String subcommand = args[0].toLowerCase();
            if(subcommand.equals("start")){
                SimpleUHC.getSimpleUHCManager().onEnable();
            }
            else if(subcommand.equals("stop")){
                SimpleUHC.getSimpleUHCManager().onDisable();
            }
            else if(subcommand.equals("tp")){
                WorldManager.teleportPlayer(player);
            }
            else if(subcommand.equals("reload")){
                SimpleUHC.instance.reloadConfig();
                SimpleUHC.instance.getLangMessages().loadMessages();
                player.sendMessage("§6§lSimpleUHC §r§7- §eConfiguration reloaded.");
            }

            else {
                player.sendMessage(message);
            }
        }
        return false;
    }
}