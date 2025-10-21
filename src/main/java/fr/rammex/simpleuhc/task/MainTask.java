package fr.rammex.simpleuhc.task;

import api.rammex.gameapi.task.AbstractTask;
import api.rammex.gameapi.uhc.UHCScoreBoard;
import fr.rammex.simpleuhc.SimpleUHC;
import fr.rammex.simpleuhc.game.SimpleUHCManager;

public class MainTask extends AbstractTask {

    public MainTask() {
        super("MainTask", "Main task for SimpleUHC", Integer.MAX_VALUE);
    }

    @Override
    protected void onStart() {
    }

    @Override
    protected void onTick() {
        UHCScoreBoard.refreshScoreBoard(SimpleUHC.getSimpleUHCManager().getName(), formatTime(getDuration() - getActualDuration()));
    }

    @Override
    protected void onFinish() {
        SimpleUHC.getSimpleUHCManager().onDisable();
    }

    @Override
    protected void onStop() {
        setActualDuration(0);
    }

    private static String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
