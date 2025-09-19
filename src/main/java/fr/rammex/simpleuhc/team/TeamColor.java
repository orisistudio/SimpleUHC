package fr.rammex.simpleuhc.team;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public enum TeamColor {
    RED(new ItemStack(Material.WOOL, 1, (short) 14), "Rouge"),
    BLUE(new ItemStack(Material.WOOL, 1, (short) 11), "Bleu"),
    GREEN(new ItemStack(Material.WOOL, 1, (short) 5), "Vert"),
    YELLOW(new ItemStack(Material.WOOL, 1, (short) 4), "Jaune"),
    PURPLE(new ItemStack(Material.WOOL, 1, (short) 10), "Violet"),
    ORANGE(new ItemStack(Material.WOOL, 1, (short) 1), "Orange"),
    WHITE(new ItemStack(Material.WOOL, 1, (short) 0), "Blanc"),
    BLACK(new ItemStack(Material.WOOL, 1, (short) 15), "Noir"),
    PINK(new ItemStack(Material.WOOL, 1, (short) 6), "Rose"),
    CYAN(new ItemStack(Material.WOOL, 1, (short) 9), "Cyan");


    private final ItemStack item;
    private final String color;

    private static final Map<ItemStack, TeamColor> NAME_MAP = new HashMap<>();
    static {
        for (TeamColor color : TeamColor.values()) {
            NAME_MAP.put(color.getColor(), color);
        }
    }

    TeamColor(ItemStack item, String color) {
        this.item = item;
        this.color = color;
    }

    public ItemStack getColor() {
        return item;
    }
}
