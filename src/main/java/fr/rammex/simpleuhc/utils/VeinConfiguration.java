package fr.rammex.simpleuhc.utils;

/**
 * Configuration pour la génération de veines de minerai
 * Adapté depuis UhcCore: https://github.com/Mezy/UhcCore
 */
public class VeinConfiguration {
    private final int minVeinsPerChunk;
    private final int maxVeinsPerChunk;
    private final int minBlocksPerVein;
    private final int maxBlocksPerVein;
    private final int minY;
    private final int maxY;

    public VeinConfiguration(int minVeinsPerChunk, int maxVeinsPerChunk,
                           int minBlocksPerVein, int maxBlocksPerVein,
                           int minY, int maxY) {
        this.minVeinsPerChunk = minVeinsPerChunk;
        this.maxVeinsPerChunk = maxVeinsPerChunk;
        this.minBlocksPerVein = minBlocksPerVein;
        this.maxBlocksPerVein = maxBlocksPerVein;
        this.minY = minY;
        this.maxY = maxY;
    }

    public int getMinVeinsPerChunk() {
        return minVeinsPerChunk;
    }

    public int getMaxVeinsPerChunk() {
        return maxVeinsPerChunk;
    }

    public int getMinBlocksPerVein() {
        return minBlocksPerVein;
    }

    public int getMaxBlocksPerVein() {
        return maxBlocksPerVein;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }
}
