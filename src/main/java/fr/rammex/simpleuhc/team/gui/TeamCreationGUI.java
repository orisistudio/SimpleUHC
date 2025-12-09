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
import fr.rammex.simpleuhc.utils.LangMessages;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamCreationGUI {

    private static Map<Map<String, Object>, Player> configuration = new HashMap<>(); // Map configuration des options en cours Map<String, String> pour Le Nom de L'option et sa value, et le player pour obtenir qui moifie

    public static void setupGUI(Player player) {
        Pane pane = new Pane(Types.CHEST, LangMessages.getMessage("gui.team_creation_gui.gui_name", null));
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

        lore.add(LangMessages.getMessage("gui.team_creation_gui.left_click_to_change_team_name", null));
        lore.add(LangMessages.getMessage("gui.team_creation_gui.actual_team_name", null).replace("{team_name}", teamName));
        ItemStack iconItem = Icon.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkyNmMxZjJjM2MxNGQwODZjNDBjZmMyMzVmZTkzODY5NGY0YTUxMDY3YWRhNDcyNmI0ODZlYTFjODdiMDNlMiJ9fX0=");
        Icon icon = new Icon(LangMessages.getMessage("gui.team_creation_gui.team_name", null), iconItem, lore);

        return new Button(2,1,icon, ( (player, clickType) -> {
            player.closeInventory();
            player.sendMessage(LangMessages.getMessage("gui.team_creation_gui.put_new_team_name_in_chat", null));
            ChangeValueListener.setWaitForValue(player, "TeamName", teamName);
        }));
    }

    private Button getTeamChangeColorButton(Player playerb) {
        List<String> lore = new ArrayList<>();

        TeamColor teamColor = (TeamColor) getPlayerOption(playerb,"TeamColor");

        lore.add(LangMessages.getMessage("gui.team_creation_gui.left_click_change_team_color", null));
        lore.add(LangMessages.getMessage("gui.team_creation_gui.actual_color", null).replace("{team_color}", teamColor.getColorName()));
        ItemStack iconItem = Icon.getHead(teamColor.getHeadBase64());
        Icon icon = new Icon(LangMessages.getMessage("gui.team_creation_gui.team_color", null), iconItem, lore);

        return new Button(6,1,icon, ( (player, clickType) -> {
            TeamColorGUI.setupGUI(player);
        }));
    }

    private Button getValidateCreationButton(Player playerb){
        List<String> lore = new ArrayList<>();
        lore.add(LangMessages.getMessage("gui.team_creation_gui.left_click_valid_team_creation", null));
        ItemStack iconItem = Icon.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzUyOTk1MjZiNGEzNTM5MmQ0YTQxYmNiNGJmMzJlMGRiMDRkMGYwMWQyNWNkYTEzNzE5OGEzNTcxOTY4NWY2In19fQ==");

        TeamColor teamColor = (TeamColor) getPlayerOption(playerb,"TeamColor");
        String teamName = (String) getPlayerOption(playerb,"TeamName");
        List<Player> players = new ArrayList<>();
        players.add(playerb);

        int teamSize = (int) OptionSetup.getOption("Game Team Size").getValue();

        Icon icon = new Icon(LangMessages.getMessage("gui.team_creation_gui.valid_team_creation", null), iconItem, lore);
        return new Button(3,2,icon, ( (player, clickType) -> {
            try{
                TeamManager.createTeam(teamColor, teamName, players, teamSize);
            } catch (IllegalArgumentException e){
                player.sendMessage("§c" + e.getMessage());
                return;
            }
            player.closeInventory();
        }));
    }

    private Button getCancelButton() {
        List<String> lore = new ArrayList<>();
        lore.add(LangMessages.getMessage("gui.team_creation_gui.left_click_stop_team_creation", null));
        ItemStack iconItem = Icon.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGZmOTY4YzkwNTIyMzQ5MWE0ZTM5MGM3ZDVmMTBjMTg0M2E2YmEwNDgyMGQ2YjE3ODRmNTJlNDEwZDExYWI1YiJ9fX0=");

        Icon icon = new Icon(LangMessages.getMessage("gui.team_creation_gui.stop_team_creation", null), iconItem, lore);
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