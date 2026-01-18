package fr.rammex.simpleuhc.game;

import api.rammex.gameapi.GameAPI;
import api.rammex.gameapi.game.GameState;
import api.rammex.gameapi.role.Role;
import api.rammex.gameapi.game.AbstractGame;
import api.rammex.gameapi.task.AbstractTask;
import fr.rammex.simpleuhc.phase.PhaseBuilder;
import fr.rammex.simpleuhc.phase.PhaseTask;
import fr.rammex.simpleuhc.utils.LangMessages;
import fr.rammex.simpleuhc.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SimpleUHCManager extends AbstractGame {
    private AbstractTask startTask;
    private PhaseTask phaseTask;
    private List<Role> roles = new ArrayList<>();
    public static boolean pvpEnabled = false;
    public static boolean damageEnabled = false;
    public static boolean isGameRunning = false;
    public SimpleUHCManager() {
        super("SimpleUHC",
                "L'UHC le plus simple qui soit !",
                ".rammex",
                "1.0",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2JiMzExZjNiYTFjMDdjM2QxMTQ3Y2QyMTBkODFmZTExZmQ4YWU5ZTNkYjIxMmEwZmE3NDg5NDZjMzYzMyJ9fX0",
                100,
                4,
                "SimpleUHC",
                GameState.INACTIVE);
    }

    private void initializePhases() {
        PhaseBuilder builder = new PhaseBuilder();

        // Récupérer les durées depuis les options
        int pvpDelayMinutes = (int) fr.rammex.simpleuhc.option.OptionSetup.getOption("Game PvP").getValue();
        int meetupDelayMinutes = (int) fr.rammex.simpleuhc.option.OptionSetup.getOption("Game Meetup").getValue();
        int meetupSpeedMinutes = (int) fr.rammex.simpleuhc.option.OptionSetup.getOption("Game Meetup Speed").getValue();
        int meetupRadius = (int) fr.rammex.simpleuhc.option.OptionSetup.getOption("Game Meetup Radius").getValue();

        // Calculer les durées en secondes
        int prepDuration = pvpDelayMinutes * 60;
        int pvpDuration = (meetupDelayMinutes - pvpDelayMinutes) * 60;
        int meetupDuration = meetupSpeedMinutes * 60;

        // Phase de préparation (dégâts et PVP désactivés)
        builder.prep(
            prepDuration,
            () -> {
                // Au démarrage de la préparation
                damageEnabled = false;
                pvpEnabled = false;
                isGameRunning = true;
                // Téléporter tous les joueurs
                if ((boolean) fr.rammex.simpleuhc.option.OptionSetup.getOption("Game Team").getValue()) {
                    WorldManager.teleportTeam();
                } else {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        WorldManager.teleportPlayer(p);
                    }
                }
                Bukkit.broadcastMessage(LangMessages.getMessage("game.preparation_phase_start", null));
            },
            (elapsed) -> {
                // Tick de la phase de préparation
                int remaining = prepDuration - elapsed;
                if (remaining > 0 && remaining % 60 == 0 && remaining <= 300) {
                    int minutes = remaining / 60;
                    Bukkit.broadcastMessage(LangMessages.getMessage("game.pvp_in_minutes", null)
                        .replace("{minutes}", String.valueOf(minutes)));
                }
            }
        );

        // Phase PVP (activer le PVP)
        builder.pvp(
            pvpDuration,
            () -> {
                // Au démarrage du PVP
                pvpEnabled = true;
                damageEnabled = true;
                Bukkit.broadcastMessage(LangMessages.getMessage("game.pvp_enabled", null));
            },
            () -> {
                // Fin de la phase PVP
                Bukkit.broadcastMessage(LangMessages.getMessage("game.pvp_phase_end", null));
            }
        );

        // Phase Meetup (rétrécir la bordure)
        builder.meetup(
            meetupDuration,
            () -> {
                // Au démarrage du meetup
                WorldManager.shrinkWorldBorder(meetupRadius, (long) meetupSpeedMinutes * 60);
                Bukkit.broadcastMessage(LangMessages.getMessage("game.meetup_start", null));
            },
            () -> {
                // Fin du meetup
                Bukkit.broadcastMessage(LangMessages.getMessage("game.meetup_end", null));
            }
        );

        phaseTask = builder.buildTask("UHC Game Phases");
        startTask = phaseTask;
    }

    @Override
    public boolean requiresRoles() {
        return false;
    }

    @Override
    public List<Role> getAvailableRoles() {
        return roles;
    }

    @Override
    public void onEnable() {
        WorldManager.createWorld();
        initializePhases();
        // Ne pas démarrer la tâche automatiquement
        // Elle sera démarrée après la génération du monde dans WorldManager.onDoneLoadingWorld()
    }

    public void startGame() {
        if (WorldManager.isWorldReady()) {
            GameAPI.instance.getTaskManager().startTask(startTask);
        } else {
            Bukkit.broadcastMessage(ChatColor.RED + "Impossible de démarrer le jeu : le monde n'est pas prêt !");
        }
    }

    @Override
    public void onDisable() {
        isGameRunning = false;
        pvpEnabled = false;
        damageEnabled = false;

        // Arrêter toutes les phases
        if (phaseTask != null) {
            GameAPI.instance.getTaskManager().stopTask(phaseTask);
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setGameMode(GameMode.SURVIVAL);
            p.teleport(WorldManager.getOriginalWorld().getSpawnLocation());
            p.sendMessage(LangMessages.getMessage("game.game_ended", null));
        }
        GameAPI.instance.getScenarioManager().unloadScenario(this);
    }

    @Override
    public void onGameStart() {

    }

    @Override
    public void onGameEnd() {

    }

    public Boolean isPvpEnabled() {
        return pvpEnabled;
    }

    public void setPvpEnabled(Boolean enabled) {
        pvpEnabled = enabled;
    }

    public Boolean isDamageEnabled() {
        return damageEnabled;
    }

    public void setDamageEnabled(Boolean enabled) {
        damageEnabled = enabled;
    }

    public PhaseTask getPhaseTask() {
        return phaseTask;
    }

    public AbstractTask getStartTask() {
        return startTask;
    }
}
