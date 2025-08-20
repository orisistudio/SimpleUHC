package fr.rammex.simpleuhc;

import api.rammex.gameapi.GameAPI;
import api.rammex.gameapi.option.OptionManager;
import api.rammex.gameapi.scenario.ScenarioManager;
import fr.rammex.simpleuhc.scenario.SimpleUHCManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleUHC extends JavaPlugin {
    public static SimpleUHC instance;
    private ScenarioManager scenarioManager;
    private OptionManager optionManager;
    private SimpleUHCManager simpleUHCManager;

    @Override
    public void onEnable() {
        instance = this;
        this.scenarioManager = GameAPI.instance.getScenarioManager();
        this.optionManager = GameAPI.instance.getOptionManager();
        this.simpleUHCManager = new SimpleUHCManager();

        scenarioManager.addScenario(simpleUHCManager);


    }

    @Override
    public void onDisable() {

    }

    public static SimpleUHCManager getSimpleUHCManager() {
        return instance.simpleUHCManager;
    }




}
