package fr.rammex.simpleuhc.world;

import fr.rammex.simpleuhc.SimpleUHC;
import fr.rammex.simpleuhc.option.OptionSetup;
import fr.rammex.simpleuhc.team.TeamColor;
import fr.rammex.simpleuhc.team.TeamManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class WorldManager {
    private static World oriniginalWorld;

    private static void setOriginalWorld(World world) {
        oriniginalWorld = world;
    }

    public static World getOriginalWorld() {
        return oriniginalWorld;
    }



    public static void createWorld(){
        WorldCreator creator = new WorldCreator("UHC_" + getActualDate());

        // Utiliser le g\u00e9n\u00e9rateur personnalis\u00e9 avec des biomes terrestres uniquement
        creator.generator(new CustomChunkGenerator());

        Bukkit.broadcastMessage("\u00a7a\u00a7lCr\u00e9ation du monde personnalis\u00e9 en cours, des freezes peuvent survenir...");
        World world = org.bukkit.Bukkit.createWorld(creator);
        if (world != null) {
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doWeatherCycle", "false");
            world.setGameRuleValue("keepInventory", "true");
            world.getWorldBorder().setCenter(0, 0);
            world.getWorldBorder().setSize((int) OptionSetup.getOption("World Border").getValue()*2);
            world.setDifficulty(Difficulty.HARD);

            addSapling();
            //taiga();
        } else {
            SimpleUHC.instance.getLogger().warning("La cr\u00e9ation du monde a \u00e9chou\u00e9.");
        }
    }

    public static void teleportPlayer(Player player) {
        setOriginalWorld(player.getWorld());
        World world = org.bukkit.Bukkit.getWorld("UHC_"+getActualDate());
        if (world != null) {
            Location spawnLocation = getRandomLocation();
            if (spawnLocation != null) {
                player.setHealth(20.0);
                player.setFoodLevel(20);
                player.teleport(spawnLocation);
            } else {
                player.sendMessage("Spawn location is not set.");
            }
        } else {
            player.sendMessage("World not found.");
        }

    }

    public static void teleportTeam() {
        TeamManager teamManager = new TeamManager();
        Map<Map<TeamColor, String>, List<Player>> teams = teamManager.getTeams();

        // Téléporte chaque équipe
        teams.values().forEach(team -> {
            System.out.println("Teleporting team of size: " + team.size());
            Location spawnLocation = getRandomLocation();
            for (Player player : team) {
                setOriginalWorld(player.getWorld());
                if (spawnLocation != null) {
                    player.setHealth(20.0);
                    player.setFoodLevel(20);
                    player.teleport(spawnLocation);
                } else {
                    player.sendMessage("Spawn location is not set.");
                }
            }
        });

        // Téléporte les joueurs sans équipe
        for (Player player : Bukkit.getOnlinePlayers()) {
            setOriginalWorld(player.getWorld());
            System.out.println("Teleporting player: " + player.getName());
            if (!teamManager.isPlayerInAnyTeam(player)) {
                Location spawnLocation = getRandomLocation();
                if (spawnLocation != null) {
                    player.setHealth(20.0);
                    player.setFoodLevel(20);
                    player.teleport(spawnLocation);
                } else {
                    player.sendMessage("Spawn location is not set.");
                }
            }
        }
    }



    // Java
    private static Location getRandomLocation() {
        World world = org.bukkit.Bukkit.getWorld("UHC_" + getActualDate());
        Location spawnLocation = getSpawnLocation();
        if (world != null && spawnLocation != null) {
            int border = (int) world.getWorldBorder().getSize() / 2;
            for (int attempt = 0; attempt < 100; attempt++) {
                int x = ThreadLocalRandom.current().nextInt(-border, border) + (int) spawnLocation.getX();
                int z = ThreadLocalRandom.current().nextInt(-border, border) + (int) spawnLocation.getZ();
                int y = world.getHighestBlockYAt(x, z);
                if (y < 50) continue;
                Block block = world.getBlockAt(x, y - 1, z);
                Block blockAbove = world.getBlockAt(x, y, z);
                if (blockAbove.getType() == Material.AIR &&
                        block.getType() != Material.WATER &&
                        block.getType() != Material.LAVA &&
                        block.getType().isSolid()) {
                    return new Location(world, x, y, z);
                }
            }
        }
        return null;
    }

    private static Location getSpawnLocation(){
        World world = org.bukkit.Bukkit.getWorld("UHC_"+getActualDate());
        if (world != null) {
            return world.getSpawnLocation();
        } else {
            return null;
        }
    }

    private static String getActualDate(){
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        return today.format(formatter);
    }

    // rajouter method pour réduire progressivement la world border
    public static void shrinkWorldBorder(int newSize, long seconds) {
        World world = org.bukkit.Bukkit.getWorld("UHC_" + getActualDate());
        if (world != null) {
            world.getWorldBorder().setSize(newSize*2, seconds);
        } else {
            SimpleUHC.instance.getLogger().warning("World not found for shrinking world border.");
        }
    }

    private static void addSapling() {
        World gameWorld = org.bukkit.Bukkit.getWorld("UHC_" + getActualDate());
        System.out.println("Start the creation of the roofed forest");
        (new Thread(() -> (new BukkitRunnable() {
            int yInicial = 50;
            int progress = 0;
            int YChange = this.yInicial;
            @Override
            public void run() {
                for (int radius = 300, x = 0 - radius; x <= radius; x++) {
                    for (int z = 0 - radius; z <= radius; z++) {
                        Block block = gameWorld.getBlockAt(x, this.YChange, z);
                        if (block.getType() == Material.AIR &&
                                (gameWorld.getBlockAt(x, this.YChange - 1, z).getType().equals(Material.DIRT) ||
                                        gameWorld.getBlockAt(x, this.YChange - 1, z).getType().equals(Material.GRASS))) {
                            int i = ThreadLocalRandom.current().nextInt(30);
                            if (i <= 5 && isAreaFree(gameWorld, x, this.YChange, z, 3)) {
                                block.getWorld().generateTree(block.getLocation(), TreeType.DARK_OAK);
                            }
                        }
                    }
                }
                this.YChange++;
                this.progress++;
                if (this.progress >= 100){
                    this.cancel();
                }
            }
        }).runTaskTimer(SimpleUHC.instance, 1L, 5L))).run();
        System.out.println("Finish the creation of the roofed forest ");

    }

    private static boolean isAreaFree(World world, int x, int y, int z, int minDistance) {
        for (int dx = -minDistance; dx <= minDistance; dx++) {
            for (int dz = -minDistance; dz <= minDistance; dz++) {
                if (dx == 0 && dz == 0) continue;
                Block b = world.getBlockAt(x + dx, y, z + dz);
                if (b.getType() == Material.LOG || b.getType() == Material.LOG_2) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void taiga(){
        World world = org.bukkit.Bukkit.getWorld("UHC_" + getActualDate());
        System.out.println("Start the creation of the taiga");
        (new Thread(() -> (new BukkitRunnable() {
            int yInicial = 50;

            int progress = 0;

            int YChange = this.yInicial;
            @Override
            public void run() {
                for (int radius = 250, x = 0 - radius; x <= radius; x++) {
                    for (int z = 0 - radius; z <= radius; z++) {
                        Block block = world.getBlockAt(x, this.YChange, z);
                        if (block.getType() == Material.AIR && (world.getBlockAt(x, this.YChange - 1, z).getType().equals(Material.DIRT) || world.getBlockAt(x, this.YChange - 1, z).getType().equals(Material.GRASS))) {
                            int i = ThreadLocalRandom.current().nextInt(36);
                            if (i <= 34)
                                block.getWorld().generateTree(block.getLocation(), TreeType.REDWOOD);
                        }
                    }
                }
                this.YChange++;
                this.progress++;
                if (this.progress >= 100){
                    this.cancel();
                }
            }
        }).runTaskTimer(SimpleUHC.instance, 1L, 5L))).run();
    }
}

    // https://github.com/Antoo42/UHC/blob/master/src/main/java/fr/anto42/emma/coreManager/worldManager/WorldPopulator.java
    // méthode pour gen les arbres dans le monde
