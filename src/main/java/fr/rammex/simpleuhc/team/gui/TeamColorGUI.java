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
import java.util.Arrays;
import java.util.List;

public class TeamColorGUI {

    public static void setupGUI(Player player) {
        Pane pane = new Pane(Types.CHEST, "§6§eChoisit une couleur");
        List<GuiElement> elements = getColorButtons(pane, player);
        for (GuiElement element : elements) {
            pane.addElement(element);
        }

        pane.show(player);
    }


    private static List<GuiElement> getColorButtons(Pane pane, Player playerB) {
        List<GuiElement> buttons = new ArrayList<>();

        List<TeamColor> colors;

        colors = Arrays.asList(TeamColor.values());
        int x = 0;
        int y = 0;

        for (TeamColor color : colors) {
            List<String> lore = new ArrayList<>();
            if (color == (TeamColor) TeamCreationGUI.getPlayerOption(playerB, "TeamColor")) {
                lore.add("§a§l✔ Couleur actuelle");
            } else {
                lore.add("§e§nClic gauche§r §cpour choisir cette couleur");
            }
            ItemStack iconItem = Icon.getHead(color.getHeadBase64());
            Icon icon = new Icon(color.getColorName(), iconItem, lore);

            Button button = new Button(x, y, icon, ((player, clickType) -> {
                TeamCreationGUI.setPlayerOption(player, "TeamColor", color);
                player.closeInventory();
                TeamCreationGUI.setupGUI(player);
            }));

            if (x == 8) {
                x = 0;
                y++;
            } else {
                x++;
            }

            buttons.add(button);
        }

        return buttons;
    }


}
