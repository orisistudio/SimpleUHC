package fr.rammex.simpleuhc.events;

import api.rammex.gameapi.option.Option;
import api.rammex.gameapi.option.OptionType;
import com.avaje.ebeaninternal.server.type.ScalarTypeScalaDouble;
import fr.rammex.simpleuhc.option.OptionSetup;
import fr.rammex.simpleuhc.utils.LangMessages;
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
        if (event.getRawSlot() != 2) return; // Slot de sortie

        AnvilInventory anvil = (AnvilInventory) event.getInventory();
        ItemStack result = anvil.getItem(2);
        if (result == null || !result.hasItemMeta() || !result.getItemMeta().hasEnchants()) return;

        ItemStack left = anvil.getItem(0);
        if (left == null) return;

        Material mat = left.getType();
        Option option = null;
        if (mat.name().startsWith("IRON_")) {
            option = OptionSetup.getOption("Iron Alowed Enchants");
        } else if (mat.name().startsWith("DIAMOND_")) {
            option = OptionSetup.getOption("Diamond Alowed Enchants");
        }
        if (option == null || option.getType() != OptionType.ENCHANT) return;

        Map<String, Boolean> allowed = (Map<String, Boolean>) option.getValue();

        for (Map.Entry<Enchantment, Integer> entry : result.getItemMeta().getEnchants().entrySet()) {
            String key = entry.getKey().getName() + "_" + entry.getValue();
            if (!allowed.getOrDefault(key, false)) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage(LangMessages.getMessage("events.enchant.cant_use_that_enchant", null));
                return;
            }
        }
    }
}