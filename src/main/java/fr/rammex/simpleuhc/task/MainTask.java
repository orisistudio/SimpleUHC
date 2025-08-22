package fr.rammex.simpleuhc.task;

import api.rammex.gameapi.task.AbstractTask;

public class MainTask extends AbstractTask {

    public MainTask() {
        super("MainTask", "Main task for SimpleUHC", Integer.MAX_VALUE);
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
    }

    @Override
    protected void onStop() {
        setActualDuration(0);
    }
}
