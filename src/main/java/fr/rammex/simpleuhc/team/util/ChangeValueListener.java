package fr.rammex.simpleuhc.team.util;

import fr.rammex.simpleuhc.team.gui.TeamCreationGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class ChangeValueListener implements Listener {
    private static Map<Map<Player, Boolean>, Map<String,String>> waitForValue = new HashMap<>();

    public static void setWaitForValue(Player player, String optionName, String currentValue) {
        Map<Player, Boolean> playerMap = new HashMap<>();
        playerMap.put(player, true);
        Map<String, String> optionMap = new HashMap<>();
        optionMap.put(optionName, currentValue);

        waitForValue.put(playerMap, optionMap);
    }

    public static void removeWaitForValue(Player player) {
        waitForValue.keySet().removeIf(map -> map.containsKey(player));
    }

    public static boolean isWaitingForValue(Player player) {
        for (Map<Player, Boolean> map : waitForValue.keySet()) {
            if (map.containsKey(player)) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(isWaitingForValue(player)) {
            event.setCancelled(true);
            String message = event.getMessage();
            for (Map<Player, Boolean> playerMap : waitForValue.keySet()) {
                if (playerMap.containsKey(player)) {
                    String newValue = message;

                    String optionName = waitForValue.get(playerMap).keySet().iterator().next();

                    TeamCreationGUI.setPlayerOption(player, optionName, newValue);
                    TeamCreationGUI.setupGUI(player);

                    removeWaitForValue(player);
                    break;
                }
            }
        }
    }
}
