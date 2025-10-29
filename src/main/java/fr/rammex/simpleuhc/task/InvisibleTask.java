package fr.rammex.simpleuhc.task;

import api.rammex.gameapi.task.AbstractTask;
import fr.rammex.simpleuhc.SimpleUHC;
import fr.rammex.simpleuhc.utils.LangMessages;
import org.bukkit.Bukkit;

public class InvisibleTask extends AbstractTask {
    public InvisibleTask(int gameInvisibility) {
        super("InvisibleTask", "phase d'invicibilité", 10);
    }

    @Override
    protected void onStart() {
        Bukkit.broadcastMessage(LangMessages.getMessage("task.invincible.start", null));
    }

    @Override
    protected void onTick() {

    }

    @Override
    protected void onFinish() {
        SimpleUHC.getSimpleUHCManager().setDamageEnabled(true);
        Bukkit.broadcastMessage(LangMessages.getMessage("task.invincible.stop", null));
    }

    @Override
    protected void onStop() {

    }
}