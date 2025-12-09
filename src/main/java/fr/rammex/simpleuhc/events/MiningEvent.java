package fr.rammex.simpleuhc.events;

import api.rammex.gameapi.option.Option;
import fr.rammex.simpleuhc.option.OptionSetup;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class MiningEvent implements Listener {

    private final Map<UUID, Integer> playerDiamCount = new HashMap<>();
    private final Random random = new Random();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = event.getPlayer();
        Material mat = event.getBlock().getType();

        if (mat == Material.DIAMOND_ORE) {
            handleDiamondLimit(event, player);
            return;
        }

        if (isLeaves(mat)) {
            handleAppleDrop(event.getBlock().getLocation());
        }

        if (mat == Material.GRAVEL) {
            handleFlintDrop(event.getBlock().getLocation());
        }
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (isLeaves(event.getBlock().getType())) {
            handleAppleDrop(event.getBlock().getLocation());
        }
    }


    private void handleDiamondLimit(BlockBreakEvent event, Player player) {
        Option option = OptionSetup.getOption("Game Diams Limit");
        if (option == null) return;

        int limit = (int) option.getValue();
        int current = playerDiamCount.getOrDefault(player.getUniqueId(), 0);

        if (current >= limit) {
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
            player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT));
            return;
        }

        playerDiamCount.put(player.getUniqueId(), current + 1);
    }


    private void handleAppleDrop(Location location) {
        Option option = OptionSetup.getOption("Apple Drop Rate");
        if (option == null) return;

        double dropRate = (double) option.getValue();
        if (dropRate <= 1.0) return; // Pas de boost


        double vanillaChance = 0.005;
        double boostedChance = vanillaChance * dropRate;

        int applesToDrop = 0;

        int maxTests = (int) Math.ceil(dropRate);
        for (int i = 0; i < maxTests; i++) {
            if (random.nextDouble() < boostedChance) {
                applesToDrop++;
            }
        }

        for (int i = 0; i < applesToDrop; i++) {
            location.getWorld().dropItemNaturally(location, new ItemStack(Material.APPLE));
        }
    }

    private void handleFlintDrop(Location location) {
        Option option = OptionSetup.getOption("Flint Drop Rate");
        if (option == null) return;

        double dropRate = (double) option.getValue();
        if (dropRate <= 1.0) return;

        double vanillaChance = 0.10; // 10%
        double boostedChance = vanillaChance * dropRate;

        int flintToDrop = 0;

        int maxTests = (int) Math.ceil(dropRate);
        for (int i = 0; i < maxTests; i++) {
            if (random.nextDouble() < boostedChance) {
                flintToDrop++;
            }
        }

        for (int i = 0; i < flintToDrop; i++) {
            location.getWorld().dropItemNaturally(location, new ItemStack(Material.FLINT));
        }
    }

    private boolean isLeaves(Material material) {
        return material == Material.LEAVES || material == Material.LEAVES_2;
    }
}