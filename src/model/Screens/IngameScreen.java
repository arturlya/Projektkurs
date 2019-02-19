package model.Screens;

import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import model.*;
import model.abitur.datenstrukturen.List;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Klasse IngameScreen
 * Erbt von der Klasse GameScreen
 * Wird erstellt beim Wechsel zum Ingame
 * Ist relevant zum Zeichnen der Spieler und deren Leben
 */
public class IngameScreen extends GameScreen {

    private ArrayList<GravitationalObject> gravObjects;
    private Image[] mage,warrior,gambler,info;
    private List<Player> others;
    private int alpha;
    /** Screen-Größe und Breite*/
    private int width = StaticData.ScreenWidth,height = StaticData.ScreenHeight;
    /** Multiplikator falls Auflösung nicht 1920x1080*/
    private float widthMultiplier = StaticData.ScreenWidthMultiplier,heightMultiplier = StaticData.ScreenHeightMultiplier;

    /**
     * Konstruktor des IngameScreens
     * Erstellt einige Bilder, wie z.b. die Bilder der Lebensanzeigen
     * @param others eine Liste der anderen Spieler
     */
    public IngameScreen(List<Player> others){
        super("INGAME");
        gravObjects = new ArrayList<>();
        mage = new Image[4];
        warrior = new Image[4];
        gambler = new Image[4];
        info = new Image[6];
        try {
            info[0] = ImageIO.read(new File("assets/img/ingame/Players/Mage/info1.png"));
            info[2] = ImageIO.read(new File("assets/img/ingame/Players/Warrior/info1.png"));
            info[4] = ImageIO.read(new File("assets/img/ingame/Players/Gambler/info1.png"));
            info[1] = ImageIO.read(new File("assets/img/ingame/Players/Mage/info2.png"));
            info[3] = ImageIO.read(new File("assets/img/ingame/Players/Warrior/info2.png"));
            info[5] = ImageIO.read(new File("assets/img/ingame/Players/Gambler/info2.png"));
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

    /**
     * Render-Methode des IRenderable-Interfaces
     * Zeichnet die Input Informationen
     */
    @Override
    public void render(final Graphics2D g){
        super.render(g);
        renderLifesAndDamage(g);
        if(GameClient.player instanceof Mage){
            g.drawImage(info[0],(int)(10*widthMultiplier),(int)(10*heightMultiplier),(int)(info[0].getWidth(null)*widthMultiplier),(int)(info[0].getHeight(null)*heightMultiplier),null);
            g.drawImage(info[1],(int)(500*widthMultiplier),(int)(10*heightMultiplier),(int)(info[1].getWidth(null)*widthMultiplier),(int)(info[1].getHeight(null)*heightMultiplier),null);
        }else if(GameClient.player instanceof Warrior){
            g.drawImage(info[2],(int)(10*widthMultiplier),(int)(10*heightMultiplier),(int)(info[2].getWidth(null)*widthMultiplier),(int)(info[2].getHeight(null)*heightMultiplier),null);
            g.drawImage(info[3],(int)(500*widthMultiplier),(int)(10*heightMultiplier),(int)(info[3].getWidth(null)*widthMultiplier),(int)(info[3].getHeight(null)*heightMultiplier),null);
        }else if(GameClient.player instanceof Gambler){
            g.drawImage(info[4],(int)(10*widthMultiplier),(int)(10*heightMultiplier),(int)(info[4].getWidth(null)*widthMultiplier),(int)(info[4].getHeight(null)*heightMultiplier),null);
            g.drawImage(info[5],(int)(500*widthMultiplier),(int)(10*heightMultiplier),(int)(info[5].getWidth(null)*widthMultiplier),(int)(info[5].getHeight(null)*heightMultiplier),null);
        }
    }

    /**
     * Zeichnet die Lebensanzeige der Spieler
     */
    private void renderLifesAndDamage(final Graphics2D g){
        try {
            if (others != null) {
                boolean playerDrawn = false;
                others.toFirst();
                while (others.hasAccess()) {
                    if (GameClient.player != null && !playerDrawn) {
                        playerDrawn = true;
                        int x = 1920 - GameClient.player.getPlayerNumber() * 120 + 3;
                        g.setColor(Color.WHITE);
                        g.fillRect((int) (x * widthMultiplier) + 3, 13, (int) (104 * widthMultiplier), (int) (104 * widthMultiplier));
                        if (GameClient.player.getKnockbackPercentage() >= 50) {
                            alpha = 255;
                        } else {
                            alpha = GameClient.player.getKnockbackPercentage() * 5;
                        }
                        g.setColor(new Color(255, 0, 0, alpha));
                        g.fillRect((int) (x * widthMultiplier) + 3, 13, (int) (104 * widthMultiplier), (int) (104 * widthMultiplier));
                        if (GameClient.player instanceof Mage) {
                            g.drawImage(mage[GameClient.player.getStocks()], (int) (x * widthMultiplier), 10, (int) (110 * widthMultiplier), (int) (110 * heightMultiplier), null);
                        } else if (GameClient.player instanceof Warrior) {
                            g.drawImage(warrior[GameClient.player.getStocks()], (int) (x * widthMultiplier), 10, (int) (110 * widthMultiplier), (int) (110 * heightMultiplier), null);
                        } else if (GameClient.player instanceof Gambler) {
                            g.drawImage(gambler[GameClient.player.getStocks()], (int) (x * widthMultiplier), 10, (int) (110 * widthMultiplier), (int) (110 * heightMultiplier), null);
                        }
                    } else if (others.getContent().getStocks() >= 0) {
                        int x = 1920 - others.getContent().getPlayerNumber() * 120 + 3;
                        g.setColor(Color.WHITE);
                        g.fillRect((int) (x * widthMultiplier) + 3, 13, (int) (104 * widthMultiplier), (int) (104 * widthMultiplier));
                        if (others.getContent().getKnockbackPercentage() >= 50) {
                            alpha = 255;
                        } else {
                            alpha = others.getContent().getKnockbackPercentage() * 5;
                        }
                        g.setColor(new Color(255, 0, 0, alpha));
                        g.fillRect((int) (x * widthMultiplier) + 3, 13, (int) (104 * widthMultiplier), (int) (104 * widthMultiplier));
                        if (others.getContent() instanceof Mage) {
                            g.drawImage(mage[others.getContent().getStocks()], (int) (x * widthMultiplier), 10, (int) (110 * widthMultiplier), (int) (110 * heightMultiplier), null);
                        } else if (others.getContent() instanceof Warrior) {
                            g.drawImage(warrior[others.getContent().getStocks()], (int) (x * widthMultiplier), 10, (int) (110 * widthMultiplier), (int) (110 * heightMultiplier), null);
                        } else if (others.getContent() instanceof Gambler) {
                            g.drawImage(gambler[others.getContent().getStocks()], (int) (x * widthMultiplier), 10, (int) (110 * widthMultiplier), (int) (110 * heightMultiplier), null);
                        }
                        others.next();
                    }
                }
            }
            if (GameClient.others != null) others = GameClient.others;
        }catch(Exception e){}
    }


}
