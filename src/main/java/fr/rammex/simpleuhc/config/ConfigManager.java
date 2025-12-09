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

public class ConfigManager {

    private static final String CONFIG_FOLDER = "configs";
    private static File configFolder;

    public static void init() {
        configFolder = new File(SimpleUHC.instance.getDataFolder(), CONFIG_FOLDER);
        if (!configFolder.exists()) {
            configFolder.mkdirs();
        }
    }

    public static boolean saveConfig(String configName) {
        if (!isValidName(configName)) {
            return false;
        }

        File configFile = new File(configFolder, configName + ".yml");
        FileConfiguration config = new YamlConfiguration();

        OptionManager optionManager = SimpleUHC.instance.getOptionManager();
        List<Option> options = getOptions();

        config.set("info.name", configName);
        config.set("info.created", System.currentTimeMillis());
        config.set("info.version", SimpleUHC.instance.getDescription().getVersion());

        for (Option option : options) {
            String path = "options." + option.getName();
            Object value = option.getValue();

            if (value instanceof Map) {
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

    public static boolean loadConfig(String configName) {
        File configFile = new File(configFolder, configName + ".yml");
        if (!configFile.exists()) {
            return false;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

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

    public static boolean deleteConfig(String configName) {
        File configFile = new File(configFolder, configName + ".yml");
        return configFile.exists() && configFile.delete();
    }

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

    public static boolean configExists(String configName) {
        File configFile = new File(configFolder, configName + ".yml");
        return configFile.exists();
    }

    private static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.matches("^[a-zA-Z0-9_-]+$");
    }

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
