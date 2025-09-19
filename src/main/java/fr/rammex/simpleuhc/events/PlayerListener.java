package fr.rammex.simpleuhc.events;

import fr.rammex.simpleuhc.SimpleUHC;
import fr.rammex.simpleuhc.option.OptionSetup;
import fr.rammex.simpleuhc.game.SimpleUHCManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        if(SimpleUHCManager.isGameRunning){
            Player player = e.getPlayer();
            e.setJoinMessage("");

            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage("§c§lL'UHC est déjà en cours, vous êtes en mode spectateur.");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        if(SimpleUHCManager.isGameRunning){
            e.setQuitMessage("");
            Player player = e.getPlayer();
            if(!player.getGameMode().equals(GameMode.SPECTATOR) || !player.getGameMode().equals(GameMode.CREATIVE)){
                playerDie(player);
            }
        }
    }

    @EventHandler
    public void playerGetDamagedByEntity(EntityDamageByEntityEvent e){
        if(SimpleUHCManager.isGameRunning && SimpleUHCManager.pvpEnabled){
            if(e.getEntity() instanceof Player){
                Player p = (Player) e.getEntity();
                playerDie(p);
                p.setGameMode(GameMode.SPECTATOR);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        if(SimpleUHCManager.isGameRunning){
            e.setDeathMessage("");
            Player player = e.getEntity();
            playerDie(player);
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

    public void playerDie(Player player){
        Bukkit.broadcastMessage("§c§l" + player.getName() + " est mort ! " + "§c§l Il reste " + (Bukkit.getOnlinePlayers().size() - 1) + " joueurs en vie.");
        boolean isTeamModeActivated = (boolean) OptionSetup.getOption("Game Team").getValue();
        if(Bukkit.getOnlinePlayers().size() - 1 <= 1 && !isTeamModeActivated){
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p != player){
                    Bukkit.broadcastMessage("§6§l" + p.getName() + " a gagné la partie !");
                    p.setGameMode(GameMode.CREATIVE);
                }
            }
            SimpleUHCManager.isGameRunning = false;
            SimpleUHC.getSimpleUHCManager().onDisable();
        }
    }
}
