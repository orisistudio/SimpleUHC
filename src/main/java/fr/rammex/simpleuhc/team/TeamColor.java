package fr.rammex.simpleuhc.team;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public enum TeamColor {
    RED(new ItemStack(Material.WOOL, 1, (short) 14), "Rouge", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTVlZmZjZTEwOWRlNTBmNTg1M2IwYzMyZGNjMWEyOGNmNjJhNTA0YjYxMGU3MWUyMmYxZmRlNmZjMWE0YjkzZCJ9fX0="),
    BLUE(new ItemStack(Material.WOOL, 1, (short) 11), "Bleu", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjE0ZWMxNTU1ZDI5ZDEzMjhmY2YxOGQ2NGFjZDY0ZmYyMmZhMzFlZTkxMjg5NTc1YjNiYTcwMGI5NTBhNGJjZSJ9fX0="),
    GREEN(new ItemStack(Material.WOOL, 1, (short) 5), "Vert", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjQ5NjAxMTMwNzNiNTIwNDRkNzA3OTk2NTQxOTYyMTkyMjMxOGE5ZTk5ZDc2NzE3MGIxNDI4ZGVkNDhjN2NlNSJ9fX0="),
    YELLOW(new ItemStack(Material.WOOL, 1, (short) 4), "Jaune", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzlhMGU1MTMzZGZjZDM2YzA2ODgwZDk4MzhkZGQ2NzY4MTRiN2Q1YTcwNTBlMjQxYTc5NmZmMTFhOWU0YzY2In19fQ=="),
    PURPLE(new ItemStack(Material.WOOL, 1, (short) 10), "Violet", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjY3MTAzMjVhNzRkOTFmN2Y5MTUwZGQwY2VlNDlkMTE1ZTVmOGJhOWIzNjg4MzU2MTA4MDJmNGMyOTY2MTVlYyJ9fX0="),
    ORANGE(new ItemStack(Material.WOOL, 1, (short) 1), "Orange", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2VkZTBjMmFhNmE3ZDI3MzA3OGU4ZGNjZGVmYzlkZWE0NjVmN2UyYjQ5NTEyOWU2ZGEwNmYzNGE2MTMyOWU1MiJ9fX0="),
    WHITE(new ItemStack(Material.WOOL, 1, (short) 0), "Blanc", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODAwM2MyMDdjNDAzYWYwNjkzYTllMjI2MWU2ODFkNTBlYzU3Y2Y4MmJlMmY1ZDM4NmIwYjlkMjcwN2Y3MTIwOSJ9fX0="),
    BLACK(new ItemStack(Material.WOOL, 1, (short) 15), "Noir", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjZkZDg5MTlmZThmNzUwN2I0NjQxYmYzYWE3MmIwNTZlMDg1N2NjMjAyYThlNWViNjZjOWMyMWFhNzNjMzg3NiJ9fX0="),
    PINK(new ItemStack(Material.WOOL, 1, (short) 6), "Rose", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA5MTEyZTYzNGZlYTA1MjNiYWZlZjM1YzYyNjVhNzE1NmY5YzgwYjY2ZTFmNmY2YzJiYzFkNjY0MzRjMTAxMCJ9fX0="),
    CYAN(new ItemStack(Material.WOOL, 1, (short) 9), "Cyan", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjAyOTBlYWQ3YmIzY2MzZjhlMTkyNThlMDNjMDFiYWJhZmY1NTU0MGZiOTE0OWZkMGI1NTliMTZmYjJlMjJmIn19fQ==");


    private final ItemStack item;
    private final String color;
    private final String headBase64;

    private static final Map<ItemStack, TeamColor> NAME_MAP = new HashMap<>();
    static {
        for (TeamColor color : TeamColor.values()) {
            NAME_MAP.put(color.getColor(), color);
        }
    }

    TeamColor(ItemStack item, String color, String headBase64) {
        this.item = item;
        this.color = color;
        this.headBase64 = headBase64;
    }

    public ItemStack getColor() {
        return item;
    }

    public String getColorName() {
        return color;
    }

    public String getHeadBase64() {
        return headBase64;
    }
}
