package fr.rammex.simpleuhc.events;

import api.rammex.gameapi.option.Option;
import api.rammex.gameapi.option.OptionType;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EnchantListener implements Listener {

    private static final List<Material> ARMOR_MATERIALS = Arrays.asList(
            Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
            Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
            Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS,
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
            Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS
    );

    private static final List<Material> WEAPON_MATERIALS = Arrays.asList(
            Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD,
            Material.BOW
    );

    private static final List<Material> TOOL_MATERIALS = Arrays.asList(
            Material.WOOD_PICKAXE, Material.WOOD_AXE, Material.WOOD_SPADE, Material.WOOD_HOE,
            Material.STONE_PICKAXE, Material.STONE_AXE, Material.STONE_SPADE, Material.STONE_HOE,
            Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SPADE, Material.IRON_HOE,
            Material.GOLD_PICKAXE, Material.GOLD_AXE, Material.GOLD_SPADE, Material.GOLD_HOE,
            Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SPADE, Material.DIAMOND_HOE,
            Material.SHEARS, Material.FLINT_AND_STEEL, Material.FISHING_ROD
    );

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        ItemStack item = event.getItem();
        Option option = getEnchantOption(item.getType());

        if (option != null && option.getType() == OptionType.ENCHANT) {
            Map<String, Boolean> allowed = (Map<String, Boolean>) option.getValue();

            event.getEnchantsToAdd().entrySet().removeIf(e -> {
                String key = e.getKey().getName() + "_" + e.getValue();
                return !allowed.getOrDefault(key, false);
            });
        }
    }

    @EventHandler
    public void onAnvilClick(InventoryClickEvent event) {
        if (event.getInventory().getType() != InventoryType.ANVIL) return;
        if (event.getRawSlot() != 2) return;

        AnvilInventory anvil = (AnvilInventory) event.getInventory();
        ItemStack result = anvil.getItem(2);
        if (result == null || !result.hasItemMeta() || !result.getItemMeta().hasEnchants()) return;

        ItemStack left = anvil.getItem(0);
        if (left == null) return;

        Option option = getEnchantOption(left.getType());
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

    private Option getEnchantOption(Material material) {
        String materialName = material.name();

        if (materialName.startsWith("IRON_") && isArmor(material)) {
            return OptionSetup.getOption("Iron Alowed Enchants");
        } else if (materialName.startsWith("DIAMOND_") && isArmor(material)) {
            return OptionSetup.getOption("Diamond Alowed Enchants");
        }

        if (isWeapon(material)) {
            return OptionSetup.getOption("Weapons Alowed Enchants");
        }

        if (isTool(material)) {
            return OptionSetup.getOption("Tools Alowed Enchants");
        }

        return null;
    }

    private boolean isArmor(Material material) {
        return ARMOR_MATERIALS.contains(material);
    }

    private boolean isWeapon(Material material) {
        return WEAPON_MATERIALS.contains(material);
    }

    private boolean isTool(Material material) {
        return TOOL_MATERIALS.contains(material);
    }
}