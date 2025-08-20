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
        optionManager.addOption(new Option("World Name", "Nom du monde de la partie", "world", OptionType.STRING, "world"), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("World Seed", "Graine du monde de la partie", "world", OptionType.STRING, ""), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("World Size", "Taille du monde de la partie", "world", OptionType.INTEGER, 1000), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("World Border", "Taille de la bordure du monde", "world", OptionType.INTEGER, 1000), SimpleUHC.getSimpleUHCManager());

        //Game options
        optionManager.addOption(new Option("Game Time", "Durée de la partie en minutes", "game", OptionType.INTEGER, 60), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game Start Delay", "Délai de démarrage de la partie en secondes", "game", OptionType.INTEGER, 30), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game Team", "Activer ou non les équipes", "game", OptionType.BOOLEAN, false), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game PvP", "Délais avant la phase pvp en minute", "game", OptionType.INTEGER, 15), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game Deathmatch", "Activer ou non le deathmatch", "game", OptionType.BOOLEAN, true), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game Deathmatch Time", "Durée avant du deathmatch en minutes", "game", OptionType.INTEGER, 55), SimpleUHC.getSimpleUHCManager());
    }
}
