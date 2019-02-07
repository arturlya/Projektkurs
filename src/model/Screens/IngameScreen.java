package model.Screens;

import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import model.*;
import model.abitur.datenstrukturen.List;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class IngameScreen extends GameScreen {

    private ArrayList<GravitationalObject> gravObjects;
    private Image[] mage,warrior,gambler;
    private List<Player> others;
    private int alpha;

    public IngameScreen(List<Player> others){
        super("INGAME");
        gravObjects = new ArrayList<>();
        mage = new Image[4];
        warrior = new Image[4];
        gambler = new Image[4];
        try {
            mage[0] = ImageIO.read(new File("assets/img/ingame/Players/Mage/MageLife0.png"));
            mage[1] = ImageIO.read(new File("assets/img/ingame/Players/Mage/MageLife1.png"));
            mage[2] = ImageIO.read(new File("assets/img/ingame/Players/Mage/MageLife2.png"));
            mage[3] = ImageIO.read(new File("assets/img/ingame/Players/Mage/MageLife3.png"));
            warrior[0] = ImageIO.read(new File("assets/img/ingame/Players/Warrior/WarriorLife0.png"));
            warrior[1] = ImageIO.read(new File("assets/img/ingame/Players/Warrior/WarriorLife1.png"));
            warrior[2] = ImageIO.read(new File("assets/img/ingame/Players/Warrior/WarriorLife2.png"));
            warrior[3] = ImageIO.read(new File("assets/img/ingame/Players/Warrior/WarriorLife3.png"));
            gambler[0] = ImageIO.read(new File("assets/img/ingame/Players/Gambler/GamblerLife0.png"));
            gambler[1] = ImageIO.read(new File("assets/img/ingame/Players/Gambler/GamblerLife1.png"));
            gambler[2] = ImageIO.read(new File("assets/img/ingame/Players/Gambler/GamblerLife2.png"));
            gambler[3] = ImageIO.read(new File("assets/img/ingame/Players/Gambler/GamblerLife3.png"));
        }catch (Exception e) {
            System.out.println("Konnte Lebensanzeigen nicht laden!");
        }
        this.others = others;
    }

    @Override
    public void render(final Graphics2D g){
        super.render(g);
        renderLifesAndDamage(g);
    }

    private void renderLifesAndDamage(final Graphics2D g){
        others.toFirst();
        while (others.hasAccess()) {
            if (others.getContent().getStocks() >= 0) {
                g.setColor(Color.WHITE);
                g.fillRect(1920 - (others.getContent().getPlayerNumber() * 120) + 3, 13, 104, 104);
                if (others.getContent().getKnockbackPercentage() >= 50) {
                    alpha = 255;
                } else {
                    alpha = others.getContent().getKnockbackPercentage() * 5;
                }
                g.setColor(new Color(255, 0, 0, alpha));
                System.out.println(others.getContent().getKnockbackPercentage());
                g.fillRect(1920 - (others.getContent().getPlayerNumber() * 120) + 3, 13, 104, 104);
                if (others.getContent() instanceof Mage) {
                    g.drawImage(mage[others.getContent().getStocks()], 1920 - (others.getContent().getPlayerNumber() * 120), 10, null);
                } else if (others.getContent() instanceof Warrior) {
                    g.drawImage(warrior[others.getContent().getStocks()], 1920 - (others.getContent().getPlayerNumber() * 120), 10, null);
                } else if (others.getContent() instanceof Gambler) {
                    g.drawImage(gambler[others.getContent().getStocks()], 1920 - (others.getContent().getPlayerNumber() * 120), 10, null);
                }
            }
            others.next();
        }
    }

    public void renderGravObjects(final Graphics2D g){
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
            g.fill(gravObjects.get(i).getRenderHitbox());
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
}
