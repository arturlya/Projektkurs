package model.Screens;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.Menu;
import de.gurkenlabs.litiengine.gui.TextFieldComponent;
import de.gurkenlabs.litiengine.gui.screens.Screen;
import de.gurkenlabs.litiengine.gui.screens.ScreenManager;
import de.gurkenlabs.litiengine.input.Input;
import model.abitur.datenstrukturen.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MenuScreen extends Screen implements IUpdateable {

    private static final Object StringUtils = new Object();
    private String menuName;
    private int width,height;
    private boolean typing = false;
    private Image bg,trans,join,options,exit;
    private BufferedImage textfield,create,test;
    private ArrayList<ImageComponent> buttons = new ArrayList<>();
    private ArrayList<TextFieldComponent> textFields = new ArrayList<>();
    private Menu optionsMenu;

    public MenuScreen(){
        super("MENU");
        menuName = "main";
        try {
            bg = ImageIO.read(new File("assets/img/Menu/bg.png"));
            trans = ImageIO.read(new File("assets/img/Menu/trans.png"));
            textfield = ImageIO.read(new File("assets/img/Menu/textfield.png"));
            create = ImageIO.read(new File("assets/img/Buttons/create.png"));
            join = ImageIO.read(new File("assets/img/Buttons/join.png"));
            options = ImageIO.read(new File("assets/img/Buttons/options.png"));
            exit = ImageIO.read(new File("assets/img/Buttons/exit.png"));
            test = ImageIO.read(new File("assets/img/Buttons/test.png"));
        } catch (IOException ex) {
            System.out.println("Bild konnte nicht geladen werden!");
        }
        createButtons();
        createTextField();
        createSubMenus();
    }

    @Override
    public void update() {
        Input.keyboard().onKeyPressed(KeyEvent.VK_ENTER, (key) -> {
            if (textFields.get(0).getText().length() == 5 && )
        });
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
        if (menuName.equalsIgnoreCase("main")) {
            for (int i = 0; i < buttons.size(); i++) {
                buttons.get(i).render(g);
            }
        }else if (menuName.equalsIgnoreCase("create")) {
            g.drawImage(trans,0,0,width,height,null);
            textFields.get(0).render(g);
        }else if (menuName.equalsIgnoreCase("join")) {
            textFields.get(0).render(g);
        }else if (menuName.equalsIgnoreCase("options")) {
            optionsMenu.render(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if (menuName.equalsIgnoreCase("main")) {
            if (buttons.get(0).getBoundingBox().intersects(e.getX(), e.getY(), 1, 1)) menuName = "join";
            if (buttons.get(1).getBoundingBox().intersects(e.getX(), e.getY(), 1, 1)) menuName = "create";
            if (buttons.get(2).getBoundingBox().intersects(e.getX(), e.getY(), 1, 1)) menuName = "options";
            if (buttons.get(3).getBoundingBox().intersects(e.getX(), e.getY(), 1, 1)) System.exit(0);
        }else if (menuName.equalsIgnoreCase("create")) {
            if (!typing) {
                if (textFields.get(0).getBoundingBox().intersects(e.getX(), e.getY(), 1, 1)) typing = true;
            }else if (typing) {

            }
        }
    }

    private void createButtons(){
        buttons.add(new ImageComponent(640,151,640,166,Spritesheet.load("assets/img/Buttons/test.png",640,166),null,test));
        buttons.add(new ImageComponent(640,349,create));
        buttons.add(new ImageComponent(640,547,options));
        buttons.add(new ImageComponent(640,745,exit));
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).prepare();
        }
    }

    private void createTextField(){
        textFields.add(new TextFieldComponent(910,520,100,40,Spritesheet.load(textfield,"assets/img/Menu/textfield.png",100,40),"Port"));
        for (int i = 0; i < textFields.size(); i++) {
            textFields.get(i).prepare();
        }
    }

    private void createSubMenus(){
        optionsMenu = new Menu(0,0,1920,1080,"Move Forwards","Move Backwards","Move Left","Move Right");
        optionsMenu.prepare();
    }
}
