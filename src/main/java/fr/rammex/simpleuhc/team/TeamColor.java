package fr.rammex.simpleuhc.team;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public enum TeamColor {
    RED(Material.RED_ROSE, "Rouge");


    private final Material material;
    private final String color;

    private static final Map<Material, TeamColor> NAME_MAP = new HashMap<>();
    static {
        for (TeamColor color : TeamColor.values()) {
            NAME_MAP.put(color.getColor(), color);
        }
    }

    TeamColor(Material material, String color) {
        this.material = material;
        this.color = color;
    }

    public Material getColor() {
        return material;
    }
}
