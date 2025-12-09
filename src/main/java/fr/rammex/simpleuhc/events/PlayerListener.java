package fr.rammex.simpleuhc.events;

import fr.rammex.simpleuhc.SimpleUHC;
import fr.rammex.simpleuhc.option.OptionSetup;
import fr.rammex.simpleuhc.game.SimpleUHCManager;
import fr.rammex.simpleuhc.team.TeamManager;
import fr.rammex.simpleuhc.utils.LangMessages;
import fr.rammex.simpleuhc.utils.WinCondition;
import fr.rammex.simpleuhc.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {

    TeamManager teamManager = new TeamManager();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        if(SimpleUHCManager.isGameRunning){
            Player player = e.getPlayer();
            e.setJoinMessage("");

            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(LangMessages.getMessage("events.player.game_already_started", null));
        } else {
            int minPlayer = (int) OptionSetup.getOption("Player Min").getValue();
            int maxPlayer = (int) OptionSetup.getOption("Player Max").getValue();

            if (Bukkit.getOnlinePlayers().size() > maxPlayer) {
                e.getPlayer().kickPlayer(LangMessages.getMessage("events.player.game_player_max_reached", null));
                return;
            }

            if (Bukkit.getOnlinePlayers().size() == minPlayer) {
                SimpleUHC.getSimpleUHCManager().onEnable();
            }
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
        } else {
            int minPlayer = (int) OptionSetup.getOption("Player Min").getValue();
            if (Bukkit.getOnlinePlayers().size() - 1 < minPlayer && SimpleUHCManager.isGameRunning) {
                SimpleUHC.getSimpleUHCManager().onDisable();
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


    @EventHandler
    public void onDamageTakenGeneral(EntityDamageEvent e){
        if(SimpleUHCManager.isGameRunning){
            if(e.getEntity() instanceof Player){
                Player player = (Player) e.getEntity();
                Boolean damageEnabled = SimpleUHC.getSimpleUHCManager().isDamageEnabled();
                if(!damageEnabled){
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onDamageTaken(EntityDamageByEntityEvent e){
        if(SimpleUHCManager.isGameRunning){
            if(e.getEntity() instanceof Player){
                Player player = (Player) e.getEntity();

                if(e.getDamager() instanceof Player){
                    Player damager = (Player) e.getDamager();

                    boolean isPvpEnabled = SimpleUHC.getSimpleUHCManager().isPvpEnabled();

                    if(!isPvpEnabled){
                        e.getDamager().sendMessage(LangMessages.getMessage("events.player.pvp_not_active", null));
                        e.setCancelled(true);
                        return;
                    }

                    boolean isTeamModeActivated = (boolean) OptionSetup.getOption("Game Team").getValue();
                    if(isTeamModeActivated){
                        String playerTeam = teamManager.getPlayerTeamName(player);
                        String damagerTeam = teamManager.getPlayerTeamName(damager);
                        if(playerTeam != null && damagerTeam != null && playerTeam.equals(damagerTeam)){
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    public void playerDie(Player player){
        player.setGameMode(GameMode.SPECTATOR);

        List<Player> survivors = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
            if (p.getGameMode() == GameMode.SURVIVAL) {
                survivors.add(p);
            }
        }

        int playersAlive = survivors.size();
        String deathMsg = LangMessages.getMessage("events.player.player_dead", null)
                .replace("{player}", player.getName())
                .replace("{playersAlive}", String.valueOf(playersAlive));
        Bukkit.broadcastMessage(deathMsg);

        boolean isTeamModeActivated = (boolean) OptionSetup.getOption("Game Team").getValue();
        if(!isTeamModeActivated){
            if(playersAlive == 1){
                Player winner = survivors.get(0);
                Bukkit.broadcastMessage(LangMessages.getMessage("events.player.player_won", null).replace("{player}", winner.getName()));
                Location spawnLocation = WorldManager.getOriginalWorld().getSpawnLocation();
                winner.teleport(spawnLocation);
                SimpleUHC.getSimpleUHCManager().onDisable();
            }
        } else {
            Object winCondition = WinCondition.isWinConditionMetTeams();
            if(winCondition instanceof String){
                String winningTeam = (String) winCondition;
                Bukkit.broadcastMessage(LangMessages.getMessage("events.player.team_won", null).replace("{team}", winningTeam));
                for (Player p : Bukkit.getOnlinePlayers()){
                    if(teamManager.getPlayerTeamName(p) != null && teamManager.getPlayerTeamName(p).equals(winningTeam)){
                        p.getInventory().clear();
                        Location spawnLocation = WorldManager.getOriginalWorld().getSpawnLocation();
                        p.teleport(spawnLocation);
                    }
                }
                SimpleUHC.getSimpleUHCManager().onDisable();
            } else if(winCondition instanceof Boolean && (Boolean) winCondition){
                if(playersAlive == 1){
                    Player winner = survivors.get(0);
                    Bukkit.broadcastMessage(LangMessages.getMessage("events.player.player_won", null).replace("{player}", winner.getName()));
                    winner.getInventory().clear();
                    Location spawnLocation = WorldManager.getOriginalWorld().getSpawnLocation();
                    winner.teleport(spawnLocation);
                    SimpleUHC.getSimpleUHCManager().onDisable();
                }
            }
        }
    }
}