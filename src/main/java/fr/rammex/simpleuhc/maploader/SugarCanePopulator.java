package fr.rammex.simpleuhc.maploader;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

/**
 * Populator qui génère des cannes à sucre près de l'eau
 * Adapté depuis UhcCore: https://github.com/Mezy/UhcCore
 */
public class SugarCanePopulator extends BlockPopulator {

    private final int percentage;

    public SugarCanePopulator(int percentage) {
        this.percentage = percentage;
    }

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        for (int x = 1; x < 15; x++) {
            for (int z = 1; z < 15; z++) {
                Block block = world.getHighestBlockAt(chunk.getBlock(x, 0, z).getLocation());
                Block below = block.getRelative(BlockFace.DOWN);

                if (percentage > random.nextInt(100) && (below.getType() == Material.SAND || below.getType() == Material.GRASS)) {

                    // Vérifier si de l'eau est à proximité
                    if (
                        below.getRelative(BlockFace.NORTH).getType() == Material.WATER ||
                        below.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER ||
                        below.getRelative(BlockFace.EAST).getType() == Material.WATER ||
                        below.getRelative(BlockFace.EAST).getType() == Material.STATIONARY_WATER ||
                        below.getRelative(BlockFace.SOUTH).getType() == Material.WATER ||
                        below.getRelative(BlockFace.SOUTH).getType() == Material.STATIONARY_WATER ||
                        below.getRelative(BlockFace.WEST).getType() == Material.WATER ||
                        below.getRelative(BlockFace.WEST).getType() == Material.STATIONARY_WATER
                    ) {
                        if (block.getType() == Material.AIR) {
                            // Générer une canne à sucre de hauteur aléatoire (1-3 blocs)
                            int height = random.nextInt(3) + 1;
                            Location location = block.getLocation();
                            while (height > 0) {
                                world.getBlockAt(location).setType(Material.SUGAR_CANE_BLOCK);
                                location = location.add(0, 1, 0);
                                height--;
                            }
                        }
                    }
                }
            }
        }
    }
}
