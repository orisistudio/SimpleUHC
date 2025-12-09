package fr.rammex.simpleuhc.commands;

import fr.rammex.simpleuhc.config.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

public class ConfigCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("simpleuhc.admin")) {
            sender.sendMessage("&cVous n'avez pas la permission d'utiliser cette commande.");
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
            sender.sendMessage("&cUsage: /uhcconfig save <nom>");
            return;
        }

        String configName = args[1];

        if (ConfigManager.configExists(configName)) {
            sender.sendMessage("&cUne configuration avec ce nom existe déjà. Utilisez /uhcconfig delete " + configName + " d'abord.");
            return;
        }

        if (ConfigManager.saveConfig(configName)) {
            sender.sendMessage("&aConfiguration '&6" + configName + "&a' sauvegardée avec succés !");
        } else {
            sender.sendMessage("&cErreur lors de la sauvegarde de la configuration. Vérifiez que le nom est valide.");
        }
    }

    private void handleLoad(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("&cUsage: /uhcconfig load <nom>");
            return;
        }

        String configName = args[1];

        if (!ConfigManager.configExists(configName)) {
            sender.sendMessage("&cLa configuration '&6" + configName + "&cn'existe pas.");
            return;
        }

        if (ConfigManager.loadConfig(configName)) {
            sender.sendMessage("&aConfiguration '&6" + configName + "&a' chargée avec succès !");
        } else {
            sender.sendMessage("&cErreur lors du chargement de la configuration.");
        }
    }

    private void handleDelete(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("&cUsage: /uhcconfig delete <nom>");
            return;
        }

        String configName = args[1];

        if (!ConfigManager.configExists(configName)) {
            sender.sendMessage("&cLa configuration '&6" + configName + "&c' n'existe pas.");
            return;
        }

        if (ConfigManager.deleteConfig(configName)) {
            sender.sendMessage("&aConfiguration '&6" + configName + "&a' supprimée avec succès !");
        } else {
            sender.sendMessage("&cErreur lors de la suppression de la configuration.");
        }
    }

    private void handleList(CommandSender sender) {
        List<String> configs = ConfigManager.getConfigList();

        if (configs.isEmpty()) {
            sender.sendMessage("&eAucune configuration sauvegardée.");
            return;
        }

        sender.sendMessage("&6&lConfigurations disponibles:");
        for (String config : configs) {
            sender.sendMessage("&e  - &a" + config);
        }
    }

    private void handleInfo(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("&cUsage: /uhcconfig info <nom>");
            return;
        }

        String configName = args[1];
        Map<String, Object> info = ConfigManager.getConfigInfo(configName);

        if (info == null) {
            sender.sendMessage("&cLa configuration '&6" + configName + "&c' n'existe pas.");
            return;
        }

        sender.sendMessage("&6&lInformations sur '&e" + configName + "&6&l':");
        sender.sendMessage("&e  Nom: &a" + info.get("name"));

        long created = (long) info.get("created");
        if (created > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            sender.sendMessage("&e  Créée le: &a" + sdf.format(new Date(created)));
        }

        sender.sendMessage("&e  Version: &a" + info.get("version"));
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("&6&l===== Gestion des Configurations UHC =====");
        sender.sendMessage("&7e/uhcconfig save <nom> &7- Sauvegarde la configuration actuelle");
        sender.sendMessage("&e/uhcconfig load <nom> &7- Charge une configuration");
        sender.sendMessage("&e/uhcconfig delete <nom> &7- Supprime une configuration");
        sender.sendMessage("&e/uhcconfig list &7- Liste toutes les configurations");
        sender.sendMessage("&e/uhcconfig info <nom> &7- Affiche les infos d'une configuration");
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
