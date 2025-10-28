package fr.rammex.simpleuhc.task;

import api.rammex.gameapi.task.AbstractTask;
import fr.rammex.simpleuhc.SimpleUHC;
import org.bukkit.Bukkit;

public class InvisibleTask extends AbstractTask {
    public InvisibleTask(int gameInvisibility) {
        super("InvisibleTask", "phase d'invicibilité", 10);
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onTick() {

    }

    @Override
    protected void onFinish() {
        SimpleUHC.getSimpleUHCManager().setDamageEnabled(true);
        Bukkit.broadcastMessage("§c§lL'invicibilité est terminée ! Vous pouvez désormais mourir !");
    }

    @Override
    protected void onStop() {

    }
}
