package fr.rammex.simpleuhc.team;

import fr.rammex.simpleuhc.option.OptionSetup;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamManager {
    private boolean teamActivated = (boolean) OptionSetup.getOption("Game Team").getValue();
    private int teamSize = (int) OptionSetup.getOption("Game Team Size").getValue();
    private Map<List<String>, List<Player>> teams = new HashMap<>(); // Map première list pour Nom de la team et la couleur et la deuxième liste pour les joueurs dans la team


    public boolean isTeamActivated() {
        return teamActivated;
    }

    public int getTeamSize() {
        return teamSize;
    }


}
