package fr.rammex.simpleuhc.world;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class CustomTreePopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        if (!WorldGenerationConfig.isTreeGenerationEnabled()) {
            return;
        }

        int treeCount = getTreeCountForChunk(random);

        for (int i = 0; i < treeCount; i++) {
            int x = random.nextInt(16);
            int z = random.nextInt(16);

            int y = getHighestBlockY(chunk, x, z);

            if (y <= 0) continue;

            Block block = chunk.getBlock(x, y, z);
            Block blockBelow = chunk.getBlock(x, y - 1, z);

            if (blockBelow.getType() == Material.GRASS || blockBelow.getType() == Material.DIRT) {
                Biome biome = block.getBiome();
                TreeType treeType = getTreeTypeForBiome(biome, random);

                if (treeType != null && block.getType() == Material.AIR) {
                    world.generateTree(block.getLocation(), treeType);
                }
            }
        }
    }

    private int getHighestBlockY(Chunk chunk, int x, int z) {
        for (int y = 127; y >= 0; y--) {
            Block block = chunk.getBlock(x, y, z);
            if (block.getType() != Material.AIR) {
                return y + 1;
            }
        }
        return 0;
    }

    private int getTreeCountForChunk(Random random) {
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
                return null;

            default:
                return TreeType.TREE;
        }
    }
}
