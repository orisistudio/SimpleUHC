package fr.rammex.simpleuhc.world;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class CustomOrePopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        generateOre(chunk, random, Material.COAL_ORE,
                WorldGenerationConfig.getCoalVeinsPerChunk(),
                WorldGenerationConfig.getCoalVeinSize(),
                WorldGenerationConfig.getCoalMinHeight(),
                WorldGenerationConfig.getCoalMaxHeight());

        generateOre(chunk, random, Material.IRON_ORE,
                WorldGenerationConfig.getIronVeinsPerChunk(),
                WorldGenerationConfig.getIronVeinSize(),
                WorldGenerationConfig.getIronMinHeight(),
                WorldGenerationConfig.getIronMaxHeight());

        generateOre(chunk, random, Material.GOLD_ORE,
                WorldGenerationConfig.getGoldVeinsPerChunk(),
                WorldGenerationConfig.getGoldVeinSize(),
                WorldGenerationConfig.getGoldMinHeight(),
                WorldGenerationConfig.getGoldMaxHeight());

        generateOre(chunk, random, Material.REDSTONE_ORE,
                WorldGenerationConfig.getRedstoneVeinsPerChunk(),
                WorldGenerationConfig.getRedstoneVeinSize(),
                WorldGenerationConfig.getRedstoneMinHeight(),
                WorldGenerationConfig.getRedstoneMaxHeight());

        generateOre(chunk, random, Material.DIAMOND_ORE,
                WorldGenerationConfig.getDiamondVeinsPerChunk(),
                WorldGenerationConfig.getDiamondVeinSize(),
                WorldGenerationConfig.getDiamondMinHeight(),
                WorldGenerationConfig.getDiamondMaxHeight());

        generateOre(chunk, random, Material.LAPIS_ORE,
                WorldGenerationConfig.getLapisVeinsPerChunk(),
                WorldGenerationConfig.getLapisVeinSize(),
                WorldGenerationConfig.getLapisMinHeight(),
                WorldGenerationConfig.getLapisMaxHeight());

        generateOre(chunk, random, Material.EMERALD_ORE,
                WorldGenerationConfig.getEmeraldVeinsPerChunk(),
                WorldGenerationConfig.getEmeraldVeinSize(),
                WorldGenerationConfig.getEmeraldMinHeight(),
                WorldGenerationConfig.getEmeraldMaxHeight());

        generateOre(chunk, random, Material.DIRT,
                WorldGenerationConfig.getDirtVeinsPerChunk(), 32, 0, 128);
        generateOre(chunk, random, Material.GRAVEL,
                WorldGenerationConfig.getGravelVeinsPerChunk(), 32, 0, 128);
    }

    private void generateOre(Chunk chunk, Random random, Material material,
                             int veinsPerChunk, int veinSize,
                             int minHeight, int maxHeight) {

        int actualVeins = veinsPerChunk > 0 ? 1 + random.nextInt(veinsPerChunk) : 0;

        for (int i = 0; i < actualVeins; i++) {
            int x = random.nextInt(16);
            int y = minHeight + random.nextInt(maxHeight - minHeight);
            int z = random.nextInt(16);

            generateVein(chunk, random, x, y, z, material, veinSize);
        }
    }

    private void generateVein(Chunk chunk, Random random, int x, int y, int z,
                              Material material, int size) {
        double angle = random.nextDouble() * Math.PI;

        double x1 = x + Math.sin(angle) * size / 8.0;
        double x2 = x - Math.sin(angle) * size / 8.0;
        double z1 = z + Math.cos(angle) * size / 8.0;
        double z2 = z - Math.cos(angle) * size / 8.0;

        double y1 = y + random.nextInt(3) - 2;
        double y2 = y + random.nextInt(3) - 2;

        for (int i = 0; i <= size; i++) {
            double xPos = x1 + (x2 - x1) * i / size;
            double yPos = y1 + (y2 - y1) * i / size;
            double zPos = z1 + (z2 - z1) * i / size;

            double fuzz = random.nextDouble() * size / 16.0;
            double radius = (Math.sin(i * Math.PI / size) + 1) * fuzz + 1;

            int xStart = (int) Math.floor(xPos - radius / 2.0);
            int yStart = (int) Math.floor(yPos - radius / 2.0);
            int zStart = (int) Math.floor(zPos - radius / 2.0);

            int xEnd = (int) Math.floor(xPos + radius / 2.0);
            int yEnd = (int) Math.floor(yPos + radius / 2.0);
            int zEnd = (int) Math.floor(zPos + radius / 2.0);

            for (int xx = xStart; xx <= xEnd; xx++) {
                if (xx < 0 || xx > 15) continue;

                double xDist = (xx + 0.5 - xPos) / (radius / 2.0);
                if (xDist * xDist < 1) {
                    for (int yy = yStart; yy <= yEnd; yy++) {
                        if (yy < 0 || yy > 127) continue;

                        double yDist = (yy + 0.5 - yPos) / (radius / 2.0);
                        if (xDist * xDist + yDist * yDist < 1) {
                            for (int zz = zStart; zz <= zEnd; zz++) {
                                if (zz < 0 || zz > 15) continue;

                                double zDist = (zz + 0.5 - zPos) / (radius / 2.0);
                                if (xDist * xDist + yDist * yDist + zDist * zDist < 1) {
                                    Block block = chunk.getBlock(xx, yy, zz);
                                    if (block.getType() == Material.STONE) {
                                        block.setType(material);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
