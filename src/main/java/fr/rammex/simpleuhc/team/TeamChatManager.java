package fr.rammex.simpleuhc.team;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * G\u00e8re l'\u00e9tat du chat d'\u00e9quipe pour chaque joueur
 */
public class TeamChatManager {
    // Map pour stocker si un joueur est en mode team chat
    private static final Map<Player, Boolean> teamChatMode = new HashMap<>();

    /**
     * Active ou d\u00e9sactive le mode team chat pour un joueur
     */
    public static void toggleTeamChat(Player player) {
        boolean currentState = isInTeamChatMode(player);
        teamChatMode.put(player, !currentState);
    }

    /**
     * V\u00e9rifie si un joueur est en mode team chat
     */
    public static boolean isInTeamChatMode(Player player) {
        return teamChatMode.getOrDefault(player, false);
    }

    /**
     * Active le mode team chat pour un joueur
     */
    public static void enableTeamChat(Player player) {
        teamChatMode.put(player, true);
    }

    /**
     * D\u00e9sactive le mode team chat pour un joueur
     */
    public static void disableTeamChat(Player player) {
        teamChatMode.put(player, false);
    }

    /**
     * Nettoie les donn\u00e9es d'un joueur qui se d\u00e9connecte
     */
    public static void cleanup(Player player) {
        teamChatMode.remove(player);
    }
}
