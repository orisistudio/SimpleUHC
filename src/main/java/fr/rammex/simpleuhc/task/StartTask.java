package fr.rammex.simpleuhc.task;

import api.rammex.gameapi.GameAPI;
import api.rammex.gameapi.task.AbstractTask;
import api.rammex.gameapi.uhc.UHCScoreBoard;
import fr.rammex.simpleuhc.SimpleUHC;
import fr.rammex.simpleuhc.game.SimpleUHCManager;
import fr.rammex.simpleuhc.option.OptionSetup;
import fr.rammex.simpleuhc.team.TeamManager;
import fr.rammex.simpleuhc.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class StartTask extends AbstractTask {
    private static AbstractTask mainTask = new MainTask();;
    private static AbstractTask meetupTask = new MeetupTask((int) OptionSetup.getOption("Game Meetup").getValue());
    private static AbstractTask pvpTask = new PvpTask((int) OptionSetup.getOption("Game PvP").getValue());
    private static AbstractTask invisibleTask = new InvisibleTask(10);

    public StartTask(int startDelay) {
        super("StartTask", "Start task for SimpleUHC", startDelay);
    }

    @Override
    protected void onStart() {
    }

    @Override
    protected void onTick() {
        int timeLeft = 30 - (getDuration() - getActualDuration());
        if (timeLeft <= 10 && timeLeft >= 4) {
            Bukkit.broadcastMessage("§a§lLe jeu commence dans §e§l" + timeLeft + "§a§l secondes !");
        } else if (timeLeft <= 3 && timeLeft > 0 ){
            String message = "§a§lLe jeu commence dans §e§l" + timeLeft + "§a§l seconde";
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
        else if (timeLeft == 1) {
            Bukkit.broadcastMessage("§a§lLe jeu commence maintenant !");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.EXPLODE, 1, 1);
            }
        } else if (timeLeft == 29) {
            Bukkit.broadcastMessage("§a§lLe jeu commence dans §e§l30§a§l secondes !");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
            }
        }
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

        GameAPI.instance.getTaskManager().addTask(invisibleTask);
        GameAPI.instance.getTaskManager().startTask(invisibleTask);

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

    public static AbstractTask getInvisibleTask() {
        return invisibleTask;
    }


}
