package control;


import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Entity;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.graphics.RenderType;
import model.Maps.Map;
import model.Screens.IngameScreen;
import model.Screens.MenuScreen;

import java.util.ArrayList;

public class ScreenController extends Entity implements IUpdateable {

    private MenuScreen menuScreen;
    private IngameScreen ingameScreen;
    private ArrayList<Environment> environments = new ArrayList<>();

    public ScreenController(){
        environments.add(new Environment("assets/maps/blank.tmx"));
        Game.loadEnvironment(environments.get(0));
        menuScreen = new MenuScreen();
        Game.getScreenManager().addScreen(menuScreen);
        environments.add(new Environment("assets/maps/blank.tmx"));
        Game.loadEnvironment(environments.get(1));
        ingameScreen = new IngameScreen();
        Game.getScreenManager().addScreen(ingameScreen);
    }

    @Override
    public void update() {

    }

    public void setMenuScreen(){
        Game.loadEnvironment(environments.get(0));
        Game.getScreenManager().displayScreen(menuScreen);
    }

    public void setIngameScreen(Map map){
        Game.loadEnvironment(environments.get(1));
        Game.getScreenManager().displayScreen(ingameScreen);
        Game.getEnvironment().add(map, RenderType.BACKGROUND);
    }

    public MenuScreen getMenuScreen() {
        return menuScreen;
    }

    public IngameScreen getIngameScreen() {
        return ingameScreen;
    }

    public ArrayList<Environment> getEnvironments() {
        return environments;
    }
}
