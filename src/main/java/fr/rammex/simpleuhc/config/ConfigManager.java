package fr.rammex.simpleuhc.config;

import api.rammex.gameapi.option.Option;
import api.rammex.gameapi.option.OptionManager;
import api.rammex.gameapi.game.AbstractGame;
import fr.rammex.simpleuhc.SimpleUHC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * G\u00e8re la cr\u00e9ation, sauvegarde et chargement des configurations de jeu
 */
public class ConfigManager {

    private static final String CONFIG_FOLDER = "configs";
    private static File configFolder;

    /**
     * Initialise le dossier de configurations
     */
    public static void init() {
        configFolder = new File(SimpleUHC.instance.getDataFolder(), CONFIG_FOLDER);
        if (!configFolder.exists()) {
            configFolder.mkdirs();
        }
    }

    /**
     * Sauvegarde la configuration actuelle sous un nom
     * @param configName Nom de la configuration
     * @return true si la sauvegarde a r\u00e9ussi
     */
    public static boolean saveConfig(String configName) {
        if (!isValidName(configName)) {
            return false;
        }

        File configFile = new File(configFolder, configName + ".yml");
        FileConfiguration config = new YamlConfiguration();

        // Sauvegarder toutes les options
        OptionManager optionManager = SimpleUHC.instance.getOptionManager();
        List<Option> options = getOptions();

        // Informations g\u00e9n\u00e9rales
        config.set("info.name", configName);
        config.set("info.created", System.currentTimeMillis());
        config.set("info.version", SimpleUHC.instance.getDescription().getVersion());

        // Sauvegarder chaque option
        for (Option option : options) {
            String path = "options." + option.getName();
            Object value = option.getValue();

            // G\u00e9rer les diff\u00e9rents types
            if (value instanceof Map) {
                // Pour les enchantements par exemple
                config.set(path, value);
            } else {
                config.set(path, value);
            }
        }

        try {
            config.save(configFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Charge une configuration
     * @param configName Nom de la configuration \u00e0 charger
     * @return true si le chargement a r\u00e9ussi
     */
    public static boolean loadConfig(String configName) {
        File configFile = new File(configFolder, configName + ".yml");
        if (!configFile.exists()) {
            return false;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        // Charger chaque option
        OptionManager optionManager = SimpleUHC.instance.getOptionManager();
        List<Option> options = getOptions();

        for (Option option : options) {
            String path = "options." + option.getName();
            if (config.contains(path)) {
                Object value = config.get(path);
                option.setValue(value);
            }
        }

        return true;
    }

    /**
     * Supprime une configuration
     * @param configName Nom de la configuration \u00e0 supprimer
     * @return true si la suppression a r\u00e9ussi
     */
    public static boolean deleteConfig(String configName) {
        File configFile = new File(configFolder, configName + ".yml");
        return configFile.exists() && configFile.delete();
    }

    /**
     * R\u00e9cup\u00e8re la liste de toutes les configurations
     * @return Liste des noms de configurations
     */
    public static List<String> getConfigList() {
        List<String> configs = new ArrayList<>();
        File[] files = configFolder.listFiles((dir, name) -> name.endsWith(".yml"));

        if (files != null) {
            for (File file : files) {
                String name = file.getName().replace(".yml", "");
                configs.add(name);
            }
        }

        return configs;
    }

    /**
     * R\u00e9cup\u00e8re les informations d'une configuration
     * @param configName Nom de la configuration
     * @return Map contenant les informations (name, created, version)
     */
    public static Map<String, Object> getConfigInfo(String configName) {
        File configFile = new File(configFolder, configName + ".yml");
        if (!configFile.exists()) {
            return null;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        Map<String, Object> info = new HashMap<>();

        info.put("name", config.getString("info.name", configName));
        info.put("created", config.getLong("info.created", 0));
        info.put("version", config.getString("info.version", "Unknown"));

        return info;
    }

    /**
     * V\u00e9rifie si un nom de configuration existe
     * @param configName Nom \u00e0 v\u00e9rifier
     * @return true si la configuration existe
     */
    public static boolean configExists(String configName) {
        File configFile = new File(configFolder, configName + ".yml");
        return configFile.exists();
    }

    /**
     * V\u00e9rifie si un nom de configuration est valide
     * @param name Nom \u00e0 v\u00e9rifier
     * @return true si le nom est valide
     */
    private static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.matches("^[a-zA-Z0-9_-]+$");
    }

    /**
     * Cr\u00e9e une configuration par d\u00e9faut si elle n'existe pas
     */
    public static void createDefaultConfig() {
        if (!configExists("default")) {
            saveConfig("default");
        }
    }

    private static List<Option> getOptions() {
        List<Option> options = new ArrayList<>();
        Map<Option, AbstractGame> optionMap = SimpleUHC.instance.getOptionManager().getOptions();
        for (Option option : optionMap.keySet()) {
            options.add(option);
        }
        return options;
    }
}
