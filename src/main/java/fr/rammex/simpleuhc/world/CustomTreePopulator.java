package fr.rammex.simpleuhc.world;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

/**
 * Populator personnalis\u00e9 pour g\u00e9n\u00e9rer des arbres en fonction des biomes
 * IMPORTANT: Ne g\u00e9n\u00e8re que dans le chunk actuel pour \u00e9viter les StackOverflow
 */
public class CustomTreePopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        // V\u00e9rifier si la g\u00e9n\u00e9ration d'arbres est activ\u00e9e
        if (!WorldGenerationConfig.isTreeGenerationEnabled()) {
            return;
        }

        // G\u00e9n\u00e9rer plusieurs arbres par chunk
        int treeCount = getTreeCountForChunk(random);

        for (int i = 0; i < treeCount; i++) {
            // Utiliser des coordonn\u00e9es relatives au chunk (0-15)
            int x = random.nextInt(16);
            int z = random.nextInt(16);

            // Trouver la hauteur la plus haute dans le chunk
            int y = getHighestBlockY(chunk, x, z);

            if (y <= 0) continue;

            // Utiliser chunk.getBlock au lieu de world.getBlockAt
            Block block = chunk.getBlock(x, y, z);
            Block blockBelow = chunk.getBlock(x, y - 1, z);

            // V\u00e9rifier que le bloc en dessous est de l'herbe ou de la terre
            if (blockBelow.getType() == Material.GRASS || blockBelow.getType() == Material.DIRT) {
                Biome biome = block.getBiome();
                TreeType treeType = getTreeTypeForBiome(biome, random);

                if (treeType != null && block.getType() == Material.AIR) {
                    // G\u00e9n\u00e9rer l'arbre - world.generateTree est s\u00e9curis\u00e9 car il est appel\u00e9 apr\u00e8s la g\u00e9n\u00e9ration du chunk
                    world.generateTree(block.getLocation(), treeType);
                }
            }
        }
    }

    private int getHighestBlockY(Chunk chunk, int x, int z) {
        // Chercher le bloc le plus haut dans le chunk sans charger d'autres chunks
        for (int y = 127; y >= 0; y--) {
            Block block = chunk.getBlock(x, y, z);
            if (block.getType() != Material.AIR) {
                return y + 1; // Retourner la position au-dessus du bloc solide
            }
        }
        return 0;
    }

    private int getTreeCountForChunk(Random random) {
        // Nombre d'arbres varie entre min et max configurables
        int min = WorldGenerationConfig.getMinTreesPerChunk();
        int max = WorldGenerationConfig.getMaxTreesPerChunk();

        if (max <= min) {
            return min;
        }

        return min + random.nextInt(max - min + 1);
    }

    private TreeType getTreeTypeForBiome(Biome biome, Random random) {
        switch (biome) {
            case FOREST:
            case BIRCH_FOREST:
            case BIRCH_FOREST_HILLS:
                return random.nextBoolean() ? TreeType.TREE : TreeType.BIRCH;

            case ROOFED_FOREST:
                return TreeType.DARK_OAK;

            case TAIGA:
            case TAIGA_HILLS:
            case MEGA_TAIGA:
            case MEGA_TAIGA_HILLS:
                return random.nextInt(3) == 0 ? TreeType.REDWOOD : TreeType.TALL_REDWOOD;

            case JUNGLE:
            case JUNGLE_HILLS:
                return random.nextInt(10) == 0 ? TreeType.JUNGLE : TreeType.SMALL_JUNGLE;

            case SAVANNA:
                return TreeType.ACACIA;

            case PLAINS:
                // Quelques arbres dans les plaines
                return random.nextInt(20) == 0 ? TreeType.TREE : null;

            case SWAMPLAND:
                return TreeType.SWAMP;

            case DESERT:
            case DESERT_HILLS:
            case MESA:
            case ICE_PLAINS:
            case ICE_MOUNTAINS:
            case EXTREME_HILLS:
            case EXTREME_HILLS_PLUS:
                // Pas d'arbres dans ces biomes
                return null;

            default:
                return TreeType.TREE;
        }
    }
}
