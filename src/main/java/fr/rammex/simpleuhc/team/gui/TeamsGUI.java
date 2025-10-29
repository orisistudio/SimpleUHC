package fr.rammex.simpleuhc.team.gui;

import com.itsmavey.GUIToolkit.Elements.Buttons.Button;
import com.itsmavey.GUIToolkit.Elements.GuiElement;
import com.itsmavey.GUIToolkit.Elements.Icon;
import com.itsmavey.GUIToolkit.Pane.Pane;
import com.itsmavey.GUIToolkit.Pane.Types;
import fr.rammex.simpleuhc.team.TeamColor;
import fr.rammex.simpleuhc.team.TeamManager;
import fr.rammex.simpleuhc.utils.LangMessages;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeamsGUI {
    private static TeamManager teamManager = new TeamManager();

    public static void setupGUI(Player player) {
        Pane pane = new Pane(Types.DOUBLE_CHEST, LangMessages.getMessage("gui.teams_gui.gui_name", null));
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
            lore.add(LangMessages.getMessage("gui.teams_gui.member_of_team", null));
            for (Player member : players) {
                lore.add("§a- "+member.getName());
            }

            lore.add(LangMessages.getMessage("gui.teams_gui.left_click_to_join", null));
            lore.add(LangMessages.getMessage("gui.teams_gui.right_click_to_leave", null));


            ItemStack iconItem = Icon.getHead(color.getHeadBase64());

            Icon icon = new Icon(teamName, iconItem, lore);

            Button button = new Button(x,y,icon, ((player, clickType) -> {
                // handle le clique gauche pour rejoindre l'équipe si il est invité
                if(clickType.isLeftClick()) {
                    if(!teamManager.isPlayerInAnyTeam(player) && teamManager.isPlayerInvitedToTeam(teamName, player)) {
                        try {
                            teamManager.acceptTeamInvite(player, teamName);
                            player.sendMessage(LangMessages.getMessage("gui.teams_gui.team_joined", null).replace("{teamName}", teamName));
                            pane.refresh();
                        } catch (IllegalArgumentException e) {
                            player.sendMessage("§cError: "+e.getMessage());
                        }
                    }else {
                        player.sendMessage(LangMessages.getMessage("gui.teams_gui.not_invited", null));
                    }
                } // handle le clique droit pour quitter l'équipe si il en fait partie
                else if (clickType.isRightClick()){
                    if(teamManager.isPlayerInTeam(teamName, player)) {
                        if(teamManager.isPlayerAloneInTeam(player)){
                            player.sendMessage(LangMessages.getMessage("gui.teams_gui.only_member", null));
                            return;
                        }
                        if(teamManager.isPlayerTeamLeader(player)){
                            player.sendMessage(LangMessages.getMessage("gui.teams_gui.leader_cant_leave", null));
                            return;
                        }
                        try {
                            teamManager.removePlayerFromTeam(teamName, player);
                            player.sendMessage(LangMessages.getMessage("gui.teams_gui.team_leaved", null).replace("{teamName}", teamName));
                            pane.refresh();
                        } catch (IllegalArgumentException e) {
                            player.sendMessage("§cError: "+e.getMessage());
                        }
                    }else {
                        player.sendMessage(LangMessages.getMessage("gui.teams_gui.not_in_team", null));
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
        buttons.add(new TeamsGUI().getDeleteTeamButton(pane));

        return buttons;
    }

    private Button getCreateTeamButton() {
        List<String> lore = new ArrayList<>();
        lore.add(LangMessages.getMessage("gui.teams_gui.left_click_to_create_team", null));
        ItemStack iconItem = Icon.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzkwZjYyZWM1ZmEyZTkzZTY3Y2YxZTAwZGI4YWY0YjQ3YWM3YWM3NjlhYTA5YTIwM2ExZjU3NWExMjcxMGIxMCJ9fX0=");
        Icon icon = new Icon(LangMessages.getMessage("gui.teams_gui.create_team", null), iconItem, lore);

        return new Button(3,5,icon, ( (player, clickType) -> {
            // handle le clique gauche pour créer une équipe
            if(clickType.isLeftClick()) {
                if(!teamManager.isPlayerInAnyTeam(player)) {
                    TeamCreationGUI.setupGUI(player);
                } else {
                    player.sendMessage(LangMessages.getMessage("gui.teams_gui.already_in_team", null));
                }
            }
        }));
    }

    private Button getDeleteTeamButton(Pane pane) {
        List<String> lore = new ArrayList<>();
        lore.add(LangMessages.getMessage("gui.teams_gui.left_click_delete_team", null));
        ItemStack iconItem = Icon.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWRmNWMyZjg5M2JkM2Y4OWNhNDA3MDNkZWQzZTQyZGQwZmJkYmE2ZjY3NjhjODc4OWFmZGZmMWZhNzhiZjYifX19");
        Icon icon = new Icon(LangMessages.getMessage("gui.teams_gui.delete_team", null), iconItem, lore);

        return new Button(5,5,icon, ( (player, clickType) -> {
            // handle le clique gauche pour supprimer son équipe
            if(clickType.isLeftClick()) {
                if(teamManager.isPlayerInAnyTeam(player) && teamManager.isPlayerTeamLeader(player)) {
                    try {
                        String teamName = teamManager.getPlayerTeamName(player);
                        teamManager.disbandTeam(teamName);
                        player.sendMessage(LangMessages.getMessage("gui.teams_gui.team_deleted", null).replace("{teamName}", teamName));
                        player.closeInventory();
                    } catch (IllegalArgumentException e) {
                        player.sendMessage("§cError: "+e.getMessage());
                    }
                } else {
                    player.sendMessage(LangMessages.getMessage("gui.teams_gui.not_team_leader", null));
                }
            }
        }));
    }




}
