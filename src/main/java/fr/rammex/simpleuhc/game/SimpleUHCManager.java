package fr.rammex.simpleuhc.game;

import api.rammex.gameapi.GameAPI;
import api.rammex.gameapi.role.Role;
import api.rammex.gameapi.scenario.AbstractScenario;
import api.rammex.gameapi.task.AbstractTask;
import fr.rammex.simpleuhc.task.StartTask;
import fr.rammex.simpleuhc.world.WorldManager;

import java.util.ArrayList;
import java.util.List;

public class SimpleUHCManager extends AbstractScenario {
    private AbstractTask startTask = new StartTask(30);
    private List<Role> roles = new ArrayList<>();
    public static boolean pvpEnabled = false;
    public static boolean isGameRunning = false;
    public SimpleUHCManager() {
        super("SimpleUHC", "L'UHC le plus simple qui soit !", ".rammex", "1.0", "", 100, 4);
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
        isGameRunning = true;
        WorldManager.createWorld();

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
    }
}
