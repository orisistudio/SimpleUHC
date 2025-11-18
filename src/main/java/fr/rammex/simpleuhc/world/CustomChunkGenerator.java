package fr.rammex.simpleuhc.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CustomChunkGenerator extends ChunkGenerator {

    private final SimplexNoise heightNoise;
    private final SimplexNoise biomeNoise;
    private final SimplexNoise detailNoise;

    public CustomChunkGenerator() {
        long seed = System.currentTimeMillis();
        this.heightNoise = new SimplexNoise(seed);
        this.biomeNoise = new SimplexNoise(seed + 1000);
        this.detailNoise = new SimplexNoise(seed + 2000);
    }

    @Override
    public byte[] generate(World world, Random random, int chunkX, int chunkZ) {
        byte[] result = new byte[32768]; // 16x16x128 pour 1.8

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX * 16 + x;
                int worldZ = chunkZ * 16 + z;

                // G\u00e9n\u00e9ration de la hauteur du terrain avec plusieurs octaves de bruit
                double height = getHeight(worldX, worldZ);

                // Convertir la hauteur en blocs (utilise les options configurables)
                int minHeight = WorldGenerationConfig.getTerrainMinHeight();
                int maxHeight = WorldGenerationConfig.getTerrainMaxHeight();
                int terrainHeight = (int) (minHeight + height * (maxHeight - minHeight));

                for (int y = 0; y < 128; y++) {
                    int index = (x * 16 + z) * 128 + y;

                    if (y == 0) {
                        // Bedrock au fond
                        result[index] = (byte) Material.BEDROCK.getId();
                    } else if (y < terrainHeight - 4) {
                        // Pierre en profondeur
                        result[index] = (byte) Material.STONE.getId();
                    } else if (y < terrainHeight - 1) {
                        // Terre pr\u00e8s de la surface
                        result[index] = (byte) Material.DIRT.getId();
                    } else if (y == terrainHeight - 1) {
                        // Herbe en surface
                        Biome biome = getBiomeAt(worldX, worldZ);
                        if (biome == Biome.DESERT || biome == Biome.DESERT_HILLS) {
                            result[index] = (byte) Material.SAND.getId();
                        } else if (biome == Biome.MESA) {
                            result[index] = (byte) Material.HARD_CLAY.getId();
                        } else {
                            result[index] = (byte) Material.GRASS.getId();
                        }
                    } else if (y >= terrainHeight) {
                        // Air au-dessus
                        result[index] = (byte) Material.AIR.getId();
                    }
                }
            }
        }

        return result;
    }

    private double getHeight(int x, int z) {
        // Combiner plusieurs octaves de bruit pour cr\u00e9er un terrain vari\u00e9
        // L'\u00e9chelle contr\u00f4le le relief : valeur faible = plat, valeur \u00e9lev\u00e9e = montagneux
        double terrainScale = WorldGenerationConfig.getTerrainScale();

        double scale1 = 0.005 * terrainScale;
        double scale2 = 0.02 * terrainScale;
        double scale3 = 0.05 * terrainScale;

        double noise1 = heightNoise.noise(x * scale1, z * scale1) * 1.0;
        double noise2 = heightNoise.noise(x * scale2, z * scale2) * 0.5;
        double noise3 = detailNoise.noise(x * scale3, z * scale3) * 0.25;

        double combined = (noise1 + noise2 + noise3) / 1.75;

        // Normaliser entre 0 et 1
        return (combined + 1) / 2;
    }

    private Biome getBiomeAt(int x, int z) {
        // Vérifier si un biome spécifique est configuré
        String configuredBiome = WorldGenerationConfig.getWorldBiome();

        if (configuredBiome != null && !configuredBiome.equalsIgnoreCase("AUTO")) {
            // Essayer de convertir le nom en biome
            try {
                return Biome.valueOf(configuredBiome.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Si le biome n'est pas valide, continuer avec la génération automatique
                System.err.println("[SimpleUHC] Biome invalide: " + configuredBiome + ", utilisation de la génération automatique.");
            }
        }

        // Génération automatique par bruit (comportement par défaut)
        // Utiliser le bruit pour déterminer les biomes (sans biomes aquatiques)
        double biomeValue = biomeNoise.noise(x * 0.001, z * 0.001);
        double temperatureValue = biomeNoise.noise(x * 0.002, z * 0.002);

        // Liste de biomes terrestres uniquement (pas d'océans, rivières, etc.)
        if (biomeValue < -0.5) {
            return Biome.PLAINS;
        } else if (biomeValue < -0.2) {
            if (temperatureValue > 0.3) {
                return Biome.SAVANNA;
            } else {
                return Biome.FOREST;
            }
        } else if (biomeValue < 0.0) {
            return Biome.BIRCH_FOREST;
        } else if (biomeValue < 0.2) {
            if (temperatureValue > 0.5) {
                return Biome.DESERT;
            } else if (temperatureValue < -0.3) {
                return Biome.TAIGA;
            } else {
                return Biome.ROOFED_FOREST;
            }
        } else if (biomeValue < 0.4) {
            if (temperatureValue > 0.4) {
                return Biome.MESA;
            } else {
                return Biome.MEGA_TAIGA;
            }
        } else if (biomeValue < 0.6) {
            if (temperatureValue < -0.2) {
                return Biome.ICE_PLAINS;
            } else {
                return Biome.EXTREME_HILLS;
            }
        } else {
            if (temperatureValue > 0.3) {
                return Biome.JUNGLE;
            } else {
                return Biome.SWAMPLAND;
            }
        }
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList(new CustomTreePopulator(), new CustomOrePopulator());
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, 80, 0);
    }
}
