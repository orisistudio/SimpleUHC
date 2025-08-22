package fr.rammex.simpleuhc.task;

import api.rammex.gameapi.task.AbstractTask;
import fr.rammex.simpleuhc.option.OptionSetup;
import fr.rammex.simpleuhc.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MeetupTask extends AbstractTask {
    public MeetupTask(int meetupDelay) {
        super("MeetupTask", "Task for managing the meetup phase in SimpleUHC", meetupDelay * 60);
    }

    @Override
    public void onTick() {
        setActualDuration(getActualDuration() - 1);
        if (getActualDuration() <= 0) {
            stop();
        }
    }

    @Override
    protected void onFinish() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        setActualDuration(getDuration());
        WorldManager.setWorldBorder((int) OptionSetup.getOption("Game Meetup Radius").getValue());
        Bukkit.broadcastMessage("§c§lLe meetup a commencé ! Le monde rétrécit !");
        for(Player player : Bukkit.getOnlinePlayers()){
            WorldManager.teleportPlayer(player);
        }
    }
}
