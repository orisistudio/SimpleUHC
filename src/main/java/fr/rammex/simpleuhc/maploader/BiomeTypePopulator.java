package fr.rammex.simpleuhc.maploader;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

/**
 * Populator qui remplace les biomes océans par des forêts
 * Adapté depuis UhcCore: https://github.com/Mezy/UhcCore
 */
public class BiomeTypePopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        for (int x = 1; x < 15; x++) {
            for (int z = 1; z < 15; z++) {
                Block block = chunk.getBlock(x, 1, z);
                Biome replacementBiome = getReplacementBiome(block.getBiome());

                if (replacementBiome != null) {
                    block.setBiome(replacementBiome);
                }
            }
        }
    }

    private Biome getReplacementBiome(Biome biome) {
        // Remplacer tous les biomes océans par des forêts
        if (biome.toString().contains("OCEAN")) {
            return Biome.FOREST;
        }
        return null;
    }
}
