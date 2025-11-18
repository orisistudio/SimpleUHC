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

        // Options de terrain
        optionManager.addOption(new Option("Terrain Min Height", "Hauteur minimale du terrain", worldCategory, OptionType.INTEGER, 60), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Terrain Max Height", "Hauteur maximale du terrain", worldCategory, OptionType.INTEGER, 90), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Terrain Scale", "\u00c9chelle de variation du terrain (0.1=plat, 1.0=montagneux)", worldCategory, OptionType.DOUBLE, 0.5), SimpleUHC.getSimpleUHCManager());

        // Options d'arbres
        optionManager.addOption(new Option("Enable Trees", "Activer la g\u00e9n\u00e9ration d'arbres", worldCategory, OptionType.BOOLEAN, true), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Min Trees Per Chunk", "Nombre minimum d'arbres par chunk", worldCategory, OptionType.INTEGER, 0), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Max Trees Per Chunk", "Nombre maximum d'arbres par chunk", worldCategory, OptionType.INTEGER, 8), SimpleUHC.getSimpleUHCManager());

        // Options de minerais - Charbon
        optionManager.addOption(new Option("Coal Veins Per Chunk", "Nombre de veines de charbon par chunk", worldCategory, OptionType.INTEGER, 20), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Coal Vein Size", "Taille des veines de charbon", worldCategory, OptionType.INTEGER, 16), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Coal Min Height", "Hauteur minimale du charbon", worldCategory, OptionType.INTEGER, 5), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Coal Max Height", "Hauteur maximale du charbon", worldCategory, OptionType.INTEGER, 128), SimpleUHC.getSimpleUHCManager());

        // Options de minerais - Fer
        optionManager.addOption(new Option("Iron Veins Per Chunk", "Nombre de veines de fer par chunk", worldCategory, OptionType.INTEGER, 20), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Iron Vein Size", "Taille des veines de fer", worldCategory, OptionType.INTEGER, 8), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Iron Min Height", "Hauteur minimale du fer", worldCategory, OptionType.INTEGER, 5), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Iron Max Height", "Hauteur maximale du fer", worldCategory, OptionType.INTEGER, 64), SimpleUHC.getSimpleUHCManager());

        // Options de minerais - Or
        optionManager.addOption(new Option("Gold Veins Per Chunk", "Nombre de veines d'or par chunk", worldCategory, OptionType.INTEGER, 2), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Gold Vein Size", "Taille des veines d'or", worldCategory, OptionType.INTEGER, 8), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Gold Min Height", "Hauteur minimale de l'or", worldCategory, OptionType.INTEGER, 5), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Gold Max Height", "Hauteur maximale de l'or", worldCategory, OptionType.INTEGER, 32), SimpleUHC.getSimpleUHCManager());

        // Options de minerais - Diamant
        optionManager.addOption(new Option("Diamond Veins Per Chunk", "Nombre de veines de diamant par chunk", worldCategory, OptionType.INTEGER, 1), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Diamond Vein Size", "Taille des veines de diamant", worldCategory, OptionType.INTEGER, 7), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Diamond Min Height", "Hauteur minimale du diamant", worldCategory, OptionType.INTEGER, 3), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Diamond Max Height", "Hauteur maximale du diamant", worldCategory, OptionType.INTEGER, 16), SimpleUHC.getSimpleUHCManager());

        // Options de minerais - Redstone
        optionManager.addOption(new Option("Redstone Veins Per Chunk", "Nombre de veines de redstone par chunk", worldCategory, OptionType.INTEGER, 8), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Redstone Vein Size", "Taille des veines de redstone", worldCategory, OptionType.INTEGER, 7), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Redstone Min Height", "Hauteur minimale de la redstone", worldCategory, OptionType.INTEGER, 5), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Redstone Max Height", "Hauteur maximale de la redstone", worldCategory, OptionType.INTEGER, 16), SimpleUHC.getSimpleUHCManager());

        // Options de minerais - Lapis
        optionManager.addOption(new Option("Lapis Veins Per Chunk", "Nombre de veines de lapis par chunk", worldCategory, OptionType.INTEGER, 1), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Lapis Vein Size", "Taille des veines de lapis", worldCategory, OptionType.INTEGER, 6), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Lapis Min Height", "Hauteur minimale du lapis", worldCategory, OptionType.INTEGER, 16), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Lapis Max Height", "Hauteur maximale du lapis", worldCategory, OptionType.INTEGER, 32), SimpleUHC.getSimpleUHCManager());

        // Options de minerais - \u00c9meraude
        optionManager.addOption(new Option("Emerald Veins Per Chunk", "Nombre de veines d'\u00e9meraude par chunk", worldCategory, OptionType.INTEGER, 1), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Emerald Vein Size", "Taille des veines d'\u00e9meraude", worldCategory, OptionType.INTEGER, 4), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Emerald Min Height", "Hauteur minimale de l'\u00e9meraude", worldCategory, OptionType.INTEGER, 4), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Emerald Max Height", "Hauteur maximale de l'\u00e9meraude", worldCategory, OptionType.INTEGER, 32), SimpleUHC.getSimpleUHCManager());

        // Options de minerais communs
        optionManager.addOption(new Option("Dirt Veins Per Chunk", "Nombre de poches de terre par chunk", worldCategory, OptionType.INTEGER, 10), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Gravel Veins Per Chunk", "Nombre de poches de gravier par chunk", worldCategory, OptionType.INTEGER, 8), SimpleUHC.getSimpleUHCManager());

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
