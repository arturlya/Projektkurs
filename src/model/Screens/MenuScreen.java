package model.Screens;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.screens.Screen;
import model.abitur.datenstrukturen.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MenuScreen extends Screen {

    private String menuName;
    private int width,height;
    private Image bg,create;
    private ArrayList<JButton> buttons = new ArrayList<>();
    private MessageHandler messageHandler = new MessageHandler();

    public MenuScreen(){
        super("MENU");
        menuName = "main";
        try {
            bg = ImageIO.read(new File("assets/img/Menu/bg.png"));
            create = ImageIO.read(new File("assets/img/Buttons/create.png"));
        } catch (IOException ex) {
            System.out.println("Bild konnrte nicht geladen werden!");
        }
        createButtons();
        if (menuName.equalsIgnoreCase("main")) {

        }
    }

    public void render(Graphics2D g) {
        if (Game.getConfiguration().graphics().isFullscreen()){
            width = (int)Game.getConfiguration().graphics().getResolution().getWidth();
            height = (int)Game.getConfiguration().graphics().getResolution().getHeight();
        }else{
            width = Game.getConfiguration().graphics().getResolutionWidth();
            height = Game.getConfiguration().graphics().getResolutionHeight();
        }

        g.drawImage(bg,0,0,width,height,null);
    }

    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if (e.getX() >= 2) {
            System.out.println(1);
        }
    }

    public void createButtons(){
        buttons.add(new JButton(new ImageIcon(create)));
        //Game.getScreenManager().
        buttons.get(0).addActionListener(messageHandler);
    }

    private class MessageHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource()==buttons.get(0)) menuName = "create";
        }
    }
}
