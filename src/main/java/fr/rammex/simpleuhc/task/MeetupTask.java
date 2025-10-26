package fr.rammex.simpleuhc.task;

import api.rammex.gameapi.task.AbstractTask;
import fr.rammex.simpleuhc.option.OptionSetup;
import fr.rammex.simpleuhc.world.WorldManager;
import org.bukkit.Bukkit;

public class MeetupTask extends AbstractTask {
    public MeetupTask(int meetupDelay) {
        super("MeetupTask", "Task for managing the meetup phase in SimpleUHC", meetupDelay * 60);
    }

    @Override
    public void onTick() {
    }

    @Override
    protected void onFinish() {
        setActualDuration(getDuration());
        WorldManager.shrinkWorldBorder(
                (int) OptionSetup.getOption("Game Meetup Radius").getValue(),
                (int) OptionSetup.getOption("Game Meetup Speed").getValue()*60);

        Bukkit.broadcastMessage("§c§lLe meetup a commencé ! Le monde rétrécit !");
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {

    }
}
