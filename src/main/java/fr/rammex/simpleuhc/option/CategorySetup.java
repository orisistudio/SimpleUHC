package fr.rammex.simpleuhc.option;

import api.rammex.gameapi.GameAPI;
import api.rammex.gameapi.category.Category;
import api.rammex.gameapi.category.CategoryManager;
import api.rammex.gameapi.category.CategoryType;
import fr.rammex.simpleuhc.SimpleUHC;
import fr.rammex.simpleuhc.game.SimpleUHCManager;

import java.util.List;

public class CategorySetup {

    public static void setup(){
        CategoryManager categoryManager = SimpleUHC.instance.getCategoryManager();
        SimpleUHCManager manager = SimpleUHC.getSimpleUHCManager();
        if (manager != null) {
            categoryManager.registerCategory(manager, new Category("player", CategoryType.OPTION));
            categoryManager.registerCategory(manager, new Category("world", CategoryType.OPTION));
            categoryManager.registerCategory(manager, new Category("game", CategoryType.OPTION));
        } else {
            System.out.println("[SimpleUHC] Erreur : SimpleUHCManager non instancié !");
        }
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
