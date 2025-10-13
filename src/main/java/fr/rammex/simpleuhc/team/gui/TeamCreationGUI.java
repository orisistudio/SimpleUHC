package fr.rammex.simpleuhc.team.gui;

import com.itsmavey.GUIToolkit.Elements.Buttons.Button;
import com.itsmavey.GUIToolkit.Elements.GuiElement;
import com.itsmavey.GUIToolkit.Elements.Icon;
import com.itsmavey.GUIToolkit.Pane.Pane;
import com.itsmavey.GUIToolkit.Pane.Types;
import fr.rammex.simpleuhc.team.TeamColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamCreationGUI {

    private static Map<Map<String, Object>, Player> configuration = new HashMap<>(); // Map configuration des options en cours Map<String, String> pour Le Nom de L'option et sa value, et le player pour obtenir qui moifie

    public static void setupGUI(Player player) {
        Pane pane = new Pane(Types.CHEST, "§6§Team Creation");
        addPlayerWithDefaults(player);
        List<GuiElement> elements = getCreationButtons(pane, player);
        for (GuiElement element : elements) {
            pane.addElement(element);
        }


        pane.show(player);
    }

    private static List<GuiElement> getCreationButtons(Pane pane, Player player) {
        List<GuiElement> buttons = new ArrayList<>();

        buttons.add(new TeamCreationGUI().getTeamChangeColorButton(player));
        buttons.add(new TeamCreationGUI().getTeamChangeNameButton(player));

        return buttons;
    }


    private Button getTeamChangeNameButton(Player playerb) {
        List<String> lore = new ArrayList<>();

        String teamName = (String) getPlayerOption(playerb,"TeamName");

        lore.add("§e§nClic gauche§r §cpour changer le nom de l'équipe");
        lore.add("\n\n§7Nom actuel: §a"+teamName);
        ItemStack iconItem = Icon.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkyNmMxZjJjM2MxNGQwODZjNDBjZmMyMzVmZTkzODY5NGY0YTUxMDY3YWRhNDcyNmI0ODZlYTFjODdiMDNlMiJ9fX0=");
        Icon icon = new Icon("§eNom de l'équipe", iconItem, lore);

        return new Button(2,1,icon, ( (player, clickType) -> {

        }));
    }

    private Button getTeamChangeColorButton(Player playerb) {
        List<String> lore = new ArrayList<>();

        TeamColor teamColor = (TeamColor) getPlayerOption(playerb,"TeamColor");

        lore.add("§e§nClic gauche§r §cpour changer la couleur de l'équipe");
        lore.add("\n\n§7Couleur actuelle: §a"+teamColor.getColorName());
        ItemStack iconItem = Icon.getHead(teamColor.getHeadBase64());
        Icon icon = new Icon("§eCouleur de l'équipe", iconItem, lore);

        return new Button(6,1,icon, ( (player, clickType) -> {
            TeamColorGUI.setupGUI(player);
        }));
    }

    public static void addPlayerWithDefaults(Player player) {
        boolean exists = configuration.values().contains(player);
        if (!exists) {
            Map<String, Object> defaults = new HashMap<>();
            defaults.put("TeamName", "Default");
            defaults.put("TeamColor", TeamColor.WHITE);

            configuration.put(defaults, player);
        }
    }

    public static Object getPlayerOption(Player player, String optionKey) {
        for (Map.Entry<Map<String, Object>, Player> entry : configuration.entrySet()) {
            if (entry.getValue().equals(player)) {
                return entry.getKey().get(optionKey);
            }
        }
        return null;
    }

    public static void setPlayerOption(Player player, String optionKey, Object optionValue) {
        for (Map.Entry<Map<String, Object>, Player> entry : configuration.entrySet()) {
            if (entry.getValue().equals(player)) {
                entry.getKey().put(optionKey, optionValue);
                return;
            }
        }
    }
}


