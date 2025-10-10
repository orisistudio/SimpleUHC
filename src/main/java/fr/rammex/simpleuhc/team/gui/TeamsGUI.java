package fr.rammex.simpleuhc.team.gui;

import com.itsmavey.GUIToolkit.Elements.Buttons.Button;
import com.itsmavey.GUIToolkit.Elements.GuiElement;
import com.itsmavey.GUIToolkit.Elements.Icon;
import com.itsmavey.GUIToolkit.Pane.Pane;
import com.itsmavey.GUIToolkit.Pane.Types;
import fr.rammex.simpleuhc.team.TeamColor;
import fr.rammex.simpleuhc.team.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeamsGUI {
    static TeamManager teamManager = new TeamManager();

    public static void setupGUI(Player player) {
        Pane pane = new Pane(Types.DOUBLE_CHEST, "§6§Teams");
        List<GuiElement> elements = getTeamsButtons(pane);
        for (GuiElement element : elements) {
            pane.addElement(element);
        }

        pane.show(player);
    }

    private static Map<Map<TeamColor,String>, List<Player>> getTeams() {
        return teamManager.getTeams();
    }

    private static List<GuiElement> getTeamsButtons(Pane pane) {
        List<GuiElement> buttons = new ArrayList<>();
        int x = 0;
        int y = 0;

        for (Map.Entry<Map<TeamColor, String>, List<Player>> teamEntry : getTeams().entrySet()) {
            Map<TeamColor, String> teamInfo = teamEntry.getKey();
            List<Player> players = teamEntry.getValue();

            TeamColor color = teamInfo.keySet().iterator().next();
            String teamName = teamInfo.get(color);

            List<String> lore = new ArrayList<>();
            lore.add("§7Membres de l'équipe:");
            for (Player member : players) {
                lore.add("§a- "+member.getName());
            }

            lore.add("\n\n§e§nClic gauche§r §apour rejoindre l'équipe");
            lore.add("\n\n§e§nClic droit§r §cpour quitter l'équipe");


            ItemStack iconItem = Icon.getHead(color.getHeadBase64());

            Icon icon = new Icon(teamName, iconItem, lore);

            Button button = new Button(x,y,icon, ((player, clickType) -> {
                // handle le clique gauche pour rejoindre l'équipe si il est invité
                if(clickType.isLeftClick()) {
                    if(!teamManager.isPlayerInAnyTeam(player) && teamManager.isPlayerInvitedToTeam(teamName, player)) {
                        try {
                            teamManager.acceptTeamInvite(player, teamName);
                            player.sendMessage("§aVous avez rejoint l'équipe §6"+teamName+"§a.");
                            pane.refresh();
                        } catch (IllegalArgumentException e) {
                            player.sendMessage("§cErreur: "+e.getMessage());
                        }
                    }else {
                        player.sendMessage("§cVous n'êtes pas invité dans cette équipe.");
                    }
                } // handle le clique droit pour quitter l'équipe si il en fait partie
                else if (clickType.isRightClick()){
                    if(teamManager.isPlayerInTeam(teamName, player)) {
                        try {
                            teamManager.removePlayerFromTeam(teamName, player);
                            player.sendMessage("§cVous avez quitté l'équipe §6"+teamName+"§c.");
                            pane.refresh();
                        } catch (IllegalArgumentException e) {
                            player.sendMessage("§cErreur: "+e.getMessage());
                        }
                    }else {
                        player.sendMessage("§cVous n'êtes pas dans cette équipe.");
                    }
                }
            }));

            buttons.add(button);
            // changement de ligne si on atteint la fin de la ligne sinon on incrémente la colonne
            if ( x+1 != 3 & y != 5 || x+1 != 5 & y != 5 ){ // emplacement pour création et delete d'équipe
                x++;
                if (x >= 9) {
                    x = 0;
                    y++;
                }
            } else {
                x = x +2;
            }
        }

        buttons.add(new TeamsGUI().getCreateTeamButton());
        buttons.add(new TeamsGUI().getDeleteTeamButton());

        return buttons;
    }

    private Button getCreateTeamButton() {
        List<String> lore = new ArrayList<>();
        lore.add("§e§nClic gauche§r §apour créer une équipe");
        ItemStack iconItem = Icon.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzkwZjYyZWM1ZmEyZTkzZTY3Y2YxZTAwZGI4YWY0YjQ3YWM3YWM3NjlhYTA5YTIwM2ExZjU3NWExMjcxMGIxMCJ9fX0=");
        Icon icon = new Icon("§aCréer une équipe", iconItem, lore);

        return new Button(3,5,icon, ( (player, clickType) -> {
            // handle le clique gauche pour créer une équipe
            if(clickType.isLeftClick()) {
                if(!teamManager.isPlayerInAnyTeam(player)) {


                } else {
                    player.sendMessage("§cVous êtes déjà dans une équipe.");
                }
            }
        }));
    }

    private Button getDeleteTeamButton() {
        List<String> lore = new ArrayList<>();
        lore.add("§e§nClic gauche§r §cpour supprimer votre équipe");
        ItemStack iconItem = Icon.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWRmNWMyZjg5M2JkM2Y4OWNhNDA3MDNkZWQzZTQyZGQwZmJkYmE2ZjY3NjhjODc4OWFmZGZmMWZhNzhiZjYifX19");
        Icon icon = new Icon("§cSupprimer votre équipe", iconItem, lore);

        return new Button(5,5,icon, ( (player, clickType) -> {
            // handle le clique gauche pour supprimer son équipe
            if(clickType.isLeftClick()) {
                if(teamManager.isPlayerInAnyTeam(player) && teamManager.isPlayerTeamLeader(player)) {
                    try {
                        String teamName = teamManager.getPlayerTeamName(player);
                        teamManager.disbandTeam(teamName);
                        player.sendMessage("§cVous avez supprimé l'équipe §6"+teamName+"§c.");
                    } catch (IllegalArgumentException e) {
                        player.sendMessage("§cErreur: "+e.getMessage());
                    }
                } else {
                    player.sendMessage("§cVous n'êtes pas le leader de votre équipe.");
                }
            }
        }));
    }




}
