package fr.rammex.simpleuhc.config;

import api.rammex.gameapi.option.Option;
import api.rammex.gameapi.option.OptionManager;
import api.rammex.gameapi.option.OptionType;
import api.rammex.gameapi.game.AbstractGame;
import api.rammex.gameapi.module.Module;
import api.rammex.gameapi.module.ModuleManager;
import fr.rammex.simpleuhc.SimpleUHC;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
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

        // Informations générales
        config.set("info.name", configName);
        config.set("info.created", System.currentTimeMillis());
        config.set("info.version", SimpleUHC.instance.getDescription().getVersion());
        config.set("info.description", "Configuration sauvegardée pour SimpleUHC");

        // Sauvegarder les options
        saveOptions(config);

        // Sauvegarder les modules/scénarios activés
        saveModules(config);

        try {
            config.save(configFile);
            SimpleUHC.instance.getLogger().info("Configuration '" + configName + "' sauvegardée avec succès.");
            return true;
        } catch (IOException e) {
            SimpleUHC.instance.getLogger().severe("Erreur lors de la sauvegarde de la configuration '" + configName + "': " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void saveOptions(FileConfiguration config) {
        List<Option> options = getOptions();

        for (Option option : options) {
            String safeName = sanitizeKey(option.getName());
            String path = "options." + safeName;
            Object value = option.getValue();

            // Gérer les différents types d'options
            if (value instanceof Map) {
                // Pour les Maps (comme les enchantements)
                Map<?, ?> map = (Map<?, ?>) value;
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    config.set(path + "." + entry.getKey().toString(), entry.getValue());
                }
            } else {
                config.set(path + ".value", value);
            }

            // Sauvegarder les métadonnées de l'option
            config.set(path + ".type", option.getType().name());
            config.set(path + ".description", option.getDescription());
        }
    }

    private static void saveModules(FileConfiguration config) {
        ModuleManager moduleManager = SimpleUHC.instance.getModuleManager();
        if (moduleManager == null) return;

        List<Module> modules = moduleManager.getRegisteredModules();
        List<String> enabledModules = new ArrayList<>();

        for (Module module : modules) {
            if (module.isEnabled()) {
                enabledModules.add(module.getName());
            }
        }

        config.set("modules.enabled", enabledModules);
    }

    public static boolean loadConfig(String configName) {
        File configFile = new File(configFolder, configName + ".yml");
        if (!configFile.exists()) {
            SimpleUHC.instance.getLogger().warning("Configuration '" + configName + "' non trouvée.");
            return false;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        try {
            // Charger les options
            loadOptions(config);

            // Charger les modules/scénarios
            loadModules(config);

            SimpleUHC.instance.getLogger().info("Configuration '" + configName + "' chargée avec succès.");
            return true;
        } catch (Exception e) {
            SimpleUHC.instance.getLogger().severe("Erreur lors du chargement de la configuration '" + configName + "': " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void loadOptions(FileConfiguration config) {
        List<Option> options = getOptions();

        for (Option option : options) {
            String safeName = sanitizeKey(option.getName());
            String path = "options." + safeName;

            if (!config.contains(path)) continue;

            try {
                OptionType type = option.getType();

                if (type == OptionType.ENCHANT) {
                    // Charger les enchantements (Map)
                    ConfigurationSection section = config.getConfigurationSection(path);
                    if (section != null) {
                        Map<String, Boolean> enchantMap = new HashMap<>();
                        for (String key : section.getKeys(false)) {
                            if (!key.equals("type") && !key.equals("description")) {
                                enchantMap.put(key, section.getBoolean(key));
                            }
                        }
                        if (!enchantMap.isEmpty()) {
                            option.setValue(enchantMap);
                        }
                    }
                } else {
                    // Charger les autres types d'options
                    Object value = config.get(path + ".value");
                    if (value != null) {
                        // Conversion du type si nécessaire
                        switch (type) {
                            case INTEGER:
                                option.setValue(config.getInt(path + ".value"));
                                break;
                            case DOUBLE:
                                option.setValue(config.getDouble(path + ".value"));
                                break;
                            case BOOLEAN:
                                option.setValue(config.getBoolean(path + ".value"));
                                break;
                            case STRING:
                                option.setValue(config.getString(path + ".value"));
                                break;
                            default:
                                option.setValue(value);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                SimpleUHC.instance.getLogger().warning("Erreur lors du chargement de l'option '" + option.getName() + "': " + e.getMessage());
            }
        }
    }

    private static void loadModules(FileConfiguration config) {
        ModuleManager moduleManager = SimpleUHC.instance.getModuleManager();
        if (moduleManager == null) return;

        List<String> enabledModules = config.getStringList("modules.enabled");
        if (enabledModules == null || enabledModules.isEmpty()) return;

        List<Module> modules = moduleManager.getRegisteredModules();

        // Désactiver tous les modules d'abord
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.setEnabled(false);
            }
        }

        // Activer les modules de la configuration
        for (String moduleName : enabledModules) {
            for (Module module : modules) {
                if (module.getName().equals(moduleName)) {
                    module.setEnabled(true);
                    break;
                }
            }
        }
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
        info.put("description", config.getString("info.description", ""));

        // Compter les options et modules
        ConfigurationSection optionsSection = config.getConfigurationSection("options");
        int optionCount = optionsSection != null ? optionsSection.getKeys(false).size() : 0;
        info.put("optionCount", optionCount);

        List<String> enabledModules = config.getStringList("modules.enabled");
        info.put("moduleCount", enabledModules != null ? enabledModules.size() : 0);
        info.put("modules", enabledModules);

        // Taille du fichier
        info.put("fileSize", configFile.length());

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

    /**
     * Copie une configuration existante vers un nouveau nom
     * @param sourceName Nom de la configuration source
     * @param targetName Nom de la nouvelle configuration
     * @return true si la copie a réussi
     */
    public static boolean copyConfig(String sourceName, String targetName) {
        if (!configExists(sourceName)) {
            SimpleUHC.instance.getLogger().warning("Configuration source '" + sourceName + "' non trouvée.");
            return false;
        }

        if (configExists(targetName)) {
            SimpleUHC.instance.getLogger().warning("Configuration cible '" + targetName + "' existe déjà.");
            return false;
        }

        if (!isValidName(targetName)) {
            SimpleUHC.instance.getLogger().warning("Nom de configuration invalide: " + targetName);
            return false;
        }

        File sourceFile = new File(configFolder, sourceName + ".yml");
        File targetFile = new File(configFolder, targetName + ".yml");

        try {
            Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES);

            // Mettre à jour les informations dans le nouveau fichier
            FileConfiguration config = YamlConfiguration.loadConfiguration(targetFile);
            config.set("info.name", targetName);
            config.set("info.created", System.currentTimeMillis());
            config.set("info.description", "Copie de " + sourceName);
            config.save(targetFile);

            SimpleUHC.instance.getLogger().info("Configuration '" + sourceName + "' copiée vers '" + targetName + "'.");
            return true;
        } catch (IOException e) {
            SimpleUHC.instance.getLogger().severe("Erreur lors de la copie de la configuration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Exporte une configuration vers un fichier spécifique
     * @param configName Nom de la configuration
     * @param targetFile Fichier cible
     * @return true si l'export a réussi
     */
    public static boolean exportConfig(String configName, File targetFile) {
        if (!configExists(configName)) {
            SimpleUHC.instance.getLogger().warning("Configuration '" + configName + "' non trouvée.");
            return false;
        }

        File sourceFile = new File(configFolder, configName + ".yml");

        try {
            Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            SimpleUHC.instance.getLogger().info("Configuration '" + configName + "' exportée vers " + targetFile.getAbsolutePath());
            return true;
        } catch (IOException e) {
            SimpleUHC.instance.getLogger().severe("Erreur lors de l'export de la configuration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Importe une configuration depuis un fichier externe
     * @param sourceFile Fichier source
     * @param configName Nom de la configuration à créer
     * @return true si l'import a réussi
     */
    public static boolean importConfig(File sourceFile, String configName) {
        if (!sourceFile.exists()) {
            SimpleUHC.instance.getLogger().warning("Fichier source non trouvé: " + sourceFile.getAbsolutePath());
            return false;
        }

        if (!isValidName(configName)) {
            SimpleUHC.instance.getLogger().warning("Nom de configuration invalide: " + configName);
            return false;
        }

        if (configExists(configName)) {
            SimpleUHC.instance.getLogger().warning("Configuration '" + configName + "' existe déjà.");
            return false;
        }

        File targetFile = new File(configFolder, configName + ".yml");

        try {
            Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES);

            // Valider le fichier YAML
            FileConfiguration config = YamlConfiguration.loadConfiguration(targetFile);
            if (!config.contains("info") || !config.contains("options")) {
                targetFile.delete();
                SimpleUHC.instance.getLogger().warning("Fichier importé invalide (structure incorrecte).");
                return false;
            }

            // Mettre à jour le nom
            config.set("info.name", configName);
            config.save(targetFile);

            SimpleUHC.instance.getLogger().info("Configuration importée depuis " + sourceFile.getAbsolutePath() + " vers '" + configName + "'.");
            return true;
        } catch (IOException e) {
            SimpleUHC.instance.getLogger().severe("Erreur lors de l'import de la configuration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Renomme une configuration existante
     * @param oldName Ancien nom
     * @param newName Nouveau nom
     * @return true si le renommage a réussi
     */
    public static boolean renameConfig(String oldName, String newName) {
        if (!configExists(oldName)) {
            SimpleUHC.instance.getLogger().warning("Configuration '" + oldName + "' non trouvée.");
            return false;
        }

        if (configExists(newName)) {
            SimpleUHC.instance.getLogger().warning("Configuration '" + newName + "' existe déjà.");
            return false;
        }

        if (!isValidName(newName)) {
            SimpleUHC.instance.getLogger().warning("Nom de configuration invalide: " + newName);
            return false;
        }

        File oldFile = new File(configFolder, oldName + ".yml");
        File newFile = new File(configFolder, newName + ".yml");

        try {
            // Mettre à jour le nom dans le fichier
            FileConfiguration config = YamlConfiguration.loadConfiguration(oldFile);
            config.set("info.name", newName);
            config.save(oldFile);

            // Renommer le fichier
            if (oldFile.renameTo(newFile)) {
                SimpleUHC.instance.getLogger().info("Configuration '" + oldName + "' renommée en '" + newName + "'.");
                return true;
            } else {
                SimpleUHC.instance.getLogger().warning("Échec du renommage du fichier.");
                return false;
            }
        } catch (IOException e) {
            SimpleUHC.instance.getLogger().severe("Erreur lors du renommage de la configuration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Nettoie le nom d'une clé pour l'utiliser dans un fichier YAML
     * @param key Clé à nettoyer
     * @return Clé nettoyée
     */
    private static String sanitizeKey(String key) {
        return key.replace(" ", "_")
                  .replace(".", "_")
                  .replace("/", "_")
                  .replace("\\", "_")
                  .toLowerCase();
    }

    /**
     * Obtient la taille formatée d'un fichier de configuration
     * @param configName Nom de la configuration
     * @return Taille formatée (ex: "2.5 KB")
     */
    public static String getConfigFileSize(String configName) {
        File configFile = new File(configFolder, configName + ".yml");
        if (!configFile.exists()) {
            return "N/A";
        }

        long bytes = configFile.length();
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        }
    }

    /**
     * Obtient une date formatée de création
     * @param configName Nom de la configuration
     * @return Date formatée
     */
    public static String getConfigCreationDate(String configName) {
        Map<String, Object> info = getConfigInfo(configName);
        if (info == null) {
            return "N/A";
        }

        long created = (long) info.get("created");
        if (created == 0) {
            return "Inconnue";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(new Date(created));
    }

    /**
     * Vérifie si le dossier de configuration existe et le crée si nécessaire
     * @return true si le dossier existe ou a été créé
     */
    public static boolean ensureConfigFolderExists() {
        if (configFolder == null) {
            init();
        }
        if (!configFolder.exists()) {
            return configFolder.mkdirs();
        }
        return true;
    }

    /**
     * Récupère le dossier de configuration
     * @return Le dossier de configuration
     */
    public static File getConfigFolder() {
        return configFolder;
    }
}
