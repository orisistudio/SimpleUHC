package fr.rammex.simpleuhc.events;

import api.rammex.gameapi.option.Option;
import api.rammex.gameapi.option.OptionType;
import fr.rammex.simpleuhc.option.OptionSetup;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EnchantListener implements Listener {
    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        ItemStack item = event.getItem();
        Material mat = item.getType();

        Option option = null;
        if (mat.name().startsWith("IRON_")) {
            option = OptionSetup.getOption("Iron Alowed Enchants");
        } else if (mat.name().startsWith("DIAMOND_")) {
            option = OptionSetup.getOption("Diamond Alowed Enchants");
        }

        if (option != null && option.getType() == OptionType.ENCHANT) {
            Map<String, Boolean> allowed = (Map<String, Boolean>) option.getValue();

            event.getEnchantsToAdd().entrySet().removeIf(e -> {
                Enchantment ench = e.getKey();
                int level = e.getValue();
                String key = ench.getName() + "_" + level;
                return !allowed.getOrDefault(key, false);
            });
        }
    }

    @EventHandler
    public void onAnvilClick(InventoryClickEvent event) {
        if (event.getInventory().getType() != InventoryType.ANVIL) return;

        AnvilInventory anvil = (AnvilInventory) event.getInventory();
        ItemStack left = anvil.getItem(0);
        ItemStack right = anvil.getItem(1);

        if (left == null || right == null) return;

        org.bukkit.Material mat = left.getType();
        Option option = null;
        if (mat.name().startsWith("IRON_")) {
            option = OptionSetup.getOption("Iron Alowed Enchants");
        } else if (mat.name().startsWith("DIAMOND_")) {
            option = OptionSetup.getOption("Diamond Alowed Enchants");
        }
        if (option == null || option.getType() != OptionType.ENCHANT) return;

        Map<String, Boolean> allowed = (Map<String, Boolean>) option.getValue();

        if (right.hasItemMeta() && right.getItemMeta().hasEnchants()) {
            for (Map.Entry<Enchantment, Integer> entry : right.getItemMeta().getEnchants().entrySet()) {
                String key = entry.getKey().getName() + "_" + entry.getValue();
                if (!allowed.getOrDefault(key, false)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}