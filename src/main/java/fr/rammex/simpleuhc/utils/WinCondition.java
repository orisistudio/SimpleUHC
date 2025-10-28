package fr.rammex.simpleuhc.utils;

import fr.rammex.simpleuhc.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WinCondition {

    static TeamManager teamManager = new TeamManager();

    public static Boolean isWinConditionMetNoTeams(){
        int alivePlayers = 0;
        for(Player p : Bukkit.getOnlinePlayers()){
            if(!p.getGameMode().equals(GameMode.SPECTATOR) && !p.getGameMode().equals(GameMode.CREATIVE)){
                alivePlayers++;
            }
        }

        if (alivePlayers-1 <= 1){
            return true;
        }

        return false;
    }

    public static Object isWinConditionMetTeams(){
        int teamAlive = 0;
        List<String> teams = new ArrayList<>();
        int playerAliveWithoutTeam = 0;
        for(Player p : Bukkit.getOnlinePlayers()){
            if(!p.getGameMode().equals(GameMode.SPECTATOR) && !p.getGameMode().equals(GameMode.CREATIVE)){
                if(teamManager.isPlayerInAnyTeam(p)){
                    String teamName = teamManager.getPlayerTeamName(p);
                    if(!teams.contains(teamName)){
                        teams.add(teamName);
                        teamAlive++;
                    }
                } else {
                    playerAliveWithoutTeam++;
                }
            }
        }

        if (teamAlive-1 <= 1 && playerAliveWithoutTeam-1 == 0){
            if(teamAlive-1 == 1){
                return teams.get(0);
            } else {
                return true;
            }
        } else if (teamAlive-1 == 0 && playerAliveWithoutTeam-1 == 1){
            return true;
        }

        return false;
    }
}
