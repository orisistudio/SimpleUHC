package fr.rammex.simpleuhc.option;

import api.rammex.gameapi.GameAPI;
import api.rammex.gameapi.category.Category;
import api.rammex.gameapi.option.Option;
import api.rammex.gameapi.option.OptionManager;
import api.rammex.gameapi.option.OptionType;
import fr.rammex.simpleuhc.SimpleUHC;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        optionManager.addOption(new Option("Game Team Size", "Taille des équipes si activer", gameCategory, OptionType.INTEGER, 2), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game PvP", "Délais avant la phase pvp en minutes", gameCategory, OptionType.INTEGER, 15), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game Meetup", "Délais en minutes avant le Meetup", gameCategory, OptionType.INTEGER, 60), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game Meetup Speed", "Temps en minutes pour que la border atteigne la taille voulue", gameCategory, OptionType.INTEGER, 10), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game Meetup Radius", "Rayon du Meetup en blocs", gameCategory, OptionType.INTEGER, 100), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game Starter Kit", "Kit de démarrage ( pas dispo encore )", gameCategory, OptionType.BOOLEAN, false), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game Diams Limit", "Limite de diamants minable par joueur", gameCategory, OptionType.INTEGER, 16), SimpleUHC.getSimpleUHCManager());


        // TOUT LES MAT EN FER
        List<Material> ironMaterials = Arrays.asList(
                Material.IRON_HELMET,
                Material.IRON_CHESTPLATE,
                Material.IRON_LEGGINGS,
                Material.IRON_BOOTS
        );

        Map<String, Boolean> ironAllowedEnchants = checkEnchants(ironMaterials);

        // TOUT LES MAT EN DIAMS
        List<Material> diamsMaterials = Arrays.asList(
                Material.DIAMOND_HELMET,
                Material.DIAMOND_CHESTPLATE,
                Material.DIAMOND_LEGGINGS,
                Material.DIAMOND_BOOTS
        );

        Map<String, Boolean> diamsAllowedEnchants = checkEnchants(diamsMaterials);

        // game options - enchants
        optionManager.addOption(new Option("Diamond Alowed Enchants", "Enchants autorisé pour les pièces en diamant", gameCategory, OptionType.ENCHANT, diamsAllowedEnchants), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Iron Alowed Enchants", "Enchants autorisé pour les pièces en fer", gameCategory, OptionType.ENCHANT, ironAllowedEnchants), SimpleUHC.getSimpleUHCManager());

    }

    public static Option getOption(String name) {
        OptionManager optionManager = GameAPI.instance.getOptionManager();
        return optionManager.getOption(SimpleUHC.getSimpleUHCManager(), name);
    }

    private static Map<String, Boolean> checkEnchants(List<Material> materials) {
        Map<String, Boolean> enchants = new HashMap<>();
        for (Material mat : materials) {
            for (Enchantment enchantment : Enchantment.values()) {
                if (enchantment.canEnchantItem(new ItemStack(mat))) {
                    for (int level = 1; level <= enchantment.getMaxLevel(); level++) {
                        String key = enchantment.getName() + "_" + level;
                        if (!enchants.containsKey(key)) {
                            enchants.put(key, true);
                        }
                    }
                }
            }
        }
        return enchants;
    }



}
