package fr.rammex.simpleuhc;

import api.rammex.gameapi.GameAPI;
import api.rammex.gameapi.category.CategoryManager;
import api.rammex.gameapi.module.ModuleManager;
import api.rammex.gameapi.option.OptionManager;
import api.rammex.gameapi.scenario.ScenarioManager;
import fr.rammex.simpleuhc.commands.ConfigCommand;
import fr.rammex.simpleuhc.commands.SimpleUHCcommand;
import fr.rammex.simpleuhc.commands.TeamCommand;
import fr.rammex.simpleuhc.commands.TeamChatCommand;
import fr.rammex.simpleuhc.commands.TeamCoordinatesCommand;
import fr.rammex.simpleuhc.commands.TeamInventoryCommand;
import fr.rammex.simpleuhc.config.ConfigManager;
import fr.rammex.simpleuhc.events.EnchantListener;
import fr.rammex.simpleuhc.events.MiningEvent;
import fr.rammex.simpleuhc.events.PlayerListener;
import fr.rammex.simpleuhc.option.CategorySetup;
import fr.rammex.simpleuhc.option.OptionSetup;
import fr.rammex.simpleuhc.game.SimpleUHCManager;
import fr.rammex.simpleuhc.team.TeamChatListener;
import fr.rammex.simpleuhc.team.util.ChangeValueListener;
import fr.rammex.simpleuhc.utils.LangMessages;
import fr.rammex.simpleuhc.utils.TeamPlaceHolder;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleUHC extends JavaPlugin {
    public static SimpleUHC instance;
    private ScenarioManager scenarioManager;
    private OptionManager optionManager;
    private SimpleUHCManager simpleUHCManager;
    private CategoryManager categoryManager;
    private ModuleManager moduleManager;
    private LangMessages langMessages = new LangMessages();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.scenarioManager = GameAPI.instance.getScenarioManager();
        this.optionManager = GameAPI.instance.getOptionManager();
        this.categoryManager = GameAPI.instance.getCategoryManager();
        this.moduleManager = GameAPI.instance.getModuleManager();

        this.simpleUHCManager = new SimpleUHCManager();

        CategorySetup.setup();
        OptionSetup.setup();

        langMessages.loadMessages();

        // Initialiser le gestionnaire de configurations
        ConfigManager.init();
        ConfigManager.createDefaultConfig();

        scenarioManager.addScenario(simpleUHCManager);

        registerEvents();
        registerCommands();

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new TeamPlaceHolder().register();
        } else {
            getLogger().severe("PlaceholderAPI not found. Placeholders will not be available.");
        }
    }

    @Override
    public void onDisable() {

    }

    public static SimpleUHCManager getSimpleUHCManager() {
        return instance.simpleUHCManager;
    }
    public ScenarioManager getScenarioManager() {
        return scenarioManager;
    }
    public OptionManager getOptionManager() {
        return optionManager;
    }
    public CategoryManager getCategoryManager() {
        return categoryManager;
    }
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new EnchantListener(), this);
        getServer().getPluginManager().registerEvents(new MiningEvent(), this);
        getServer().getPluginManager().registerEvents(new ChangeValueListener(), this);
        getServer().getPluginManager().registerEvents(new TeamChatListener(), this);
    }

    private void registerCommands(){
        // Vérifier que les commandes existent avant de les enregistrer
        if (getCommand("team") != null) {
            getCommand("team").setExecutor(new TeamCommand());
        }
        if (getCommand("simpleuhc") != null) {
            getCommand("simpleuhc").setExecutor(new SimpleUHCcommand());
        }
        if (getCommand("teaminventory") != null) {
            getCommand("teaminventory").setExecutor(new TeamInventoryCommand());
        }
        if (getCommand("tc") != null) {
            getCommand("tc").setExecutor(new TeamChatCommand());
        }
        if (getCommand("tcc") != null) {
            getCommand("tcc").setExecutor(new TeamCoordinatesCommand());
        }

        if (getCommand("uhcconfig") != null) {
            ConfigCommand configCommand = new ConfigCommand();
            getCommand("uhcconfig").setExecutor(configCommand);
            getCommand("uhcconfig").setTabCompleter(configCommand);
        }
    }

    public LangMessages getLangMessages() {
        return langMessages;
    }

}
