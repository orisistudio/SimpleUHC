package fr.rammex.simpleuhc;

import api.rammex.gameapi.GameAPI;
import api.rammex.gameapi.category.CategoryManager;
import api.rammex.gameapi.option.OptionManager;
import api.rammex.gameapi.scenario.ScenarioManager;
import fr.rammex.simpleuhc.events.PlayerListener;
import fr.rammex.simpleuhc.option.CategorySetup;
import fr.rammex.simpleuhc.option.OptionSetup;
import fr.rammex.simpleuhc.game.SimpleUHCManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleUHC extends JavaPlugin {
    public static SimpleUHC instance;
    private ScenarioManager scenarioManager;
    private OptionManager optionManager;
    private SimpleUHCManager simpleUHCManager;
    private CategoryManager categoryManager;

    @Override
    public void onEnable() {
        instance = this;
        this.scenarioManager = GameAPI.instance.getScenarioManager();
        this.optionManager = GameAPI.instance.getOptionManager();
        this.categoryManager = GameAPI.instance.getCategoryManager();
        this.simpleUHCManager = new SimpleUHCManager();

        scenarioManager.addScenario(simpleUHCManager);
        CategorySetup.setup();
        OptionSetup.setup();
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

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }





}
