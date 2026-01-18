package fr.rammex.simpleuhc.maploader;

import fr.rammex.simpleuhc.utils.RandomUtils;
import fr.rammex.simpleuhc.utils.VeinConfiguration;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Générateur de veines de minerai
 * Adapté depuis UhcCore: https://github.com/Mezy/UhcCore
 */
public class VeinGenerator {

    private final Map<Material, VeinConfiguration> generateVeins;

    public VeinGenerator(Map<Material, VeinConfiguration> generateVeins) {
        this.generateVeins = generateVeins;
    }

    /**
     * Génère des veines aléatoires dans le chunk donné selon la configuration
     * @param chunk : le chunk dans lequel générer les veines
     */
    public void generateVeinsInChunk(Chunk chunk) {
        for (Entry<Material, VeinConfiguration> entry : generateVeins.entrySet()) {
            VeinConfiguration veinCfg = entry.getValue();
            Material material = entry.getKey();

            int randNbrVeins = RandomUtils.randomInteger(veinCfg.getMinVeinsPerChunk(), veinCfg.getMaxVeinsPerChunk());

            for (int i = 0; i < randNbrVeins; i++) {
                int randNbrBlocks = RandomUtils.randomInteger(veinCfg.getMinBlocksPerVein(), veinCfg.getMaxBlocksPerVein());
                if (randNbrBlocks > 0) {
                    int randX = RandomUtils.randomInteger(0, 15);
                    int randY = RandomUtils.randomInteger(veinCfg.getMinY(), veinCfg.getMaxY());
                    int randZ = RandomUtils.randomInteger(0, 15);
                    Block randBlock = tryAdjustingToProperBlock(chunk.getBlock(randX, randY, randZ));
                    if (randBlock != null) {
                        generateVein(material, randBlock, randNbrBlocks);
                    }
                }
            }
        }
    }

    /**
     * Cherche dans un rayon de 5 blocs un bloc de pierre valide
     * @param randBlock le bloc de départ
     * @return un bloc de pierre si trouvé, sinon null
     */
    private Block tryAdjustingToProperBlock(Block randBlock) {
        if (randBlock.getType().equals(Material.STONE)) {
            return randBlock;
        }

        // Descendre pour passer sous l'eau en mer
        if (randBlock.getType().equals(Material.STATIONARY_WATER) || randBlock.getType().equals(Material.WATER)) {
            while ((randBlock.getType().equals(Material.STATIONARY_WATER) || randBlock.getType().equals(Material.WATER))
                    && randBlock.getY() > 10) {
                randBlock = randBlock.getRelative(0, -10, 0);
            }
            if (randBlock.getType().equals(Material.STONE)) {
                return randBlock;
            }
        }

        // Trouver un bloc de pierre à proximité
        for (int i = -5; i <= 5; i++) {
            for (int j = -5; j <= 5; j++) {
                for (int k = -5; k <= 5; k++) {
                    Block relativeBlock = randBlock.getRelative(i, j, k);
                    if (relativeBlock.getType().equals(Material.STONE)) {
                        return relativeBlock;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Génère une veine à partir d'un bloc
     * @param material : le matériau de la veine
     * @param startBlock : le bloc de départ de la veine
     * @param nbrBlocks : le nombre de blocs dans la veine
     */
    private void generateVein(Material material, Block startBlock, int nbrBlocks) {
        List<Block> blocks = getAdjacentsBlocks(startBlock, nbrBlocks);
        for (Block block : blocks) {
            // false = pas de physics update, beaucoup plus rapide
            block.setType(material, false);
        }
    }

    /**
     * Obtient un ensemble de blocs adjacents à partir d'un bloc
     * @param startBlock : le bloc de départ
     * @param nbrBlocks : nombre de blocs adjacents
     * @return la liste des blocs adjacents
     */
    private List<Block> getAdjacentsBlocks(Block startBlock, int nbrBlocks) {
        int failedAttempts = 0;
        List<Block> adjacentBlocks = new ArrayList<>();
        adjacentBlocks.add(startBlock);

        while (adjacentBlocks.size() < nbrBlocks && failedAttempts < 25) {
            // Obtenir un bloc aléatoire dans la liste des blocs choisis
            Block block = adjacentBlocks.get(RandomUtils.randomInteger(0, adjacentBlocks.size() - 1));

            // Face aléatoire
            BlockFace face = RandomUtils.randomAdjacentFace();
            Location blockLocation = block.getLocation();

            if ((blockLocation.getBlockY() <= 1 && face.equals(BlockFace.DOWN)) ||
                (blockLocation.getBlockY() >= 255 && face.equals(BlockFace.UP))) {
                failedAttempts++;
            } else {
                // Trouver un bloc adjacent aléatoire
                Block adjacent = block.getRelative(face);
                if (adjacentBlocks.contains(adjacent) || !adjacent.getType().equals(Material.STONE)) {
                    // On veut uniquement des nouveaux blocs dans la pierre pour éviter de placer des minerais dans l'air
                    failedAttempts++;
                } else {
                    adjacentBlocks.add(adjacent);
                }
            }
        }
        return adjacentBlocks;
    }
}
