package fr.rammex.simpleuhc.world;

import fr.rammex.simpleuhc.SimpleUHC;
import fr.rammex.simpleuhc.option.OptionSetup;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    public static void setWorldBorder(int size) {
        World world = org.bukkit.Bukkit.getWorld("UHC_"+getActualDate());
        if (world != null) {
            world.getWorldBorder().setSize(size);
            world.getWorldBorder().setCenter(world.getSpawnLocation());
        } else {
            SimpleUHC.instance.getLogger().warning("World not found for setting world border.");
        }
    }

    private static Location getRandomLocation(){
        World world = org.bukkit.Bukkit.getWorld("UHC_"+getActualDate());
        Location spawnLocation = getSpawnLocation();
        if (world != null && spawnLocation != null) {
            int x = (int) (Math.random() * world.getWorldBorder().getSize() - world.getWorldBorder().getSize() / 2 + spawnLocation.getX());
            int z = (int) (Math.random() * world.getWorldBorder().getSize() - world.getWorldBorder().getSize() / 2 + spawnLocation.getZ());
            int y = world.getHighestBlockYAt(x, z);
            return new Location(world, x, y, z);
        } else {
            return null;
        }
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


}
