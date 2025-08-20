package fr.rammex.simpleuhc.option;

import api.rammex.gameapi.GameAPI;
import api.rammex.gameapi.option.Option;
import api.rammex.gameapi.option.OptionManager;
import api.rammex.gameapi.option.OptionType;
import fr.rammex.simpleuhc.SimpleUHC;
import fr.rammex.simpleuhc.scenario.SimpleUHCManager;

public class OptionSetup {

    public static void setup() {
        OptionManager optionManager = GameAPI.instance.getOptionManager();

        //Player options
        optionManager.addOption(new Option("Player Max", "Nombre maximum de joueurs dans la partie", "player", OptionType.INTEGER, 100), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Player Min", "Nombre minimum de joueurs dans la partie", "player", OptionType.INTEGER, 4), SimpleUHC.getSimpleUHCManager());

        //World options

    }
}
