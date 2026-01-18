package fr.rammex.simpleuhc.maploader;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Populator qui retire les minerais qui ne sont pas connectés à l'air (caves)
 * Adapté depuis UhcCore: https://github.com/Mezy/UhcCore
 */
public class CaveOresOnlyPopulator extends BlockPopulator {

    private final boolean[][][] explored = new boolean[16][256][16];

    private static final BlockFace[] BLOCK_FACES = new BlockFace[]{
            BlockFace.DOWN,
            BlockFace.UP,
            BlockFace.SOUTH,
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.WEST
    };

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        scanChunk(chunk);
    }

    private void scanChunk(Chunk chunk) {
        // Réinitialiser l'exploration (limité à Y=128 pour optimisation)
        for (int x = 0; x < 16; x++) {
            for (int y = 5; y < 128; y++) {
                for (int z = 0; z < 16; z++) {
                    explored[x][y][z] = false;
                }
            }
        }

        // Scanner tous les blocs (limité à Y=5-128 car la plupart des minerais sont en dessous)
        for (int x = 0; x < 16; x++) {
            for (int y = 5; y < 128; y++) {
                for (int z = 0; z < 16; z++) {
                    Block block = chunk.getBlock(x, y, z);

                    if (isOre(block.getType())) {
                        Vein vein = new Vein(chunk, block);
                        vein.process();
                        if (!vein.isConnectedToAir()) {
                            vein.setToStone();
                        }
                    }
                }
            }
        }
    }

    private boolean isOre(Material material) {
        return material == Material.COAL_ORE ||
               material == Material.IRON_ORE ||
               material == Material.GOLD_ORE ||
               material == Material.DIAMOND_ORE ||
               material == Material.REDSTONE_ORE ||
               material == Material.GLOWING_REDSTONE_ORE ||
               material == Material.LAPIS_ORE ||
               material == Material.EMERALD_ORE ||
               material == Material.QUARTZ_ORE;
    }

    private class Vein {
        private final Set<Block> ores;
        private final Chunk chunk;
        private final Material type;
        private final Block startBlock;

        private Vein(Chunk chunk, Block startBlock) {
            this.ores = new HashSet<>();
            this.chunk = chunk;
            this.type = startBlock.getType();
            this.startBlock = startBlock;
        }

        private void process() {
            getVeinBlocks(startBlock);
        }

        private void getVeinBlocks(Block block) {
            int relX = block.getX() & 0x0000000F;
            int relY = block.getY();
            int relZ = block.getZ() & 0x0000000F;

            if ((block.getX() >> 4) != chunk.getX() ||
                (block.getZ() >> 4) != chunk.getZ() ||
                block.getType() != type ||
                explored[relX][relY][relZ]) {
                return;
            }

            explored[relX][relY][relZ] = true;
            ores.add(block);

            for (BlockFace face : BLOCK_FACES) {
                getVeinBlocks(block.getRelative(face));
            }
        }

        private boolean isConnectedToAir() {
            for (Block block : ores) {
                for (BlockFace face : BLOCK_FACES) {
                    Block adjacentBlock = block.getRelative(face);
                    if ((adjacentBlock.getX() >> 4) == chunk.getX() &&
                        (adjacentBlock.getZ() >> 4) == chunk.getZ()) {
                        Material relative = adjacentBlock.getType();
                        if (relative == Material.AIR || relative == Material.WATER || relative == Material.STATIONARY_WATER) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private void setToStone() {
            for (Block block : ores) {
                block.setType(Material.STONE, false);
            }
        }
    }
}
