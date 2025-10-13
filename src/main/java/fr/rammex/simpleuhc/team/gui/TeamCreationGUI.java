package fr.rammex.simpleuhc.team.gui;

import com.itsmavey.GUIToolkit.Elements.Buttons.Button;
import com.itsmavey.GUIToolkit.Elements.GuiElement;
import com.itsmavey.GUIToolkit.Elements.Icon;
import com.itsmavey.GUIToolkit.Pane.Pane;
import com.itsmavey.GUIToolkit.Pane.Types;
import fr.rammex.simpleuhc.option.OptionSetup;
import fr.rammex.simpleuhc.team.TeamColor;
import fr.rammex.simpleuhc.team.TeamManager;
import fr.rammex.simpleuhc.team.util.ChangeValueListener;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamCreationGUI {

    private static Map<Map<String, Object>, Player> configuration = new HashMap<>(); // Map configuration des options en cours Map<String, String> pour Le Nom de L'option et sa value, et le player pour obtenir qui moifie

    public static void setupGUI(Player player) {
        Pane pane = new Pane(Types.CHEST, "§6§eTeam Creation");
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
        buttons.add(new TeamCreationGUI().getValidateCreationButton(player));
        buttons.add(new TeamCreationGUI().getCancelButton());

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
            player.closeInventory();
            player.sendMessage("§e§lVeuillez entrer le nouveau nom de l'équipe dans le chat:");
            ChangeValueListener.setWaitForValue(player, "TeamName", teamName);
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

    private Button getValidateCreationButton(Player playerb){
        List<String> lore = new ArrayList<>();
        lore.add("§e§nClic gauche§r §cpour valider la création de l'équipe");
        ItemStack iconItem = Icon.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzUyOTk1MjZiNGEzNTM5MmQ0YTQxYmNiNGJmMzJlMGRiMDRkMGYwMWQyNWNkYTEzNzE5OGEzNTcxOTY4NWY2In19fQ==");

        TeamColor teamColor = (TeamColor) getPlayerOption(playerb,"TeamColor");
        String teamName = (String) getPlayerOption(playerb,"TeamName");
        List<Player> players = new ArrayList<>();
        players.add(playerb);

        int teamSize = (int) OptionSetup.getOption("Game Team Size").getValue();

        Icon icon = new Icon("§aValider la création", iconItem, lore);
        return new Button(3,2,icon, ( (player, clickType) -> {
            try{
                TeamManager.createTeam(teamColor, teamName, players, teamSize);
            } catch (IllegalArgumentException e){ // handle l'erreur si le nom est déjà utilisé ou invalide
                player.sendMessage("§cErreur: "+e.getMessage());
                return;
            }
            player.closeInventory();
        }));
    }

    private Button getCancelButton() {
        List<String> lore = new ArrayList<>();
        lore.add("§e§nClic gauche§r §cpour annuler la création de l'équipe");
        ItemStack iconItem = Icon.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGZmOTY4YzkwNTIyMzQ5MWE0ZTM5MGM3ZDVmMTBjMTg0M2E2YmEwNDgyMGQ2YjE3ODRmNTJlNDEwZDExYWI1YiJ9fX0=");

        Icon icon = new Icon("§cAnnuler", iconItem, lore);
        return new Button(5, 2, icon, ((player, clickType) -> {
            player.closeInventory();
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


