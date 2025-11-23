package fr.rammex.simpleuhc.world;

import fr.rammex.simpleuhc.option.OptionSetup;

/**
 * Classe de configuration pour la g\u00e9n\u00e9ration personnalis\u00e9e du monde
 * Centralise tous les param\u00e8tres configurables
 */
public class WorldGenerationConfig {

    // ========== TERRAIN ==========
    public static int getTerrainMinHeight() {
        return (int) OptionSetup.getOption("Terrain Min Height").getValue();
    }

    public static int getTerrainMaxHeight() {
        return (int) OptionSetup.getOption("Terrain Max Height").getValue();
    }

    public static double getTerrainScale() {
        return (double) OptionSetup.getOption("Terrain Scale").getValue();
    }

    public static String getWorldBiome() {
        return (String) OptionSetup.getOption("World Biome").getValue();
    }

    // ========== ARBRES ==========
    public static boolean isTreeGenerationEnabled() {
        return (boolean) OptionSetup.getOption("Enable Trees").getValue();
    }

    public static int getMinTreesPerChunk() {
        return (int) OptionSetup.getOption("Min Trees Per Chunk").getValue();
    }

    public static int getMaxTreesPerChunk() {
        return (int) OptionSetup.getOption("Max Trees Per Chunk").getValue();
    }

    // ========== MINERAIS - CHARBON ==========
    public static int getCoalVeinsPerChunk() {
        return (int) OptionSetup.getOption("Coal Veins Per Chunk").getValue();
    }

    public static int getCoalVeinSize() {
        return (int) OptionSetup.getOption("Coal Vein Size").getValue();
    }

    public static int getCoalMinHeight() {
        return (int) OptionSetup.getOption("Coal Min Height").getValue();
    }

    public static int getCoalMaxHeight() {
        return (int) OptionSetup.getOption("Coal Max Height").getValue();
    }

    // ========== MINERAIS - FER ==========
    public static int getIronVeinsPerChunk() {
        return (int) OptionSetup.getOption("Iron Veins Per Chunk").getValue();
    }

    public static int getIronVeinSize() {
        return (int) OptionSetup.getOption("Iron Vein Size").getValue();
    }

    public static int getIronMinHeight() {
        return (int) OptionSetup.getOption("Iron Min Height").getValue();
    }

    public static int getIronMaxHeight() {
        return (int) OptionSetup.getOption("Iron Max Height").getValue();
    }

    // ========== MINERAIS - OR ==========
    public static int getGoldVeinsPerChunk() {
        return (int) OptionSetup.getOption("Gold Veins Per Chunk").getValue();
    }

    public static int getGoldVeinSize() {
        return (int) OptionSetup.getOption("Gold Vein Size").getValue();
    }

    public static int getGoldMinHeight() {
        return (int) OptionSetup.getOption("Gold Min Height").getValue();
    }

    public static int getGoldMaxHeight() {
        return (int) OptionSetup.getOption("Gold Max Height").getValue();
    }

    // ========== MINERAIS - DIAMANT ==========
    public static int getDiamondVeinsPerChunk() {
        return (int) OptionSetup.getOption("Diamond Veins Per Chunk").getValue();
    }

    public static int getDiamondVeinSize() {
        return (int) OptionSetup.getOption("Diamond Vein Size").getValue();
    }

    public static int getDiamondMinHeight() {
        return (int) OptionSetup.getOption("Diamond Min Height").getValue();
    }

    public static int getDiamondMaxHeight() {
        return (int) OptionSetup.getOption("Diamond Max Height").getValue();
    }

    // ========== MINERAIS - REDSTONE ==========
    public static int getRedstoneVeinsPerChunk() {
        return (int) OptionSetup.getOption("Redstone Veins Per Chunk").getValue();
    }

    public static int getRedstoneVeinSize() {
        return (int) OptionSetup.getOption("Redstone Vein Size").getValue();
    }

    public static int getRedstoneMinHeight() {
        return (int) OptionSetup.getOption("Redstone Min Height").getValue();
    }

    public static int getRedstoneMaxHeight() {
        return (int) OptionSetup.getOption("Redstone Max Height").getValue();
    }

    // ========== MINERAIS - LAPIS ==========
    public static int getLapisVeinsPerChunk() {
        return (int) OptionSetup.getOption("Lapis Veins Per Chunk").getValue();
    }

    public static int getLapisVeinSize() {
        return (int) OptionSetup.getOption("Lapis Vein Size").getValue();
    }

    public static int getLapisMinHeight() {
        return (int) OptionSetup.getOption("Lapis Min Height").getValue();
    }

    public static int getLapisMaxHeight() {
        return (int) OptionSetup.getOption("Lapis Max Height").getValue();
    }

    // ========== MINERAIS - EMERAUDE ==========
    public static int getEmeraldVeinsPerChunk() {
        return (int) OptionSetup.getOption("Emerald Veins Per Chunk").getValue();
    }

    public static int getEmeraldVeinSize() {
        return (int) OptionSetup.getOption("Emerald Vein Size").getValue();
    }

    public static int getEmeraldMinHeight() {
        return (int) OptionSetup.getOption("Emerald Min Height").getValue();
    }

    public static int getEmeraldMaxHeight() {
        return (int) OptionSetup.getOption("Emerald Max Height").getValue();
    }

    // ========== MINERAIS COMMUNS ==========
    public static int getDirtVeinsPerChunk() {
        return (int) OptionSetup.getOption("Dirt Veins Per Chunk").getValue();
    }

    public static int getGravelVeinsPerChunk() {
        return (int) OptionSetup.getOption("Gravel Veins Per Chunk").getValue();
    }

    // ========== CAVES ==========
    public static boolean isCaveGenerationEnabled() {
        return (boolean) OptionSetup.getOption("Enable Caves").getValue();
    }

    public static int getCaveMinHeight() {
        return (int) OptionSetup.getOption("Cave Min Height").getValue();
    }

    public static int getCaveMaxHeight() {
        return (int) OptionSetup.getOption("Cave Max Height").getValue();
    }

    public static double getCaveScale() {
        return (double) OptionSetup.getOption("Cave Scale").getValue();
    }

    public static double getCaveThreshold() {
        return (double) OptionSetup.getOption("Cave Threshold").getValue();
    }
}
