package fr.rammex.simpleuhc.option;

import api.rammex.gameapi.GameAPI;
import api.rammex.gameapi.category.Category;
import api.rammex.gameapi.category.CategoryManager;
import api.rammex.gameapi.option.Option;
import api.rammex.gameapi.option.OptionManager;
import api.rammex.gameapi.option.OptionType;
import fr.rammex.simpleuhc.SimpleUHC;
import fr.rammex.simpleuhc.scenario.SimpleUHCManager;

public class OptionSetup {

    public static void setup() {
        OptionManager optionManager = SimpleUHC.instance.getOptionManager();

        Category playerCategory = CategorySetup.getCategory("player");
        Category worldCategory = CategorySetup.getCategory("world");
        Category gameCategory = CategorySetup.getCategory("game");

        //Player options
        optionManager.addOption(new Option("Player Max", "Nombre maximum de joueurs dans la partie", playerCategory, OptionType.INTEGER, 100), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Player Min", "Nombre minimum de joueurs dans la partie", playerCategory, OptionType.INTEGER, 4), SimpleUHC.getSimpleUHCManager());

        //World options
        optionManager.addOption(new Option("World Border", "Taille de la bordure du monde", worldCategory, OptionType.INTEGER, 1000), SimpleUHC.getSimpleUHCManager());

        //Game options
        optionManager.addOption(new Option("Game Team", "Activer ou non les équipes", gameCategory, OptionType.BOOLEAN, false), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game PvP", "Délais avant la phase pvp en minute", gameCategory, OptionType.INTEGER, 15), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game Meetup", "Délais en minute avant le Meetup", gameCategory, OptionType.INTEGER, 60), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game Meetup Radius", "Rayon du Meetup en blocs", gameCategory, OptionType.INTEGER, 100), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game Starter Kit", "Kit de démarrage", gameCategory, OptionType.BOOLEAN, false), SimpleUHC.getSimpleUHCManager());

    }

    public static Option getOption(String name) {
        OptionManager optionManager = GameAPI.instance.getOptionManager();
        return optionManager.getOption(SimpleUHC.getSimpleUHCManager(), name);
    }
}
