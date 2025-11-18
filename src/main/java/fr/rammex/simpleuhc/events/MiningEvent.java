// EnchantDiamLimitListener.java
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

        // Gestion de la limite de diamants
        if (mat == Material.DIAMOND_ORE) {
            handleDiamondLimit(event, player);
            return;
        }

        // Gestion du drop de pommes pour les feuilles
        if (isLeaves(mat)) {
            handleAppleDrop(event.getBlock().getLocation());
        }

        // Gestion du drop de silex pour le gravier
        if (mat == Material.GRAVEL) {
            handleFlintDrop(event.getBlock().getLocation());
        }
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        // Gestion du drop de pommes pour les feuilles qui se cassent naturellement
        if (isLeaves(event.getBlock().getType())) {
            handleAppleDrop(event.getBlock().getLocation());
        }
    }

    /**
     * Gère la limite de diamants minables
     */
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

    /**
     * Gère le drop boosté de pommes pour les feuilles
     */
    private void handleAppleDrop(Location location) {
        Option option = OptionSetup.getOption("Apple Drop Rate");
        if (option == null) return;

        double dropRate = (double) option.getValue();
        if (dropRate <= 1.0) return; // Pas de boost

        // Chance de base vanilla pour les feuilles de chêne et de chêne noir : 0.5% (1/200)
        // On augmente cette chance en fonction du multiplicateur
        double vanillaChance = 0.005; // 0.5%
        double boostedChance = vanillaChance * dropRate;

        // Calcul du nombre de pommes à drop
        int applesToDrop = 0;

        // On teste plusieurs fois pour permettre le drop de plusieurs pommes avec des taux élevés
        int maxTests = (int) Math.ceil(dropRate);
        for (int i = 0; i < maxTests; i++) {
            if (random.nextDouble() < boostedChance) {
                applesToDrop++;
            }
        }

        // Drop des pommes
        for (int i = 0; i < applesToDrop; i++) {
            location.getWorld().dropItemNaturally(location, new ItemStack(Material.APPLE));
        }
    }

    /**
     * Gère le drop boosté de silex pour le gravier
     */
    private void handleFlintDrop(Location location) {
        Option option = OptionSetup.getOption("Flint Drop Rate");
        if (option == null) return;

        double dropRate = (double) option.getValue();
        if (dropRate <= 1.0) return; // Pas de boost

        // Chance de base vanilla pour le silex : 10% (1/10)
        // On augmente cette chance en fonction du multiplicateur
        double vanillaChance = 0.10; // 10%
        double boostedChance = vanillaChance * dropRate;

        // Calcul du nombre de silex à drop
        int flintToDrop = 0;

        // On teste plusieurs fois pour permettre le drop de plusieurs silex avec des taux élevés
        int maxTests = (int) Math.ceil(dropRate);
        for (int i = 0; i < maxTests; i++) {
            if (random.nextDouble() < boostedChance) {
                flintToDrop++;
            }
        }

        // Drop des silex
        for (int i = 0; i < flintToDrop; i++) {
            location.getWorld().dropItemNaturally(location, new ItemStack(Material.FLINT));
        }
    }

    /**
     * Vérifie si le matériau est un type de feuilles
     */
    private boolean isLeaves(Material material) {
        return material == Material.LEAVES || material == Material.LEAVES_2;
    }
}