// EnchantDiamLimitListener.java
package fr.rammex.simpleuhc.events;

import api.rammex.gameapi.option.Option;
import fr.rammex.simpleuhc.option.OptionSetup;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MiningEvent implements Listener {

    private final Map<UUID, Integer> playerDiamCount = new HashMap<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = event.getPlayer();

        Material mat = event.getBlock().getType();
        if (mat != Material.DIAMOND_ORE) return;

        Option option = OptionSetup.getOption("Game Diams Limit");
        if (option == null) return;

        int limit = (int) option.getValue();
        int current = playerDiamCount.getOrDefault(player.getUniqueId(), 0);

        if (current >= limit) {
            event.setCancelled(true);
            player.sendMessage("§cTu as atteint la limite de diamants minables (" + limit + ").");
            return;
        }

        playerDiamCount.put(player.getUniqueId(), current + 1);
    }
}