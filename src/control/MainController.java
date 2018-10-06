package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.gui.screens.Resolution;
import modell.IngameScreen;
import modell.Mage;
import modell.Warrior;

public class MainController {

    public static void main(String[] args){
        new MainController();
    }

    private IngameScreen ingameScreen;
    private Environment environment;

    public MainController(){
        Game.init();

        environment = new Environment("assets/maps/test.tmx");
        Game.loadEnvironment(environment);
        Game.getScreenManager().setResolution(Resolution.Ratio4x3.RES_1024x768);
        ingameScreen = new IngameScreen();
        Game.getScreenManager().addScreen(ingameScreen);
        Game.getEnvironment().add(new Timer());

        Warrior m = new Warrior();

        Game.getEnvironment().add(m);
        ingameScreen.addGravObject(m);
        Game.start();
    }
}
