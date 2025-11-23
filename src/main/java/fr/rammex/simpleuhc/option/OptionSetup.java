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
        optionManager.addOption(new Option("Player Max", "Maximum number of players in the game", playerCategory, OptionType.INTEGER, 100), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Player Min", "Minimum number of players in the game", playerCategory, OptionType.INTEGER, 4), SimpleUHC.getSimpleUHCManager());

        //World options
        optionManager.addOption(new Option("World Border", "World border size", worldCategory, OptionType.INTEGER, 1000), SimpleUHC.getSimpleUHCManager());

        // Terrain options
        optionManager.addOption(new Option("Terrain Min Height", "Minimum terrain height", worldCategory, OptionType.INTEGER, 60), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Terrain Max Height", "Maximum terrain height", worldCategory, OptionType.INTEGER, 90), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Terrain Scale", "Terrain variation scale (0.1=flat, 1.0=mountainous)", worldCategory, OptionType.DOUBLE, 0.5), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("World Biome", "Biome for spawn area 200x200 (AUTO for automatic generation) | Available biomes: PLAINS, FOREST, BIRCH_FOREST, ROOFED_FOREST, TAIGA, MEGA_TAIGA, SAVANNA, DESERT, MESA, JUNGLE, SWAMPLAND, ICE_PLAINS, EXTREME_HILLS", worldCategory, OptionType.STRING, "AUTO"), SimpleUHC.getSimpleUHCManager());

        // Tree options
        optionManager.addOption(new Option("Enable Trees", "Enable tree generation", worldCategory, OptionType.BOOLEAN, true), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Min Trees Per Chunk", "Minimum number of trees per chunk", worldCategory, OptionType.INTEGER, 0), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Max Trees Per Chunk", "Maximum number of trees per chunk", worldCategory, OptionType.INTEGER, 8), SimpleUHC.getSimpleUHCManager());

        // Ore options - Coal
        optionManager.addOption(new Option("Coal Veins Per Chunk", "Number of coal veins per chunk", worldCategory, OptionType.INTEGER, 20), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Coal Vein Size", "Coal vein size", worldCategory, OptionType.INTEGER, 16), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Coal Min Height", "Minimum coal height", worldCategory, OptionType.INTEGER, 5), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Coal Max Height", "Maximum coal height", worldCategory, OptionType.INTEGER, 128), SimpleUHC.getSimpleUHCManager());

        // Ore options - Iron
        optionManager.addOption(new Option("Iron Veins Per Chunk", "Number of iron veins per chunk", worldCategory, OptionType.INTEGER, 20), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Iron Vein Size", "Iron vein size", worldCategory, OptionType.INTEGER, 8), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Iron Min Height", "Minimum iron height", worldCategory, OptionType.INTEGER, 5), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Iron Max Height", "Maximum iron height", worldCategory, OptionType.INTEGER, 64), SimpleUHC.getSimpleUHCManager());

        // Ore options - Gold
        optionManager.addOption(new Option("Gold Veins Per Chunk", "Number of gold veins per chunk", worldCategory, OptionType.INTEGER, 2), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Gold Vein Size", "Gold vein size", worldCategory, OptionType.INTEGER, 8), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Gold Min Height", "Minimum gold height", worldCategory, OptionType.INTEGER, 5), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Gold Max Height", "Maximum gold height", worldCategory, OptionType.INTEGER, 32), SimpleUHC.getSimpleUHCManager());

        // Ore options - Diamond
        optionManager.addOption(new Option("Diamond Veins Per Chunk", "Number of diamond veins per chunk", worldCategory, OptionType.INTEGER, 1), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Diamond Vein Size", "Diamond vein size", worldCategory, OptionType.INTEGER, 7), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Diamond Min Height", "Minimum diamond height", worldCategory, OptionType.INTEGER, 3), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Diamond Max Height", "Maximum diamond height", worldCategory, OptionType.INTEGER, 16), SimpleUHC.getSimpleUHCManager());

        // Ore options - Redstone
        optionManager.addOption(new Option("Redstone Veins Per Chunk", "Number of redstone veins per chunk", worldCategory, OptionType.INTEGER, 8), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Redstone Vein Size", "Redstone vein size", worldCategory, OptionType.INTEGER, 7), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Redstone Min Height", "Minimum redstone height", worldCategory, OptionType.INTEGER, 5), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Redstone Max Height", "Maximum redstone height", worldCategory, OptionType.INTEGER, 16), SimpleUHC.getSimpleUHCManager());

        // Ore options - Lapis
        optionManager.addOption(new Option("Lapis Veins Per Chunk", "Number of lapis veins per chunk", worldCategory, OptionType.INTEGER, 1), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Lapis Vein Size", "Lapis vein size", worldCategory, OptionType.INTEGER, 6), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Lapis Min Height", "Minimum lapis height", worldCategory, OptionType.INTEGER, 16), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Lapis Max Height", "Maximum lapis height", worldCategory, OptionType.INTEGER, 32), SimpleUHC.getSimpleUHCManager());

        // Ore options - Emerald
        optionManager.addOption(new Option("Emerald Veins Per Chunk", "Number of emerald veins per chunk", worldCategory, OptionType.INTEGER, 1), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Emerald Vein Size", "Emerald vein size", worldCategory, OptionType.INTEGER, 4), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Emerald Min Height", "Minimum emerald height", worldCategory, OptionType.INTEGER, 4), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Emerald Max Height", "Maximum emerald height", worldCategory, OptionType.INTEGER, 32), SimpleUHC.getSimpleUHCManager());

        // Common ore options
        optionManager.addOption(new Option("Dirt Veins Per Chunk", "Number of dirt pockets per chunk", worldCategory, OptionType.INTEGER, 10), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Gravel Veins Per Chunk", "Number of gravel pockets per chunk", worldCategory, OptionType.INTEGER, 8), SimpleUHC.getSimpleUHCManager());

        // Cave options
        optionManager.addOption(new Option("Enable Caves", "Enable cave generation", worldCategory, OptionType.BOOLEAN, true), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Cave Min Height", "Minimum height for cave generation", worldCategory, OptionType.INTEGER, 5), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Cave Max Height", "Maximum height for cave generation", worldCategory, OptionType.INTEGER, 60), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Cave Scale", "Cave size scale (0.5=small, 1.0=normal, 2.0=large)", worldCategory, OptionType.DOUBLE, 1.0), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Cave Threshold", "Cave density threshold (0.0=more caves, 0.5=normal, 1.0=fewer caves)", worldCategory, OptionType.DOUBLE, 0.3), SimpleUHC.getSimpleUHCManager());

        //Game options
        optionManager.addOption(new Option("Game Team", "Enable or disable teams", gameCategory, OptionType.BOOLEAN, false), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game Team Size", "Team size if enabled", gameCategory, OptionType.INTEGER, 2), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game PvP", "Delay before PvP phase in minutes", gameCategory, OptionType.INTEGER, 15), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game Meetup", "Delay in minutes before Meetup", gameCategory, OptionType.INTEGER, 60), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game Meetup Speed", "Time in minutes for the border to reach desired size", gameCategory, OptionType.INTEGER, 10), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game Meetup Radius", "Meetup radius in blocks", gameCategory, OptionType.INTEGER, 100), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game Starter Kit", "Starter kit (not available yet)", gameCategory, OptionType.BOOLEAN, false), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Game Diams Limit", "Diamond mining limit per player", gameCategory, OptionType.INTEGER, 16), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Apple Drop Rate", "Apple drop rate multiplier (1.0 = normal, 2.0 = double, etc.)", gameCategory, OptionType.DOUBLE, 1.0), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Flint Drop Rate", "Flint drop rate multiplier (1.0 = normal, 2.0 = double, etc.)", gameCategory, OptionType.DOUBLE, 1.0), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("XP Multiplier", "XP drop multiplier (1.0 = normal, 2.0 = double, etc.)", gameCategory, OptionType.DOUBLE, 1.0), SimpleUHC.getSimpleUHCManager());


        // ARMURES EN FER
        List<Material> ironMaterials = Arrays.asList(
                Material.IRON_HELMET,
                Material.IRON_CHESTPLATE,
                Material.IRON_LEGGINGS,
                Material.IRON_BOOTS
        );

        Map<String, Boolean> ironAllowedEnchants = checkEnchants(ironMaterials);

        // ARMURES EN DIAMANT
        List<Material> diamsMaterials = Arrays.asList(
                Material.DIAMOND_HELMET,
                Material.DIAMOND_CHESTPLATE,
                Material.DIAMOND_LEGGINGS,
                Material.DIAMOND_BOOTS
        );

        Map<String, Boolean> diamsAllowedEnchants = checkEnchants(diamsMaterials);

        // ARMES
        List<Material> weaponsMaterials = Arrays.asList(
                Material.WOOD_SWORD,
                Material.STONE_SWORD,
                Material.IRON_SWORD,
                Material.GOLD_SWORD,
                Material.DIAMOND_SWORD,
                Material.BOW
        );

        Map<String, Boolean> weaponsAllowedEnchants = checkEnchants(weaponsMaterials);

        // OUTILS
        List<Material> toolsMaterials = Arrays.asList(
                Material.WOOD_PICKAXE,
                Material.WOOD_AXE,
                Material.WOOD_SPADE,
                Material.WOOD_HOE,
                Material.STONE_PICKAXE,
                Material.STONE_AXE,
                Material.STONE_SPADE,
                Material.STONE_HOE,
                Material.IRON_PICKAXE,
                Material.IRON_AXE,
                Material.IRON_SPADE,
                Material.IRON_HOE,
                Material.GOLD_PICKAXE,
                Material.GOLD_AXE,
                Material.GOLD_SPADE,
                Material.GOLD_HOE,
                Material.DIAMOND_PICKAXE,
                Material.DIAMOND_AXE,
                Material.DIAMOND_SPADE,
                Material.DIAMOND_HOE,
                Material.SHEARS,
                Material.FLINT_AND_STEEL,
                Material.FISHING_ROD
        );

        Map<String, Boolean> toolsAllowedEnchants = checkEnchants(toolsMaterials);

        // game options - enchants
        optionManager.addOption(new Option("Diamond Alowed Enchants", "Allowed enchants for diamond armor", gameCategory, OptionType.ENCHANT, diamsAllowedEnchants), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Iron Alowed Enchants", "Allowed enchants for iron armor", gameCategory, OptionType.ENCHANT, ironAllowedEnchants), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Weapons Alowed Enchants", "Allowed enchants for weapons", gameCategory, OptionType.ENCHANT, weaponsAllowedEnchants), SimpleUHC.getSimpleUHCManager());
        optionManager.addOption(new Option("Tools Alowed Enchants", "Allowed enchants for tools", gameCategory, OptionType.ENCHANT, toolsAllowedEnchants), SimpleUHC.getSimpleUHCManager());

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
