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


    public static void createWorld(){
        World world = org.bukkit.Bukkit.createWorld(new org.bukkit.WorldCreator("UHC_"+getActualDate()));
        if (world != null) {
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doWeatherCycle", "false");
            world.setGameRuleValue("keepInventory", "true");
        }
        world.getWorldBorder().setSize((int) OptionSetup.getOption("World Border").getValue());
        world.getWorldBorder().setCenter(world.getSpawnLocation());
        addSapling();
        taiga();
    }

    public static void teleportPlayer(Player player) {
        World world = org.bukkit.Bukkit.getWorld("UHC_"+getActualDate());
        if (world != null) {
            Location spawnLocation = getRandomLocation();
            if (spawnLocation != null) {
                player.teleport(spawnLocation);
            } else {
                player.sendMessage("Spawn location is not set.");
            }
        } else {
            player.sendMessage("World not found.");
        }

    }

    public static void teleportTeam(){
        TeamManager teamManager = new TeamManager();
        Map<Map<TeamColor, String>, List<Player>> teams = teamManager.getTeams();
        for (teams.values().forEach(team -> {
            for (Player player : team) {
                Location spawnLocation = getRandomLocation();
                if (spawnLocation != null) {
                    player.teleport(spawnLocation);
                } else {
                    player.sendMessage("Spawn location is not set.");
                }
            }
        });;);
    }

    public static void setWorldBorder(int size) {
        World world = org.bukkit.Bukkit.getWorld("UHC_"+getActualDate());
        if (world != null) {
            world.getWorldBorder().setSize(size);
            world.getWorldBorder().setCenter(world.getSpawnLocation());
        } else {
            SimpleUHC.instance.getLogger().warning("World not found for setting world border.");
        }
    }

    private static Location getRandomLocation() {
        World world = org.bukkit.Bukkit.getWorld("UHC_" + getActualDate());
        Location spawnLocation = getSpawnLocation();
        if (world != null && spawnLocation != null) {
            int border = (int) world.getWorldBorder().getSize() / 2;
            for (int attempt = 0; attempt < 100; attempt++) {
                int x = ThreadLocalRandom.current().nextInt(-border, border) + (int) spawnLocation.getX();
                int z = ThreadLocalRandom.current().nextInt(-border, border) + (int) spawnLocation.getZ();
                int y = world.getHighestBlockYAt(x, z);
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
            world.getWorldBorder().setSize(newSize, seconds);
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
                for (int radius = 250, x = 0 - radius; x <= radius; x++) {
                    for (int z = 0 - radius; z <= radius; z++) {
                        Block block = gameWorld.getBlockAt(x, this.YChange, z);
                        if (block.getType() == Material.AIR && (gameWorld.getBlockAt(x, this.YChange - 1, z).getType().equals(Material.DIRT) || gameWorld.getBlockAt(x, this.YChange - 1, z).getType().equals(Material.GRASS))) {
                            int i = ThreadLocalRandom.current().nextInt(100);
                            if (i <= 6)
                                block.getWorld().generateTree(block.getLocation(), TreeType.DARK_OAK);
                            if (i == 90) {
                                block.getWorld().generateTree(block.getLocation(), TreeType.BROWN_MUSHROOM);
                            } else if (i == 91) {
                                block.getWorld().generateTree(block.getLocation(), TreeType.RED_MUSHROOM);
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
