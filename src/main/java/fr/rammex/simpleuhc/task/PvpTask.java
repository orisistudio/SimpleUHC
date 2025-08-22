package fr.rammex.simpleuhc.task;

import api.rammex.gameapi.task.AbstractTask;

public class PvpTask extends AbstractTask {
    public PvpTask(int pvpDelay) {
        super("PvpTask", "Task for managing the pvp phase in SimpleUHC", pvpDelay * 60);
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
    }
}
