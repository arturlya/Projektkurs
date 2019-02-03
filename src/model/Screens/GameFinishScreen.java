package model.Screens;

import control.ScreenController;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import model.StaticData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class GameFinishScreen extends GameScreen {

    private Image bg, trans, exit;
    private int width = StaticData.ScreenWidth,height = StaticData.ScreenHeight;
    private float widthMultiplier = StaticData.ScreenWidthMultiplier,heightMultiplier = StaticData.ScreenHeightMultiplier;
    private ImageComponent button;
    private ScreenController screenController;

    public GameFinishScreen(ScreenController screenController){
        super("GAMEFINISH");
        try {
            bg = ImageIO.read(new File("assets/img/Menu/bg.png"));
            trans = ImageIO.read(new File("assets/img/Menu/trans.png"));
            exit = ImageIO.read(new File("assets/img/Buttons/exit.png"));
        } catch (IOException ex) {
            System.out.println("Bild konnte nicht geladen werden!");
        }
        button = new ImageComponent(640*widthMultiplier,745*heightMultiplier,exit);
        button.prepare();
        this.screenController = screenController;
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(bg, 0, 0, width, height, null);
        g.drawImage(trans, 0, 0, width, height, null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(button.getBoundingBox().contains(e.getPoint())){
            screenController.setMenuScreen();
        }
    }
}
