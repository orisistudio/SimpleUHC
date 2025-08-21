package fr.rammex.simpleuhc.option;

import api.rammex.gameapi.GameAPI;
import api.rammex.gameapi.category.Category;
import api.rammex.gameapi.category.CategoryManager;
import api.rammex.gameapi.category.CategoryType;
import api.rammex.gameapi.scenario.AbstractScenario;
import fr.rammex.simpleuhc.SimpleUHC;

import java.util.List;
import java.util.Map;

public class CategorySetup {

    public static void setup(){
        CategoryManager categoryManager = SimpleUHC.instance.getCategoryManager();

        categoryManager.registerCategory(SimpleUHC.getSimpleUHCManager(), new Category("player", CategoryType.OPTION));
        categoryManager.registerCategory(SimpleUHC.getSimpleUHCManager(), new Category("world", CategoryType.OPTION));
        categoryManager.registerCategory(SimpleUHC.getSimpleUHCManager(), new Category("game", CategoryType.OPTION));
    }

    public static Category getCategory(String name) {
        List<Category> categories = GameAPI.instance.getCategoryManager().getCategories(SimpleUHC.getSimpleUHCManager());
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }
}
