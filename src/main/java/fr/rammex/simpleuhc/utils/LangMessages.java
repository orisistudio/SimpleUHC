package fr.rammex.simpleuhc.utils;

import fr.rammex.simpleuhc.SimpleUHC;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class LangMessages {
    private static FileConfiguration frConf;
    private static FileConfiguration enConf;
    private File file;

    private void loadFile(String fileName, String folder) {
        if(folder == null){
            file = new File(SimpleUHC.instance.getDataFolder(), fileName + ".yml");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                SimpleUHC.instance.saveResource(fileName + ".yml", false);
            }
        } else {
            file = new File(SimpleUHC.instance.getDataFolder() + "/" + folder, fileName + ".yml");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                SimpleUHC.instance.saveResource(folder+"/"+fileName + ".yml", false);
            }
        }

        FileConfiguration fileConf = new YamlConfiguration();
        try {
            fileConf.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        switch (fileName) {
            case "fr":
                frConf = fileConf;
                break;
            case "en":
                enConf = fileConf;
                break;
        }
    }

    public FileConfiguration getFrenchConf() {
        return frConf;
    }


    public void loadMessages() {
        loadFile("fr", "lang");
        loadFile("en", "lang");
    }

    public static String getMessage(String key, Player player) {
        String lang = SimpleUHC.instance.getConfig().getString("lang");
        switch (lang){
            case "en":
                return getenMessage(key, player);
            case "fr":
                return getfrMessage(key, player);
            default:
                return getfrMessage(key, player);
        }
    }

    public static String getfrMessage(String key, Player player) {
        String message = frConf.getString(key);
        if (message != null) {
            message = message.replace("&", "§");
            message = PlaceholderAPI.setPlaceholders(player, message);
        } else {
            message = "Message not found: " + key;
        }
        return message;
    }

    public static String getenMessage(String key, Player player) {
        String message = enConf.getString(key);
        if (message != null) {
            message = message.replace("&", "§");
            message = PlaceholderAPI.setPlaceholders(player, message);
        } else {
            message = "Message not found: " + key;
        }
        return message;
    }

}
