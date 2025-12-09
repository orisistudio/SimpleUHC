package fr.rammex.simpleuhc.phase;

import api.rammex.gameapi.game.Phase;
import api.rammex.gameapi.game.SimplePhase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * DSL fluide pour composer des phases.
 *
 * Exemples d'usage :
 * new PhaseBuilder()
 *    .lobby(60, () -> broadcast("Lobby start"))
 *    .prep(120, null, elapsed -> updateProgress(elapsed))
 *    .pvp(900, () -> enablePvp(), () -> broadcast("PVP started"))
 *    .meetup(300, () -> shrinkBorder(), null)
 *    .end(10, () -> announceWinner())
 *    .buildTask("GamePhases");
 */
public class PhaseBuilder {
    private final List<Phase> phases = new ArrayList<>();

    public PhaseBuilder addPhase(String name, int seconds, Runnable onStart, Consumer<Integer> onTick, Runnable onFinish) {
        phases.add(new SimplePhase(name, seconds, onStart, onTick, onFinish));
        return this;
    }

    public PhaseBuilder lobby(int seconds, Runnable onStart, Runnable onFinish) {
        return addPhase("Lobby", seconds, onStart, null, onFinish);
    }

    public PhaseBuilder prep(int seconds, Runnable onStart, Consumer<Integer> onTick) {
        return addPhase("Preparation", seconds, onStart, onTick, null);
    }

    public PhaseBuilder pvp(int seconds, Runnable onStart, Runnable onFinish) {
        return addPhase("PVP", seconds, onStart, null, onFinish);
    }

    public PhaseBuilder meetup(int seconds, Runnable onStart, Runnable onFinish) {
        return addPhase("Meetup", seconds, onStart, null, onFinish);
    }


    public PhaseTask buildTask(String taskName) {
        return new PhaseTask(taskName, "Composite phases", computeTotalDuration(), new ArrayList<>(phases));
    }

    private int computeTotalDuration() {
        return phases.stream().mapToInt(Phase::getDurationSeconds).sum();
    }
}