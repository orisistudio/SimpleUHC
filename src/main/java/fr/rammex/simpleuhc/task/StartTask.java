package fr.rammex.simpleuhc.task;

import api.rammex.gameapi.GameAPI;
import api.rammex.gameapi.task.AbstractTask;
import fr.rammex.simpleuhc.game.SimpleUHCManager;
import fr.rammex.simpleuhc.option.OptionSetup;
import fr.rammex.simpleuhc.team.TeamManager;
import fr.rammex.simpleuhc.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class StartTask extends AbstractTask {
    private static AbstractTask mainTask = new MainTask();;
    private static AbstractTask meetupTask = new MeetupTask((int) OptionSetup.getOption("Game Meetup").getValue());
    private static AbstractTask pvpTask = new PvpTask((int) OptionSetup.getOption("Game PvP").getValue());

    public StartTask(int startDelay) {
        super("MainTask", "Main task for SimpleUHC", startDelay);
    }

    @Override
    protected void onStart() {
    }

    @Override
    protected void onTick() {
        setActualDuration(getActualDuration()+1);
    }

    @Override
    protected void onFinish() {
        SimpleUHCManager.isGameRunning = true;
        GameAPI.instance.getTaskManager().addTask(mainTask);
        GameAPI.instance.getTaskManager().startTask(mainTask);

        GameAPI.instance.getTaskManager().addTask(meetupTask);
        GameAPI.instance.getTaskManager().startTask(meetupTask);

        GameAPI.instance.getTaskManager().addTask(pvpTask);
        GameAPI.instance.getTaskManager().startTask(pvpTask);

        if(TeamManager.isTeamActivated()){
            WorldManager.teleportTeam();
        } else {
            for(Player player : Bukkit.getOnlinePlayers()){
                if(player.getGameMode() == GameMode.SURVIVAL){
                    WorldManager.teleportPlayer(player);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        setActualDuration(0);
    }

    public static AbstractTask getMainTask() {
        return mainTask;
    }

    public static AbstractTask getMeetupTask() {
        return meetupTask;
    }

    public static AbstractTask getPvpTask() {
        return pvpTask;
    }
}
