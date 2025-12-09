package fr.rammex.simpleuhc.team;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;


public class TeamChatManager {
    private static final Map<Player, Boolean> teamChatMode = new HashMap<>();

    public static void toggleTeamChat(Player player) {
        boolean currentState = isInTeamChatMode(player);
        teamChatMode.put(player, !currentState);
    }

    public static boolean isInTeamChatMode(Player player) {
        return teamChatMode.getOrDefault(player, false);
    }

    public static void enableTeamChat(Player player) {
        teamChatMode.put(player, true);
    }

    public static void disableTeamChat(Player player) {
        teamChatMode.put(player, false);
    }

    public static void cleanup(Player player) {
        teamChatMode.remove(player);
    }
}
