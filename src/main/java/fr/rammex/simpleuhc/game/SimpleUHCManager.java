package fr.rammex.simpleuhc.game;

import api.rammex.gameapi.GameAPI;
import api.rammex.gameapi.role.Role;
import api.rammex.gameapi.scenario.AbstractScenario;
import api.rammex.gameapi.task.AbstractTask;
import fr.rammex.simpleuhc.task.StartTask;
import fr.rammex.simpleuhc.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SimpleUHCManager extends AbstractScenario {
    private AbstractTask startTask;
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
                4);
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
        startTask = new StartTask(30);
        GameAPI.instance.getTaskManager().startTask(startTask);
    }

    @Override
    public void onDisable() {
        isGameRunning = false;
        pvpEnabled = false;

        GameAPI.instance.getTaskManager().stopTask(startTask);
        GameAPI.instance.getTaskManager().stopTask(StartTask.getMainTask());
        GameAPI.instance.getTaskManager().stopTask(StartTask.getMeetupTask());
        GameAPI.instance.getTaskManager().stopTask(StartTask.getPvpTask());
        GameAPI.instance.getTaskManager().stopTask(StartTask.getInvisibleTask());

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setGameMode(GameMode.SURVIVAL);
            p.teleport(WorldManager.getOriginalWorld().getSpawnLocation());
            p.sendMessage("§cLe SimpleUHC est terminé Merci d'y avoir joué!");
        }
        GameAPI.instance.getScenarioManager().unloadScenario(this);
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
}
