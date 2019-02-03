package control;


import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Entity;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.graphics.RenderType;
import model.GameClient;
import model.Maps.Map;
import model.Maps.Map1;
import model.Screens.GameFinishScreen;
import model.Screens.IngameScreen;
import model.Screens.MenuScreen;
import model.User;

import java.util.ArrayList;

public class ScreenController extends Entity implements IUpdateable {

    private MenuScreen menuScreen;
    private static IngameScreen ingameScreen;
    private static GameFinishScreen gameFinishScreen;
    public static ArrayList<Environment> environments = new ArrayList<>();
    private Map currentMap;
    private GameController gameController;
    private boolean ingame;

    public ScreenController(User user){
        Game.getLoop().attach(this);
        environments.add(new Environment("assets/maps/blank.tmx"));
        Game.loadEnvironment(environments.get(0));
        menuScreen = new MenuScreen(user);
        Game.getScreenManager().addScreen(menuScreen);
        environments.add(new Environment("assets/maps/blank.tmx"));
        Game.loadEnvironment(environments.get(1));
        ingameScreen = new IngameScreen();
        Game.getScreenManager().addScreen(ingameScreen);
        environments.add(new Environment("assets/maps/blank.tmx"));
        Game.loadEnvironment(environments.get(2));
        gameFinishScreen = new GameFinishScreen(this);
        Game.getScreenManager().addScreen(gameFinishScreen);
    }

    @Override
    public void update() {
        if (GameClient.gameStarted && !ingame) {
            setIngameScreen(new Map1());
            ingame = true;
        }
    }

    public void setMenuScreen(){
        Game.loadEnvironment(environments.get(0));
        Game.getScreenManager().displayScreen(menuScreen);
        gameController = null;
    }

    public void setIngameScreen(Map map){
        Game.loadEnvironment(environments.get(1));
        Game.getScreenManager().displayScreen(ingameScreen);
        currentMap = map;
        Game.getEnvironment().add(map, RenderType.BACKGROUND);
        if(gameController==null) {
            gameController = new GameController();
            System.out.println("Controller erstellt");

        }
    }

    public static void setGameFinishScreen(){
        Game.loadEnvironment(environments.get(2));
        Game.getScreenManager().displayScreen(gameFinishScreen);
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

    public Map getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(Map currentMap) {
        this.currentMap = currentMap;
    }

    public static boolean isIngame(){
        if(Game.getScreenManager().getCurrentScreen() == ingameScreen){
            return true;
        }else{
            return false;
        }
    }

}
