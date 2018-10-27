package model.Screens;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.gui.screens.Screen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MenuScreen extends Screen {

    private int menuNumber;
    private Image[] menuImages;

    public MenuScreen(){
        super("MENU");
        menuNumber = 0;
        menuImages = new Image[4];
        try {
            menuImages[0] = ImageIO.read(new File("assets/img/menu.png"));
        } catch (IOException ex) {
            System.out.println("Bild konnrte nicht geladen werden!");
        }
    }

    public void render(Graphics2D g) {
        if (menuNumber == 0){
            g.drawImage(menuImages[menuNumber],0,0, Game.getConfiguration().graphics().getResolutionWidth(),Game.getConfiguration().graphics().getResolutionHeight(),null);
        }
        Game.getEnvironment().render(g);
    }
}
