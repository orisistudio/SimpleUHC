package fr.rammex.simpleuhc.game;

import api.rammex.gameapi.GameAPI;
import api.rammex.gameapi.role.Role;
import api.rammex.gameapi.scenario.AbstractScenario;
import api.rammex.gameapi.task.AbstractTask;
import fr.rammex.simpleuhc.option.OptionSetup;
import fr.rammex.simpleuhc.task.MainTask;
import fr.rammex.simpleuhc.task.MeetupTask;
import fr.rammex.simpleuhc.task.PvpTask;
import fr.rammex.simpleuhc.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SimpleUHCManager extends AbstractScenario {
    private AbstractTask mainTask = new MainTask();;
    private AbstractTask meetupTask = new MeetupTask((int) OptionSetup.getOption("Game Meetup").getValue());;
    private AbstractTask pvpTask = new PvpTask((int) OptionSetup.getOption("Game PvP").getValue());;
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
        return Collections.emptyList();
    }

    @Override
    public void onEnable() {
        isGameRunning = true;
        WorldManager.createWorld();

        GameAPI.instance.getTaskManager().addTask(mainTask);
        GameAPI.instance.getTaskManager().startTask(mainTask);

        GameAPI.instance.getTaskManager().addTask(meetupTask);
        GameAPI.instance.getTaskManager().startTask(meetupTask);

        GameAPI.instance.getTaskManager().addTask(pvpTask);
        GameAPI.instance.getTaskManager().startTask(pvpTask);

        for(Player p : Bukkit.getOnlinePlayers()) {
            WorldManager.teleportPlayer(p);
            p.sendMessage("§eL'UHC a commencé ! Préparez-vous !");
        }

    }

    @Override
    public void onDisable() {
        isGameRunning = false;
        pvpEnabled = false;

        GameAPI.instance.getTaskManager().stopTask(mainTask);
        GameAPI.instance.getTaskManager().stopTask(meetupTask);
        GameAPI.instance.getTaskManager().stopTask(pvpTask);
    }
}
