package fr.rammex.simpleuhc.utils;

import org.bukkit.block.BlockFace;

import java.util.Random;

/**
 * Utilitaires pour la génération aléatoire
 * Adapté depuis UhcCore: https://github.com/Mezy/UhcCore
 */
public class RandomUtils {

    private static final Random random = new Random();

    private static final BlockFace[] ADJACENT_FACES = new BlockFace[]{
            BlockFace.DOWN,
            BlockFace.UP,
            BlockFace.NORTH,
            BlockFace.SOUTH,
            BlockFace.EAST,
            BlockFace.WEST
    };

    /**
     * Génère un entier aléatoire entre min et max (inclus)
     */
    public static int randomInteger(int min, int max) {
        if (min == max) {
            return min;
        }
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Retourne une face de bloc adjacente aléatoire
     */
    public static BlockFace randomAdjacentFace() {
        return ADJACENT_FACES[random.nextInt(ADJACENT_FACES.length)];
    }
}
