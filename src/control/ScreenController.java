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

/**
 * Klasse ScreenController
 * Reguliert zwischen IngameScreen und MenuScreen
 *
 */
public class ScreenController extends Entity implements IUpdateable {

    /** Referenz auf ein MenuScreen-Objekt*/
    private MenuScreen menuScreen;
    /** Referenz auf ein IngameScreen-Objekt*/
    private static IngameScreen ingameScreen;
    /** Referenz auf ein GameFinishScreen-Objekt*/
    private static GameFinishScreen gameFinishScreen;
    /** Arraylist mit Environments*/
    public static ArrayList<Environment> environments = new ArrayList<>();
    /** Momentane Map*/
    private Map currentMap;
    /** Referenz auf ein GameController-Objekt*/
    private GameController gameController;
    /** Ob man ingame ist*/
    private boolean ingame;


    /**
     * Der ScreenController Konstruktor initialisiert die einzelnen Screens und Environments
     * @param user
     */
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

    /**
     * Update des Interfaces IUpdateable
     */
    @Override
    public void update() {
        if (GameClient.gameStarted && !ingame) {
            setIngameScreen(new Map1());
            ingame = true;
        }
    }


    /**
     * Lauter verändernde und rückgebende Methoden
     */

    public void setMenuScreen(){
        Game.loadEnvironment(environments.get(0));
        Game.getScreenManager().displayScreen(menuScreen);
        gameController.removeControllers();
        gameController = null;
    }

    /**
     * Setzt das Fenster auf das Spielfenster
     *
     * @param map Stage des Spiels
     */
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

    /**
     * Wechselt das Fenster auf den Endscreen
     */
    public static void setGameFinishScreen(){
        Game.loadEnvironment(environments.get(2));
        Game.getScreenManager().displayScreen(gameFinishScreen);
    }

    /**
     * @return Gibt zurück, ob das aktuelle Fenster das Spielfenster ist
     */
    public static boolean isIngame(){
        if(Game.getScreenManager().getCurrentScreen() == ingameScreen){
            return true;
        }else{
            return false;
        }
    }

}
