package fr.rammex.simpleuhc.phase;

import api.rammex.gameapi.game.Phase;
import api.rammex.gameapi.task.AbstractTask;
import java.util.List;

/**
 * Composite task that executes phases sequentially.
 */
public class PhaseTask extends AbstractTask {
    private final List<Phase> phases;
    private int currentIndex = 0;
    private int[] prefixSums;
    private int lastTickElapsed = -1; // Pour éviter de traiter le même tick plusieurs fois

    public PhaseTask(String name, String description, int duration, List<Phase> phases) {
        super(name, description, Math.max(1, duration));
        this.phases = phases;
        buildPrefixSums();
    }

    private void buildPrefixSums() {
        prefixSums = new int[phases.size() + 1];
        for (int i = 0; i < phases.size(); i++) {
            prefixSums[i + 1] = prefixSums[i] + phases.get(i).getDurationSeconds();
        }
    }

    @Override
    protected void onStart() {
        if (!phases.isEmpty()) {
            currentIndex = 0;
            lastTickElapsed = -1;
            phases.get(0).onStart();
        }
    }

    @Override
    protected void onTick() {
        if (phases.isEmpty()) return;

        int totalElapsed = getActualDuration();

        // Éviter de traiter le même tick plusieurs fois
        if (totalElapsed == lastTickElapsed) return;
        lastTickElapsed = totalElapsed;

        // Vérifier si on doit changer de phase
        while (currentIndex < phases.size() && totalElapsed >= prefixSums[currentIndex + 1]) {
            // La phase courante est terminée
            try {
                phases.get(currentIndex).onFinish();
            } catch (Exception ignored) {}

            currentIndex++;

            if (currentIndex < phases.size()) {
                try {
                    phases.get(currentIndex).onStart();
                } catch (Exception ignored) {}
            } else {
                // Plus de phases, marquer comme terminé
                setActualDuration(getDuration());
                return;
            }
        }

        // Exécuter le tick de la phase courante
        if (currentIndex < phases.size()) {
            int phaseStart = prefixSums[currentIndex];
            int elapsedInPhase = totalElapsed - phaseStart;

            try {
                phases.get(currentIndex).onTick(elapsedInPhase);
            } catch (Exception ignored) {}
        }
    }

    @Override
    protected void onFinish() {
        if (currentIndex < phases.size()) {
            try {
                phases.get(currentIndex).onFinish();
            } catch (Exception ignored) {}
        }
    }

    @Override
    protected void onStop() {
        setActualDuration(0);
        currentIndex = 0;
        lastTickElapsed = -1;
    }

    public synchronized void stopPhase(int index) {
        if (index < 0 || index >= phases.size()) return;

        if (index == currentIndex) {
            try {
                phases.get(currentIndex).onFinish();
            } catch (Exception ignored) {}

            currentIndex++;

            if (currentIndex < phases.size()) {
                try {
                    phases.get(currentIndex).onStart();
                } catch (Exception ignored) {}
            } else {
                setActualDuration(getDuration());
            }
            buildPrefixSums();
            return;
        }

        if (index > currentIndex) {
            phases.remove(index);
            buildPrefixSums();

            if (currentIndex >= phases.size()) {
                setActualDuration(getDuration());
            }
        }
    }

    public synchronized void stopPhase(String name) {
        if (name == null) return;
        for (int i = 0; i < phases.size(); i++) {
            if (name.equals(phases.get(i).getName())) {
                stopPhase(i);
                return;
            }
        }
    }

    public synchronized void stopAllPhases() {
        for (int i = currentIndex; i < phases.size(); i++) {
            try {
                phases.get(i).onFinish();
            } catch (Exception ignored) {}
        }
        currentIndex = phases.size();
        setActualDuration(getDuration());
        buildPrefixSums();
    }
}