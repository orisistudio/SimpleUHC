package fr.rammex.simpleuhc.commands;

import fr.rammex.simpleuhc.config.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Commande /uhcconfig pour g\u00e9rer les configurations
 */
public class ConfigCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("simpleuhc.admin")) {
            sender.sendMessage("\u00a7cVous n'avez pas la permission d'utiliser cette commande.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "save":
                handleSave(sender, args);
                break;

            case "load":
                handleLoad(sender, args);
                break;

            case "delete":
                handleDelete(sender, args);
                break;

            case "list":
                handleList(sender);
                break;

            case "info":
                handleInfo(sender, args);
                break;

            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void handleSave(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("\u00a7cUsage: /uhcconfig save <nom>");
            return;
        }

        String configName = args[1];

        if (ConfigManager.configExists(configName)) {
            sender.sendMessage("\u00a7cUne configuration avec ce nom existe d\u00e9j\u00e0. Utilisez /uhcconfig delete " + configName + " d'abord.");
            return;
        }

        if (ConfigManager.saveConfig(configName)) {
            sender.sendMessage("\u00a7aConfiguration '\u00a76" + configName + "\u00a7a' sauvegard\u00e9e avec succ\u00e8s !");
        } else {
            sender.sendMessage("\u00a7cErreur lors de la sauvegarde de la configuration. V\u00e9rifiez que le nom est valide.");
        }
    }

    private void handleLoad(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("\u00a7cUsage: /uhcconfig load <nom>");
            return;
        }

        String configName = args[1];

        if (!ConfigManager.configExists(configName)) {
            sender.sendMessage("\u00a7cLa configuration '\u00a76" + configName + "\u00a7c' n'existe pas.");
            return;
        }

        if (ConfigManager.loadConfig(configName)) {
            sender.sendMessage("\u00a7aConfiguration '\u00a76" + configName + "\u00a7a' charg\u00e9e avec succ\u00e8s !");
        } else {
            sender.sendMessage("\u00a7cErreur lors du chargement de la configuration.");
        }
    }

    private void handleDelete(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("\u00a7cUsage: /uhcconfig delete <nom>");
            return;
        }

        String configName = args[1];

        if (!ConfigManager.configExists(configName)) {
            sender.sendMessage("\u00a7cLa configuration '\u00a76" + configName + "\u00a7c' n'existe pas.");
            return;
        }

        if (ConfigManager.deleteConfig(configName)) {
            sender.sendMessage("\u00a7aConfiguration '\u00a76" + configName + "\u00a7a' supprim\u00e9e avec succ\u00e8s !");
        } else {
            sender.sendMessage("\u00a7cErreur lors de la suppression de la configuration.");
        }
    }

    private void handleList(CommandSender sender) {
        List<String> configs = ConfigManager.getConfigList();

        if (configs.isEmpty()) {
            sender.sendMessage("\u00a7eAucune configuration sauvegard\u00e9e.");
            return;
        }

        sender.sendMessage("\u00a76\u00a7lConfigurations disponibles:");
        for (String config : configs) {
            sender.sendMessage("\u00a7e  - \u00a7a" + config);
        }
    }

    private void handleInfo(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("\u00a7cUsage: /uhcconfig info <nom>");
            return;
        }

        String configName = args[1];
        Map<String, Object> info = ConfigManager.getConfigInfo(configName);

        if (info == null) {
            sender.sendMessage("\u00a7cLa configuration '\u00a76" + configName + "\u00a7c' n'existe pas.");
            return;
        }

        sender.sendMessage("\u00a76\u00a7lInformations sur '\u00a7e" + configName + "\u00a76\u00a7l':");
        sender.sendMessage("\u00a7e  Nom: \u00a7a" + info.get("name"));

        long created = (long) info.get("created");
        if (created > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            sender.sendMessage("\u00a7e  Cr\u00e9\u00e9e le: \u00a7a" + sdf.format(new Date(created)));
        }

        sender.sendMessage("\u00a7e  Version: \u00a7a" + info.get("version"));
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("\u00a76\u00a7l===== Gestion des Configurations UHC =====");
        sender.sendMessage("\u00a7e/uhcconfig save <nom> \u00a77- Sauvegarde la configuration actuelle");
        sender.sendMessage("\u00a7e/uhcconfig load <nom> \u00a77- Charge une configuration");
        sender.sendMessage("\u00a7e/uhcconfig delete <nom> \u00a77- Supprime une configuration");
        sender.sendMessage("\u00a7e/uhcconfig list \u00a77- Liste toutes les configurations");
        sender.sendMessage("\u00a7e/uhcconfig info <nom> \u00a77- Affiche les infos d'une configuration");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Arrays.asList("save", "load", "delete", "list", "info"));
        } else if (args.length == 2) {
            String subCommand = args[0].toLowerCase();
            if (subCommand.equals("load") || subCommand.equals("delete") || subCommand.equals("info")) {
                completions.addAll(ConfigManager.getConfigList());
            }
        }

        return completions;
    }
}
