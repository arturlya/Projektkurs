package model;

import de.gurkenlabs.litiengine.gui.screens.GameScreen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;

public class IngameScreen extends GameScreen {

    private ArrayList<GravitationalObject> gravObjects;
    private Menu menu;

    public IngameScreen(){
        gravObjects = new ArrayList<>();
        menu = new Menu();

    }

    @Override
    public void render(final Graphics2D g){
        super.render(g);
        menu.render(g);
        for(int i = 0; i < gravObjects.size(); i++){
            if(gravObjects.get(i) instanceof Player){
                if(((Player) gravObjects.get(i)).getHurtbox().isHurting()){
                    g.setColor(new Color(255,0,0,100));
                }else{
                    g.setColor(new Color(0,255,0,100));
                }
                g.fill(((Player) gravObjects.get(i)).getHurtbox());
            }
            g.setColor(new Color(70,120,255));
            if(gravObjects.get(i) instanceof Player)if(((Player) gravObjects.get(i)).isShieldActive()) g.setColor(new Color(150,150,150));
            g.fill(gravObjects.get(i).getHitbox());
        }
    }

    public void addGravObject(GravitationalObject g){
        gravObjects.add(g);
    }

    public ArrayList<GravitationalObject> getGravObjects() {
        return gravObjects;
    }

    public void removeGravObject(GravitationalObject g){
        gravObjects.remove(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
    }

    private class Menu {

        private int menuNumber;
        private Image[] menuImages;

        Menu(){
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
                g.drawImage(menuImages[menuNumber],0,0,1920,1080,null);
            }
        }
    }
}
