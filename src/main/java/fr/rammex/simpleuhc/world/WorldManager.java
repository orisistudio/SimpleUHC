package fr.rammex.simpleuhc.world;

import fr.rammex.simpleuhc.SimpleUHC;
import fr.rammex.simpleuhc.maploader.BiomeTypePopulator;
import fr.rammex.simpleuhc.maploader.CaveOresOnlyPopulator;
import fr.rammex.simpleuhc.maploader.SugarCanePopulator;
import fr.rammex.simpleuhc.maploader.VeinGenerator;
import fr.rammex.simpleuhc.option.OptionSetup;
import fr.rammex.simpleuhc.team.TeamColor;
import fr.rammex.simpleuhc.team.TeamManager;
import fr.rammex.simpleuhc.threads.ChunkLoaderThread;
import fr.rammex.simpleuhc.utils.VeinConfiguration;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class WorldManager {
    private static World originalWorld;
    private static String currentWorldId = null;
    private static Location spawnLocation = null;
    private static boolean worldReady = false;
    private static int totalChunks = 0;
    private static int chunksGenerated = 0;

    private static void setOriginalWorld(World world) {
        originalWorld = world;
    }

    public static World getOriginalWorld() {
        return originalWorld;
    }

    public static boolean isWorldReady() {
        return worldReady;
    }

    public static void createWorld() {
        worldReady = false;
        chunksGenerated = 0;
        currentWorldId = UUID.randomUUID().toString().substring(0, 8);

        WorldCreator creator = new WorldCreator("UHC_" + currentWorldId);
        creator.generateStructures(true);
        creator.type(WorldType.NORMAL);

        Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "║  Création du monde en cours...     ║");

        World world = Bukkit.createWorld(creator);

        if (world != null) {
            // Ajouter les populators basés sur les options
            addPopulators(world);

            // Configuration des game rules
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doWeatherCycle", "false");
            world.setGameRuleValue("keepInventory", "true");
            world.setGameRuleValue("doMobSpawning", "false");
            world.setDifficulty(Difficulty.HARD);
            world.setTime(6000);

            // Configuration de la world border
            int borderSize = (int) OptionSetup.getOption("World Border").getValue();
            world.getWorldBorder().setCenter(0, 0);
            world.getWorldBorder().setSize(borderSize * 2);

            // Charger et définir le spawn
            world.loadChunk(0, 0, true);
            int spawnY = world.getHighestBlockYAt(0, 0);
            world.setSpawnLocation(0, spawnY, 0);
            spawnLocation = new Location(world, 0, spawnY, 0);
            SimpleUHC.instance.getLogger().info("Spawn point défini à: 0, " + spawnY + ", 0");

            // Calculer le nombre total de chunks
            int maxChunk = Math.round(borderSize / 16f) + 1;
            totalChunks = (2 * maxChunk + 1) * (2 * maxChunk + 1);

            // Pré-générer les chunks avec le nouveau système optimisé
            Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Pré-génération de " + totalChunks + " chunks...");
            Bukkit.broadcastMessage(ChatColor.GRAY + "Cela peut prendre quelques minutes selon la taille du monde.");
            preGenerateChunks(world, borderSize);
        } else {
            SimpleUHC.instance.getLogger().warning("La création du monde a échoué.");
        }
    }

    private static void addPopulators(World world) {
        world.getPopulators().add(new BiomeTypePopulator());
        SimpleUHC.instance.getLogger().info("BiomeTypePopulator activé - Les océans seront remplacés par des forêts");

        // Ajouter CaveOresOnlyPopulator si l'option est activée
        if ((boolean) OptionSetup.getOption("Cave Ores Only").getValue()) {
            world.getPopulators().add(new CaveOresOnlyPopulator());
            SimpleUHC.instance.getLogger().info("CaveOresOnlyPopulator activé - Les minerais seront uniquement dans les caves");
        }

        // Ajouter SugarCanePopulator si le pourcentage est > 0
        int sugarCanePercentage = (int) OptionSetup.getOption("Sugar Cane Percentage").getValue();
        if (sugarCanePercentage > 0) {
            world.getPopulators().add(new SugarCanePopulator(sugarCanePercentage));
            SimpleUHC.instance.getLogger().info("SugarCanePopulator activé - " + sugarCanePercentage + "% de génération");
        }
    }

    private static void preGenerateChunks(World world, int borderSize) {
        // Désactiver l'auto-save temporairement pour plus de performance
        boolean originalAutoSave = world.isAutoSave();
        world.setAutoSave(false);
        SimpleUHC.instance.getLogger().info("Auto-save désactivée pendant la génération");

        // Récupérer le nombre de chunks par tick depuis les options
        int chunksPerTick = 20;
        Bukkit.getLogger().info("Nombre de chunks par batch: " + chunksPerTick);
        int restDuration = 2; // Pause de 2 ticks entre chaque batch

        // Configurer VeinGenerator si l'option est activée
        VeinGenerator veinGenerator = null;
        if ((boolean) OptionSetup.getOption("Generate Custom Veins").getValue()) {
            veinGenerator = createVeinGenerator();
            SimpleUHC.instance.getLogger().info("VeinGenerator activé - Veines de minerai personnalisées");
        }

        final VeinGenerator finalVeinGenerator = veinGenerator;
        final boolean finalOriginalAutoSave = originalAutoSave;

        ChunkLoaderThread chunkLoaderThread = new ChunkLoaderThread(world, borderSize, chunksPerTick, restDuration) {
            private long startTime = System.currentTimeMillis();
            private int lastPercentage = 0;

            @Override
            public void onDoneLoadingWorld() {
                worldReady = true;
                long totalTime = (System.currentTimeMillis() - startTime) / 1000;

                final World world = Bukkit.getWorld("UHC_" + currentWorldId);

                if (world != null) {
                    // Réactiver l'auto-save
                    world.setAutoSave(finalOriginalAutoSave);
                    SimpleUHC.instance.getLogger().info("Auto-save réactivée");

                    // Les chunks se déchargeront automatiquement
                    SimpleUHC.instance.getLogger().info("Les chunks seront gérés automatiquement par le serveur");
                }

                Bukkit.getLogger().info("");
                Bukkit.getLogger().info(ChatColor.GREEN + "" + ChatColor.BOLD + "╔════════════════════════════════════════════╗");
                Bukkit.getLogger().info(ChatColor.GREEN + "" + ChatColor.BOLD + "║      ✓ Monde créé avec succès !            ║");
                Bukkit.getLogger().info(ChatColor.GREEN + "" + ChatColor.BOLD + "╠════════════════════════════════════════════╣");
                Bukkit.getLogger().info(ChatColor.YELLOW + "  Chunks générés: " + ChatColor.WHITE + chunksGenerated);
                Bukkit.getLogger().info(ChatColor.YELLOW + "  Temps total: " + ChatColor.WHITE + totalTime + "s");
                Bukkit.getLogger().info(ChatColor.YELLOW + "  Vitesse: " + ChatColor.WHITE + (chunksGenerated / Math.max(1, totalTime)) + " chunks/s");
                Bukkit.getLogger().info(ChatColor.GREEN + "" + ChatColor.BOLD + "╠════════════════════════════════════════════╣");
                Bukkit.getLogger().info(ChatColor.GREEN + "" + ChatColor.BOLD + "║  Vous pouvez maintenant lancer la partie ! ║");
                Bukkit.getLogger().info(ChatColor.GREEN + "" + ChatColor.BOLD + "╚════════════════════════════════════════════╝");
                Bukkit.getLogger().info("");

                SimpleUHC.instance.getLogger().info("Génération terminée en " + totalTime + "s");
            }

            @Override
            public void onDoneLoadingChunk(Chunk chunk) {
                chunksGenerated++;

                // Générer des veines de minerai personnalisées si activé
                if (finalVeinGenerator != null) {
                    finalVeinGenerator.generateVeinsInChunk(chunk);
                }

                // Afficher la progression tous les 5%
                int currentPercentage = (chunksGenerated * 100) / totalChunks;
                if (currentPercentage >= lastPercentage + 5 && currentPercentage <= 100) {
                    lastPercentage = currentPercentage;

                    // Barre de progression visuelle
                    int barLength = 20;
                    int filled = (currentPercentage * barLength) / 100;
                    StringBuilder bar = new StringBuilder( "[");
                    for (int i = 0; i < barLength; i++) {
                        if (i < filled) {
                            bar.append("█");
                        } else {
                            bar.append("░");
                        }
                    }
                    bar.append("]");

                    long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                    int chunksPerSecond = elapsed > 0 ? (int)(chunksGenerated / elapsed) : 0;
                    int estimatedRemaining = chunksPerSecond > 0 ? (totalChunks - chunksGenerated) / chunksPerSecond : 0;

                    Bukkit.getLogger().info("Génération: " + bar.toString() +
                             " " + currentPercentage + "% " +
                            "(" + chunksGenerated + "/" + totalChunks + " chunks)");

                    if (estimatedRemaining > 0) {
                        Bukkit.getLogger().info("Temps restant estimé: ~" + estimatedRemaining + "s " +
                                "(" + chunksPerSecond + " chunks/s)");
                    }
                }
            }
        };

        chunkLoaderThread.printSettings();
        Bukkit.getScheduler().runTask(SimpleUHC.instance, chunkLoaderThread);
    }

    private static VeinGenerator createVeinGenerator() {
        Map<Material, VeinConfiguration> veins = new HashMap<>();

        // Coal - Charbon
        int coalVeins = (int) OptionSetup.getOption("Coal Veins Per Chunk").getValue();
        int coalSize = (int) OptionSetup.getOption("Coal Vein Size").getValue();
        int coalMinY = (int) OptionSetup.getOption("Coal Min Height").getValue();
        int coalMaxY = (int) OptionSetup.getOption("Coal Max Height").getValue();
        veins.put(Material.COAL_ORE, new VeinConfiguration(coalVeins, coalVeins, coalSize, coalSize, coalMinY, coalMaxY));

        // Iron - Fer
        int ironVeins = (int) OptionSetup.getOption("Iron Veins Per Chunk").getValue();
        int ironSize = (int) OptionSetup.getOption("Iron Vein Size").getValue();
        int ironMinY = (int) OptionSetup.getOption("Iron Min Height").getValue();
        int ironMaxY = (int) OptionSetup.getOption("Iron Max Height").getValue();
        veins.put(Material.IRON_ORE, new VeinConfiguration(ironVeins, ironVeins, ironSize, ironSize, ironMinY, ironMaxY));

        // Gold - Or
        int goldVeins = (int) OptionSetup.getOption("Gold Veins Per Chunk").getValue();
        int goldSize = (int) OptionSetup.getOption("Gold Vein Size").getValue();
        int goldMinY = (int) OptionSetup.getOption("Gold Min Height").getValue();
        int goldMaxY = (int) OptionSetup.getOption("Gold Max Height").getValue();
        veins.put(Material.GOLD_ORE, new VeinConfiguration(goldVeins, goldVeins, goldSize, goldSize, goldMinY, goldMaxY));

        // Diamond - Diamant
        int diamondVeins = (int) OptionSetup.getOption("Diamond Veins Per Chunk").getValue();
        int diamondSize = (int) OptionSetup.getOption("Diamond Vein Size").getValue();
        int diamondMinY = (int) OptionSetup.getOption("Diamond Min Height").getValue();
        int diamondMaxY = (int) OptionSetup.getOption("Diamond Max Height").getValue();
        veins.put(Material.DIAMOND_ORE, new VeinConfiguration(diamondVeins, diamondVeins, diamondSize, diamondSize, diamondMinY, diamondMaxY));

        // Redstone
        int redstoneVeins = (int) OptionSetup.getOption("Redstone Veins Per Chunk").getValue();
        int redstoneSize = (int) OptionSetup.getOption("Redstone Vein Size").getValue();
        int redstoneMinY = (int) OptionSetup.getOption("Redstone Min Height").getValue();
        int redstoneMaxY = (int) OptionSetup.getOption("Redstone Max Height").getValue();
        veins.put(Material.REDSTONE_ORE, new VeinConfiguration(redstoneVeins, redstoneVeins, redstoneSize, redstoneSize, redstoneMinY, redstoneMaxY));

        // Lapis
        int lapisVeins = (int) OptionSetup.getOption("Lapis Veins Per Chunk").getValue();
        int lapisSize = (int) OptionSetup.getOption("Lapis Vein Size").getValue();
        int lapisMinY = (int) OptionSetup.getOption("Lapis Min Height").getValue();
        int lapisMaxY = (int) OptionSetup.getOption("Lapis Max Height").getValue();
        veins.put(Material.LAPIS_ORE, new VeinConfiguration(lapisVeins, lapisVeins, lapisSize, lapisSize, lapisMinY, lapisMaxY));

        // Emerald - Émeraude
        int emeraldVeins = (int) OptionSetup.getOption("Emerald Veins Per Chunk").getValue();
        int emeraldSize = (int) OptionSetup.getOption("Emerald Vein Size").getValue();
        int emeraldMinY = (int) OptionSetup.getOption("Emerald Min Height").getValue();
        int emeraldMaxY = (int) OptionSetup.getOption("Emerald Max Height").getValue();
        veins.put(Material.EMERALD_ORE, new VeinConfiguration(emeraldVeins, emeraldVeins, emeraldSize, emeraldSize, emeraldMinY, emeraldMaxY));

        // Dirt - Terre
        int dirtVeins = (int) OptionSetup.getOption("Dirt Veins Per Chunk").getValue();
        veins.put(Material.DIRT, new VeinConfiguration(dirtVeins, dirtVeins, 32, 32, 0, 128));

        // Gravel - Gravier
        int gravelVeins = (int) OptionSetup.getOption("Gravel Veins Per Chunk").getValue();
        veins.put(Material.GRAVEL, new VeinConfiguration(gravelVeins, gravelVeins, 32, 32, 0, 128));

        return new VeinGenerator(veins);
    }

    public static void teleportPlayer(Player player) {
        if (!worldReady) {
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "⚠ Le monde n'est pas encore prêt !");
            player.sendMessage(ChatColor.YELLOW + "Veuillez attendre la fin de la génération...");
            player.sendMessage(ChatColor.GRAY + "Progression: " + chunksGenerated + "/" + totalChunks + " chunks (" +
                    (totalChunks > 0 ? (chunksGenerated * 100 / totalChunks) : 0) + "%)");
            return;
        }

        setOriginalWorld(player.getWorld());
        World world = Bukkit.getWorld("UHC_" + currentWorldId);

        if (world != null) {
            Location randomLocation = getRandomLocation();
            if (randomLocation != null) {
                player.setHealth(20.0);
                player.setFoodLevel(20);
                player.teleport(randomLocation);
            } else {
                player.sendMessage(ChatColor.RED + "Impossible de trouver une position de spawn valide.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Le monde UHC n'existe pas.");
        }
    }

    public static void teleportTeam() {
        if (!worldReady) {
            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "⚠ Le monde n'est pas encore prêt !");
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Veuillez attendre la fin de la génération...");
            Bukkit.broadcastMessage(ChatColor.GRAY + "Progression: " + chunksGenerated + "/" + totalChunks + " chunks (" +
                    (totalChunks > 0 ? (chunksGenerated * 100 / totalChunks) : 0) + "%)");
            return;
        }

        TeamManager teamManager = new TeamManager();
        Map<Map<TeamColor, String>, List<Player>> teams = teamManager.getTeams();

        teams.values().forEach(team -> {
            Location randomLocation = getRandomLocation();
            for (Player player : team) {
                setOriginalWorld(player.getWorld());
                if (randomLocation != null) {
                    player.setHealth(20.0);
                    player.setFoodLevel(20);
                    player.teleport(randomLocation);
                } else {
                    player.sendMessage(ChatColor.RED + "Impossible de trouver une position de spawn valide.");
                }
            }
        });

        for (Player player : Bukkit.getOnlinePlayers()) {
            setOriginalWorld(player.getWorld());
            if (!teamManager.isPlayerInAnyTeam(player)) {
                Location randomLocation = getRandomLocation();
                if (randomLocation != null) {
                    player.setHealth(20.0);
                    player.setFoodLevel(20);
                    player.teleport(randomLocation);
                } else {
                    player.sendMessage(ChatColor.RED + "Impossible de trouver une position de spawn valide.");
                }
            }
        }
    }

    private static Location getRandomLocation() {
        World world = Bukkit.getWorld("UHC_" + currentWorldId);

        if (world == null || spawnLocation == null) {
            return null;
        }

        int border = (int) world.getWorldBorder().getSize() / 2;

        // Essayer de trouver une position valide
        for (int attempt = 0; attempt < 100; attempt++) {
            int x = ThreadLocalRandom.current().nextInt(-border, border);
            int z = ThreadLocalRandom.current().nextInt(-border, border);

            // Les chunks sont déjà pré-générés, pas besoin de les charger
            int y = world.getHighestBlockYAt(x, z);

            if (y < 50) continue; // Trop bas (probablement dans l'eau/océan)

            Block block = world.getBlockAt(x, y - 1, z);
            Block blockAbove = world.getBlockAt(x, y, z);

            if (blockAbove.getType() == Material.AIR &&
                    block.getType() != Material.WATER &&
                    block.getType() != Material.LAVA &&
                    block.getType().isSolid()) {
                return new Location(world, x + 0.5, y, z + 0.5);
            }
        }

        return null;
    }

    public static void shrinkWorldBorder(int newSize, long seconds) {
        World world = Bukkit.getWorld("UHC_" + currentWorldId);
        if (world != null) {
            world.getWorldBorder().setSize(newSize * 2, seconds);
        } else {
            SimpleUHC.instance.getLogger().warning("Monde introuvable pour réduire la bordure.");
        }
    }
}
