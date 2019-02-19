package model.Screens;

import control.ScreenController;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import model.StaticData;
import model.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/**
 * Klasse GameFinishScreen
 * Erbt von GameScreen
 * Wird gezeigt, sobald das Spiel vorbei ist
 */
public class GameFinishScreen extends GameScreen {
    /**Bilder*/
    private Image bg, trans, exit;
    /**Bildschirm Breite und Höhe*/
    private int width = StaticData.ScreenWidth,height = StaticData.ScreenHeight;
    /**Breite und Höhe Multiplikator, zur Skalierung notwendig*/
    private float widthMultiplier = StaticData.ScreenWidthMultiplier,heightMultiplier = StaticData.ScreenHeightMultiplier;
    /**Exit button*/
    private ImageComponent button;
    /**Referenz auf den ScreenController*/
    private ScreenController screenController;

    /**
     * Konstruktor des GameScreens
     * Erstellt die Bilder und den Exit-Button
     */
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

    /**
     * Render-Methode des IRenderable-Interfaces
     * Zeichnet die Bilder und den Button
     */
    @Override
    public void render(Graphics2D g) {
        g.drawImage(bg, 0, 0, width, height, null);
        g.drawImage(trans, 0, 0, width, height, null);
        button.render(g);
    }

    /**
     * mouseClicked-Methode der GameScreen Klasse
     * Überprüft, ob der Button geklickt wird
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if(button.getBoundingBox().contains(e.getPoint())){
            screenController.setMenuScreen();
            User.closeConnection();
        }
    }
}
