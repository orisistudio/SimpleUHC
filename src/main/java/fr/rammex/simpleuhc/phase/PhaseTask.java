package fr.rammex.simpleuhc.phase;

import api.rammex.gameapi.game.Phase;
import api.rammex.gameapi.task.AbstractTask;

import java.util.List;

public class PhaseTask extends AbstractTask {
    private final List<Phase> phases;
    private int currentIndex = 0;
    private int[] prefixSums;

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
            phases.get(0).onStart();
        }
    }

    @Override
    protected void onTick() {
        if (phases.isEmpty()) return;

        int totalElapsed = getActualDuration();
        while (currentIndex < phases.size()) {
            int phaseStart = prefixSums[currentIndex];
            int phaseDuration = phases.get(currentIndex).getDurationSeconds();
            int elapsedInPhase = totalElapsed - phaseStart;

            if (elapsedInPhase >= 0 && elapsedInPhase < phaseDuration) {
                phases.get(currentIndex).onTick(elapsedInPhase);
                break;
            }

            if (elapsedInPhase >= phaseDuration) {
                phases.get(currentIndex).onFinish();
                currentIndex++;
                if (currentIndex < phases.size()) {
                    phases.get(currentIndex).onStart();
                } else {
                    setActualDuration(getDuration());
                    return;
                }
            } else {
                break;
            }
        }
    }

    @Override
    protected void onFinish() {
        if (currentIndex < phases.size()) {
            try { phases.get(currentIndex).onFinish(); } catch (Exception ignored) {}
        }
    }

    @Override
    protected void onStop() {
        setActualDuration(0);
    }
}