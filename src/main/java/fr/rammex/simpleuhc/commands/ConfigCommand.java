package fr.rammex.simpleuhc.commands;

import fr.rammex.simpleuhc.config.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
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

            case "copy":
                handleCopy(sender, args);
                break;

            case "rename":
                handleRename(sender, args);
                break;

            case "export":
                handleExport(sender, args);
                break;

            case "import":
                handleImport(sender, args);
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

        sender.sendMessage("&6&l===== Informations sur '&e" + configName + "&6&l' =====");
        sender.sendMessage("&e  Nom: &a" + info.get("name"));

        long created = (long) info.get("created");
        if (created > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            sender.sendMessage("&e  Créée le: &a" + sdf.format(new Date(created)));
        }

        sender.sendMessage("&e  Version: &a" + info.get("version"));
        sender.sendMessage("&e  Description: &a" + info.get("description"));
        sender.sendMessage("&e  Options: &a" + info.get("optionCount"));
        sender.sendMessage("&e  Modules: &a" + info.get("moduleCount"));
        sender.sendMessage("&e  Taille: &a" + ConfigManager.getConfigFileSize(configName));

        List<String> modules = (List<String>) info.get("modules");
        if (modules != null && !modules.isEmpty()) {
            sender.sendMessage("&e  Modules activés: &a" + String.join(", ", modules));
        }
    }

    private void handleCopy(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("&cUsage: /uhcconfig copy <source> <destination>");
            return;
        }

        String sourceName = args[1];
        String targetName = args[2];

        if (ConfigManager.copyConfig(sourceName, targetName)) {
            sender.sendMessage("&aConfiguration '&6" + sourceName + "&a' copiée vers '&6" + targetName + "&a' avec succès !");
        } else {
            sender.sendMessage("&cErreur lors de la copie de la configuration.");
        }
    }

    private void handleRename(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("&cUsage: /uhcconfig rename <ancien_nom> <nouveau_nom>");
            return;
        }

        String oldName = args[1];
        String newName = args[2];

        if (ConfigManager.renameConfig(oldName, newName)) {
            sender.sendMessage("&aConfiguration '&6" + oldName + "&a' renommée en '&6" + newName + "&a' avec succès !");
        } else {
            sender.sendMessage("&cErreur lors du renommage de la configuration.");
        }
    }

    private void handleExport(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("&cUsage: /uhcconfig export <nom> <chemin>");
            return;
        }

        String configName = args[1];
        String targetPath = args[2];
        File targetFile = new File(targetPath);

        if (ConfigManager.exportConfig(configName, targetFile)) {
            sender.sendMessage("&aConfiguration '&6" + configName + "&a' exportée vers '&6" + targetPath + "&a' avec succès !");
        } else {
            sender.sendMessage("&cErreur lors de l'export de la configuration.");
        }
    }

    private void handleImport(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("&cUsage: /uhcconfig import <chemin> <nom>");
            return;
        }

        String sourcePath = args[1];
        String configName = args[2];
        File sourceFile = new File(sourcePath);

        if (ConfigManager.importConfig(sourceFile, configName)) {
            sender.sendMessage("&aConfiguration importée depuis '&6" + sourcePath + "&a' vers '&6" + configName + "&a' avec succès !");
        } else {
            sender.sendMessage("&cErreur lors de l'import de la configuration.");
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("&6&l===== Gestion des Configurations UHC =====");
        sender.sendMessage("&e/uhcconfig save <nom> &7- Sauvegarde la configuration actuelle");
        sender.sendMessage("&e/uhcconfig load <nom> &7- Charge une configuration");
        sender.sendMessage("&e/uhcconfig delete <nom> &7- Supprime une configuration");
        sender.sendMessage("&e/uhcconfig list &7- Liste toutes les configurations");
        sender.sendMessage("&e/uhcconfig info <nom> &7- Affiche les infos d'une configuration");
        sender.sendMessage("&e/uhcconfig copy <source> <dest> &7- Copie une configuration");
        sender.sendMessage("&e/uhcconfig rename <ancien> <nouveau> &7- Renomme une configuration");
        sender.sendMessage("&e/uhcconfig export <nom> <chemin> &7- Exporte une configuration");
        sender.sendMessage("&e/uhcconfig import <chemin> <nom> &7- Importe une configuration");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Arrays.asList("save", "load", "delete", "list", "info", "copy", "rename", "export", "import"));
        } else if (args.length == 2) {
            String subCommand = args[0].toLowerCase();
            if (subCommand.equals("load") || subCommand.equals("delete") || subCommand.equals("info") ||
                subCommand.equals("copy") || subCommand.equals("rename") || subCommand.equals("export")) {
                completions.addAll(ConfigManager.getConfigList());
            }
        } else if (args.length == 3) {
            String subCommand = args[0].toLowerCase();
            if (subCommand.equals("copy")) {
                // Pour la copie, suggérer un nom basé sur la source
                completions.add(args[1] + "_copy");
            } else if (subCommand.equals("rename")) {
                // Pour le renommage, suggérer un nouveau nom
                completions.add(args[1] + "_renamed");
            }
        }

        return completions;
    }
}
