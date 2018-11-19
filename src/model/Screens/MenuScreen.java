package model.Screens;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.gui.*;
import de.gurkenlabs.litiengine.gui.Menu;
import de.gurkenlabs.litiengine.gui.screens.Screen;
import de.gurkenlabs.litiengine.input.Input;
import model.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MenuScreen extends Screen implements IUpdateable {

    private String menuName;
    public static String joinPort,hostPort;
    private char[] chars;
    private int width = StaticData.ScreenWidth,height = StaticData.ScreenHeight;
    private static boolean chooseKey;
    private Image bg,trans,join,options,exit;
    private BufferedImage textfield,create;
    private ArrayList<ImageComponent> buttons = new ArrayList<>();
    private ArrayList<TextFieldComponent> textFields = new ArrayList<>();
    private Menu optionsMenu,keyNameMenu,keyMenu;
    private KeyChecker keyChecker = new KeyChecker();
    private HorizontalSlider audio;
    private CheckBox fullscreen;

    public MenuScreen(){
        super("MENU");
        Game.getLoop().attach(this);
        menuName = "main";
        try {
            bg = ImageIO.read(new File("assets/img/Menu/bg.png"));
            trans = ImageIO.read(new File("assets/img/Menu/trans.png"));
            textfield = ImageIO.read(new File("assets/img/Menu/textfield.png"));
            create = ImageIO.read(new File("assets/img/Buttons/create.png"));
            join = ImageIO.read(new File("assets/img/Buttons/join.png"));
            options = ImageIO.read(new File("assets/img/Buttons/options.png"));
            exit = ImageIO.read(new File("assets/img/Buttons/exit.png"));
        } catch (IOException ex) {
            System.out.println("Bild konnte nicht geladen werden!");
        }
        createButtons();
        createTextField();
        createSubMenus();
        Input.keyboard().addKeyListener(keyChecker);
    }

    private class KeyChecker extends KeyAdapter{

        private int key;

        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getKeyCode() != KeyEvent.VK_ENTER && MenuScreen.chooseKey) {
                key = e.getKeyCode();
            }
        }

        public int getKey(){
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }
    }

    @Override
    public void update() {
        Input.keyboard().onKeyTyped(KeyEvent.VK_ESCAPE, (key) -> menuName = "main");
        Input.keyboard().onKeyTyped(KeyEvent.VK_ENTER, (key) -> {
            if (textFields.get(0).getText().length() == 4 && textFields.get(0).getText() != null && menuName.equalsIgnoreCase("join")) {
                chars = textFields.get(0).getText().toCharArray();
                boolean isNumber = false;
                for (char aChar : chars) {
                    isNumber = Character.isDigit(aChar);
                    if (!isNumber) break;
                }
                if (isNumber) {
                    joinPort = textFields.get(0).getText();
                    textFields.get(0).setText("");
                }
            }else if (textFields.get(1).getText().length() == 4 && textFields.get(1).getText() != null && menuName.equalsIgnoreCase("create")) {
                chars = textFields.get(1).getText().toCharArray();
                boolean isNumber = false;
                for (char aChar : chars) {
                    isNumber = Character.isDigit(aChar);
                    if (!isNumber) break;
                }
                if (isNumber) {
                    hostPort = textFields.get(1).getText();
                    textFields.get(1).setText("");
                }
            }
        });
        if (chooseKey && keyChecker.getKey() != 0 && keyChecker.getKey() != 27) {
            for (int i = 0; i < keyMenu.getCellComponents().size(); i++) {
                if (KeyEvent.getKeyText(keyChecker.getKey()).equalsIgnoreCase(keyMenu.getCellComponents().get(i).getText())) {
                    //Doppelten Key löschen
                }
            }
            if (keyNameMenu.getCurrentSelection() == 0) {
                StaticData.moveUp = keyChecker.getKey();
                keyMenu.getCellComponents().get(0).setText(KeyEvent.getKeyText(keyChecker.getKey()));
            }else if (keyNameMenu.getCurrentSelection() == 1) {
                StaticData.moveDown = keyChecker.getKey();
                keyMenu.getCellComponents().get(1).setText(KeyEvent.getKeyText(keyChecker.getKey()));
            }else if (keyNameMenu.getCurrentSelection() == 2) {
                StaticData.moveLeft = keyChecker.getKey();
                keyMenu.getCellComponents().get(2).setText(KeyEvent.getKeyText(keyChecker.getKey()));
            }else if (keyNameMenu.getCurrentSelection() == 3) {
                StaticData.moveRight = keyChecker.getKey();
                keyMenu.getCellComponents().get(3).setText(KeyEvent.getKeyText(keyChecker.getKey()));
            }else if (keyNameMenu.getCurrentSelection() == 4) {
                StaticData.jump = keyChecker.getKey();
                keyMenu.getCellComponents().get(4).setText(KeyEvent.getKeyText(keyChecker.getKey()));
            }else if (keyNameMenu.getCurrentSelection() == 5) {
                StaticData.normalAttack = keyChecker.getKey();
                keyMenu.getCellComponents().get(5).setText(KeyEvent.getKeyText(keyChecker.getKey()));
            }else if (keyNameMenu.getCurrentSelection() == 6) {
                StaticData.specialAttack = keyChecker.getKey();
                keyMenu.getCellComponents().get(6).setText(KeyEvent.getKeyText(keyChecker.getKey()));
            }
            chooseKey = false;
            keyChecker.setKey(0);
        }
        if (menuName.equalsIgnoreCase("audiovideo")) {
            StaticData.audioVolume = (int)audio.getCurrentValue();
            Game.getConfiguration().sound().setMusicVolume(StaticData.audioVolume/100f);
        }
    }

    public void render(Graphics2D g) {
        g.drawImage(bg, 0, 0, width, height, null);
        if (menuName.equalsIgnoreCase("main")) {
            for (ImageComponent button : buttons) {
                button.render(g);
            }
        }else if (menuName.equalsIgnoreCase("join")) {
            g.drawImage(trans, 0, 0, width, height, null);
            textFields.get(0).render(g);
        }else if (menuName.equalsIgnoreCase("create")) {
            g.drawImage(trans, 0, 0, width, height, null);
            textFields.get(1).render(g);
        }else if (menuName.equalsIgnoreCase("options")) {
            g.drawImage(trans,0,0,width,height,null);
            g.drawImage(trans,0,0,width,height,null);
            optionsMenu.render(g);
        }else if (menuName.equalsIgnoreCase("keys")) {
            g.drawImage(trans,0,0,width,height,null);
            g.drawImage(trans,0,0,width,height,null);
            keyNameMenu.render(g);
            keyMenu.render(g);
        }else if (menuName.equalsIgnoreCase("audiovideo")) {
            g.drawImage(trans,0,0,width,height,null);
            g.drawImage(trans,0,0,width,height,null);
            audio.render(g);
            g.drawString("Volume: ",width/2-200,height/2-117);
            g.drawString(""+audio.getCurrentValue(),width/2+200,height/2-117);
        }else if (menuName.equalsIgnoreCase("overall")) {
            g.drawImage(trans,0,0,width,height,null);
            g.drawImage(trans,0,0,width,height,null);
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
        }else if (menuName.equalsIgnoreCase("options")) {
            if (optionsMenu.getCellComponents().get(0).getBoundingBox().intersects(e.getX(),e.getY(),1,1)) menuName = "keys";
            if (optionsMenu.getCellComponents().get(1).getBoundingBox().intersects(e.getX(),e.getY(),1,1)) menuName = "audiovideo";
            if (optionsMenu.getCellComponents().get(2).getBoundingBox().intersects(e.getX(),e.getY(),1,1)) menuName = "overall";
        }else if (menuName.equalsIgnoreCase("keys")) {
            for (int i = 0; i < keyNameMenu.getCellComponents().size(); i++) {
                if (keyNameMenu.getCellComponents().get(i).getBoundingBox().intersects(e.getX(), e.getY(), 1, 1)) {

                    chooseKey = true;
                    keyNameMenu.setCurrentSelection(i);
                }
            }
        }
    }

    private void createButtons(){
        buttons.add(new ImageComponent(640,151,join));
        buttons.add(new ImageComponent(640,349,create));
        buttons.add(new ImageComponent(640,547,options));
        buttons.add(new ImageComponent(640,745,exit));
        for (ImageComponent button : buttons) {
            button.prepare();
        }
    }

    private void createTextField(){
        textFields.add(new TextFieldComponent(910,520,100,40,Spritesheet.load(textfield,"assets/img/Menu/textfield.png",100,40),""));
        textFields.add(new TextFieldComponent(910,520,100,40,Spritesheet.load(textfield,"assets/img/Menu/textfield.png",100,40),""));
        for (TextFieldComponent textField : textFields) {
            textField.setFormat(TextFieldComponent.INTEGER_FORMAT);
            textField.setMaxLength(4);
            textField.prepare();
        }
    }

    private void createSubMenus(){
        keyNameMenu = new Menu(0, 0, width - 500, height, "Move Forwards", "Move Backwards", "Move Left", "Move Right", "Jump", "Normal Attack", "Special Attack");
        keyNameMenu.prepare();
        keyNameMenu.setEnabled(true);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(KeyEvent.getKeyText(StaticData.moveUp));
        arrayList.add(KeyEvent.getKeyText(StaticData.moveDown));
        arrayList.add(KeyEvent.getKeyText(StaticData.moveLeft));
        arrayList.add(KeyEvent.getKeyText(StaticData.moveRight));
        arrayList.add(KeyEvent.getKeyText(StaticData.jump));
        arrayList.add(KeyEvent.getKeyText(StaticData.normalAttack));
        arrayList.add(KeyEvent.getKeyText(StaticData.specialAttack));
        keyMenu = new Menu(width-490,0,490,height,arrayList.get(0),arrayList.get(1),arrayList.get(2),arrayList.get(3),arrayList.get(4),arrayList.get(5),arrayList.get(6));
        keyMenu.prepare();
        keyMenu.setEnabled(true);
        optionsMenu = new Menu(0, 0, width, height, "Key Assignment", "Audio & Video", "Overall");
        optionsMenu.prepare();
        optionsMenu.setEnabled(true);
        createAudioSlider();
    }

    private void createAudioSlider(){
        audio = new HorizontalSlider(width/2-100,height/2-150,300,50,0,100,5,null,null,true);
        audio.prepare();
        audio.setEnabled(true);
        audio.setCurrentValue(StaticData.audioVolume);
    }

    private void createFullscreenCheckMark(){

    }
}
