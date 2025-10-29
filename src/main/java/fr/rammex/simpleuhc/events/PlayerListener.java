package fr.rammex.simpleuhc.events;

import fr.rammex.simpleuhc.SimpleUHC;
import fr.rammex.simpleuhc.option.OptionSetup;
import fr.rammex.simpleuhc.game.SimpleUHCManager;
import fr.rammex.simpleuhc.team.TeamManager;
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

// Listener principal pour gérer les événements liés aux joueurs dans l'UHC
public class PlayerListener implements Listener {

    TeamManager teamManager = new TeamManager();

    // Gère l'arrivée d'un joueur pendant une partie en cours
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        if(SimpleUHCManager.isGameRunning){
            Player player = e.getPlayer();
            e.setJoinMessage(""); // Pas de message de join

            // Met le joueur en mode spectateur et l'informe
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage("§c§lL'UHC est déjà en cours, vous êtes en mode spectateur.");
        } else {
            int minPlayer = (int) OptionSetup.getOption("Player Min").getValue();
            int maxPlayer = (int) OptionSetup.getOption("Player Max").getValue();

            if (Bukkit.getOnlinePlayers().size() > maxPlayer) {
                e.getPlayer().kickPlayer("§cLe nombre maximum de joueurs est atteint. Vous ne pouvez pas rejoindre la partie.");
                return;
            }

            if (Bukkit.getOnlinePlayers().size() == minPlayer) {
                SimpleUHC.getSimpleUHCManager().onEnable();
            }
        }
    }

    // Gère le départ d'un joueur pendant une partie en cours
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        if(SimpleUHCManager.isGameRunning){
            e.setQuitMessage(""); // Pas de message de quit
            Player player = e.getPlayer();
            // Si le joueur n'est pas spectateur ou créatif, il est considéré comme mort
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

    // Gère les dégâts infligés par une entité à un joueur pendant la partie (PVP)
    @EventHandler
    public void playerGetDamagedByEntity(EntityDamageByEntityEvent e){
        if(SimpleUHCManager.isGameRunning && SimpleUHCManager.pvpEnabled){
            if(e.getEntity() instanceof Player){
                Player p = (Player) e.getEntity();
                playerDie(p); // Le joueur meurt
                p.setGameMode(GameMode.SPECTATOR); // Passe en spectateur
            }
        }
    }

    // Gère la mort d'un joueur (message et passage en spectateur)
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        if(SimpleUHCManager.isGameRunning){
            e.setDeathMessage(""); // Pas de message de mort
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
                        e.getDamager().sendMessage("§cLe PVP n'est pas encore activé !");
                        e.setCancelled(true);
                        return;
                    }

                    boolean isTeamModeActivated = (boolean) OptionSetup.getOption("Game Team").getValue();
                    if(isTeamModeActivated){
                        String playerTeam = teamManager.getPlayerTeamName(player);
                        String damagerTeam = teamManager.getPlayerTeamName(damager);
                        // Empêche les dégâts entre membres de la même équipe
                        if(playerTeam != null && damagerTeam != null && playerTeam.equals(damagerTeam)){
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    // Méthode utilitaire pour gérer la mort d'un joueur
    public void playerDie(Player player){
        // Met immédiatement le joueur en spectateur pour que le comptage soit correct
        player.setGameMode(GameMode.SPECTATOR);

        // Construire la liste des survivants (mode SURVIVAL)
        List<Player> survivors = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
            if (p.getGameMode() == GameMode.SURVIVAL) {
                survivors.add(p);
            }
        }

        int playersAlive = survivors.size();
        Bukkit.broadcastMessage("§c§l" + player.getName() + " est mort ! " + "§c§l Il reste " + playersAlive + " joueurs en vie.");

        boolean isTeamModeActivated = (boolean) OptionSetup.getOption("Game Team").getValue();
        // Si un seul joueur reste (hors mode équipe), il gagne la partie
        if(!isTeamModeActivated){
            if(playersAlive == 1){
                Player winner = survivors.get(0);
                Bukkit.broadcastMessage("§6§l" + winner.getName() + " a gagné la partie !");
                Location spawnLocation = WorldManager.getOriginalWorld().getSpawnLocation();
                winner.teleport(spawnLocation);
                SimpleUHC.getSimpleUHCManager().onDisable();
            }
        } else { // Vérifie la condition de victoire en mode équipe
            Object winCondition = WinCondition.isWinConditionMetTeams();
            if(winCondition instanceof String){ // Si une équipe a gagné
                String winningTeam = (String) winCondition;
                Bukkit.broadcastMessage("§6§lL'équipe " + winningTeam + " a gagné la partie !");
                for (Player p : Bukkit.getOnlinePlayers()){
                    if(teamManager.getPlayerTeamName(p) != null && teamManager.getPlayerTeamName(p).equals(winningTeam)){
                        p.getInventory().clear();
                        Location spawnLocation = WorldManager.getOriginalWorld().getSpawnLocation();
                        p.teleport(spawnLocation);
                    }
                }
                SimpleUHC.getSimpleUHCManager().onDisable();
            } else if(winCondition instanceof Boolean && (Boolean) winCondition){ // Si un joueur solo a gagné
                if(playersAlive == 1){
                    Player winner = survivors.get(0);
                    Bukkit.broadcastMessage("§6§lLe joueur " + winner.getName() + " a gagné la partie !");
                    winner.getInventory().clear();
                    Location spawnLocation = WorldManager.getOriginalWorld().getSpawnLocation();
                    winner.teleport(spawnLocation);
                    SimpleUHC.getSimpleUHCManager().onDisable();
                }
            }
        }
    }
}