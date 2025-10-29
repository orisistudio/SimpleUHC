package fr.rammex.simpleuhc.task;

import api.rammex.gameapi.task.AbstractTask;
import fr.rammex.simpleuhc.SimpleUHC;
import fr.rammex.simpleuhc.game.SimpleUHCManager;
import fr.rammex.simpleuhc.utils.LangMessages;
import org.bukkit.Bukkit;

public class PvpTask extends AbstractTask {
    public PvpTask(int pvpDelay) {
        super("PvpTask", "Task for managing the pvp phase in SimpleUHC", pvpDelay * 60);
    }

    @Override
    public void onTick() {
    }

    @Override
    protected void onFinish() {
        SimpleUHC.getSimpleUHCManager().setPvpEnabled(true);
        Bukkit.broadcastMessage(LangMessages.getMessage("task.pvp.start", null));
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        setActualDuration(getDuration());
    }
}
