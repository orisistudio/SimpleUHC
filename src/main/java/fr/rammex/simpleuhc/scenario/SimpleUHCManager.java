package fr.rammex.simpleuhc.scenario;

import api.rammex.gameapi.role.Role;
import api.rammex.gameapi.scenario.AbstractScenario;

import java.util.Collections;
import java.util.List;

public class SimpleUHCManager extends AbstractScenario {
    public SimpleUHCManager() {
        super("SimpleUHC", "L'UHC le plus simple qui soit !", ".rammex", "1.0", "", 100, 4);
    }

    @Override
    public boolean requiresRoles() {
        return false;
    }

    @Override
    public List<Role> getAvailableRoles() {
        return Collections.emptyList();
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
