package fr.rammex.simpleuhc.utils;

import api.rammex.gameapi.team.Team;
import fr.rammex.simpleuhc.SimpleUHC;
import fr.rammex.simpleuhc.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WinCondition {

    public static Boolean isWinConditionMetNoTeams(){
        int alivePlayers = 0;
        for(Player p : Bukkit.getOnlinePlayers()){
            if(!p.getGameMode().equals(GameMode.SPECTATOR) && !p.getGameMode().equals(GameMode.CREATIVE)){
                alivePlayers++;
            }
        }

        if (alivePlayers <= 1){
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
                if(SimpleUHC.instance.getTeamManager().isPlayerInAnyTeam(p)){
                    TeamManager teamManager = SimpleUHC.instance.getTeamManager();
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

        if (teamAlive <= 1 && playerAliveWithoutTeam == 0){
            if(teamAlive == 1){
                return teams.get(0);
            } else {
                return true;
            }
        } else if (teamAlive == 0 && playerAliveWithoutTeam == 1){
            return true;
        }

        return false;
    }
}
