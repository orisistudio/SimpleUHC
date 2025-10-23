package fr.rammex.simpleuhc.events;

import api.rammex.gameapi.option.Option;
import api.rammex.gameapi.option.OptionType;
import fr.rammex.simpleuhc.option.OptionSetup;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
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
}