package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.sound.Sound;
import model.Maps.Map1;
import model.StaticData;
import model.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainController {

    public static void main(String[] args){
        new MainController();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Config.pref.exportNode(new FileOutputStream("assets/keyAssignment.xml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    private ScreenController screenController;
    private Image cursor;

    public MainController(){
        Game.init();
        Game.getConfiguration().graphics().setFullscreen(false);
        new Config();
        try {
            cursor = ImageIO.read(new File("assets/img/cursor.png"));
        } catch (IOException ex) {
            System.err.println(ex);
            //System.out.println("Bild konnte nicht geladen werden!");
        }
        Game.getScreenManager().getRenderComponent().setCursor(cursor,0,0);
        screenController = new ScreenController(new User());
        //screenController.setIngameScreen(new Map1());
        screenController.setMenuScreen();
        User user = new User();
        user.init();


        /**
         * Wer von euch lit ist uncommented!
         */
        Game.getSoundEngine().playMusic(Sound.get("assets/audio/Musik/bgm.mp3"));
        Game.start();

    }
}
