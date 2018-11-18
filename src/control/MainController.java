package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.Environment;
import model.Maps.Map1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainController {

    public static void main(String[] args){
        new MainController();
    }

    private ScreenController screenController;
    private Environment menu,blank;
    private Image cursor;

    public MainController(){
        Game.init();
        Game.getConfiguration().graphics().setFullscreen(true);
        Game.getRenderEngine().setBaseRenderScale(1.0f);
        try {
            cursor = ImageIO.read(new File("assets/img/cursor.png"));
        } catch (IOException ex) {
            System.out.println("Bild konnte nicht geladen werden!");
        }
        Game.getScreenManager().getRenderComponent().setCursor(cursor);
        screenController = new ScreenController();
        //screenController.setIngameScreen(new Map1());
        screenController.setMenuScreen();
        new GameController(screenController.getIngameScreen());
        Game.start();
    }
}
