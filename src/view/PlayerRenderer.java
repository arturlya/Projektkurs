package view;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.graphics.IRenderable;
import model.Gambler;
import model.Mage;
import model.Player;
import model.Warrior;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static control.Timer.dt;

/**
 * Klasse PlayerRenderer
 * Implementiert IUpdatable, IRenderable
 * Zeichnet den Spieler abhängig von der Fenstergröße
 */
public class PlayerRenderer implements IUpdateable, IRenderable {
    /**Merkt sich den Path zum Ordner, indem die Images des Spielers enthalten sind*/
    private String pathToImageFolder;
    /**Merkt sich den Spieler, welcher gezeichnet werden soll*/
    private Player player;
    /**Merkt sich, ob hurtboxes gezeichnet werden sollen*/
    private boolean renderHurtboxes = true;
    /**Merkt sich die renderHurtbox, also die hurtbox abhängig von der Fenstergröße gescaled*/
    private Rectangle2D renderHurtbox;
    /**Merkt sich die, auf die Fenstergröße angepassten Koordinaten und Maße, und andere Maße, falls der Spieler außerhalb des Fensters ist*/
    private double rx, ry, rwidth, rheight, offScreenX, offScreenY, offScreenWidth, offScreenHeight;
    /**Merkt sich die Fensterecke, aus der der Spieler heraus gegangen ist*/
    private Point corner;
    /**Merkt sich die Bilder der Kreise, die auftauchen, wenn ein Spieler außerhalb des Fensters ist*/
    private Image[] offScreenCircles;
    /**Merkt sich den Kreis, der gerade gezeichnet werden soll*/
    private Image activeOffScreenCircle;
    /**Merkt sich die Koordinaten des Kreises*/
    private double circleX, circleY;
    /**Merkt sich die Breite des Fensters*/
    private double gameWidth = Game.getConfiguration().graphics().getResolution().getWidth();
    /**Merkt sich die Höhe des Fensters*/
    private double gameHeight = Game.getConfiguration().graphics().getResolution().getHeight();


    private String currentMove, currentAnimation;
    /**Merkt sich die verschiedenen Bilder des Spielers*/
    private HashMap<String, ArrayList<Image>> playerImages;

    private ArrayList<String> allPlayerMoves;
    /**Merkt sich das momentan gezeichnete Bild des Spielers*/
    private Image currentPlayerImage;
    /**Merkt sich den Timer, der für die standingAnimation verantwortlich ist*/
    private double animationTimer, currentAnimationStartTime, standingAnimationTimer;

    private double animationOffsetX, animationOffsetY;

    /**
     * Konstruktor des PlayerRenderers
     * @param player der Spieler, der gezeichnet werden soll
     */
    public PlayerRenderer(Player player) {
        this.player = player;
        renderHurtbox = new Rectangle2D.Double(0, 0, 0, 0);
        corner = new Point(0, 0);
        createCircleImages();
        pathToImageFolder = "assets/img/ingame/Players";
        if (player instanceof Mage) {
            pathToImageFolder += "/Mage";
        }else if (player instanceof Warrior) {
            pathToImageFolder += "/Warrior";
        }else if (player instanceof Gambler) {
            pathToImageFolder += "/Gambler";
        }
        allPlayerMoves = new ArrayList<>();
        playerImages = new HashMap<>();
    }

    /**
     * Erstellt die verschiedenen Bilder des Spielers
     */
    public void createImages(){
        for(String s : allPlayerMoves){
            String a = "Left";
            for(int i = 0; i < 2; i++) {
                ArrayList<Image> tempImg = new ArrayList();
                boolean done = false;
                int j = 1;
                while(!done){
                    try{
                        tempImg.add(ImageIO.read(new File(pathToImageFolder+s+a+j+".png")));
                        j++;
                    } catch (IOException ex) {
                        done = true;
                    }
                }
                String temp = s.replace("/","") + a;
                playerImages.put(temp, tempImg);
                a = "Right";
            }
        }
    }

    /**
     * Erstellt die verschiedenen Bilder der Kreise
     */
    private void createCircleImages(){
        offScreenCircles = new Image[8];
        try {
            offScreenCircles[0] = ImageIO.read(new File("assets/img/ingame/offScreenCircles/circleDown.png"));
            offScreenCircles[1] = ImageIO.read(new File("assets/img/ingame/offScreenCircles/circleLeftDown.png"));
            offScreenCircles[2] = ImageIO.read(new File("assets/img/ingame/offScreenCircles/circleLeft.png"));
            offScreenCircles[3] = ImageIO.read(new File("assets/img/ingame/offScreenCircles/circleLeftUp.png"));
            offScreenCircles[4] = ImageIO.read(new File("assets/img/ingame/offScreenCircles/circleUp.png"));
            offScreenCircles[5] = ImageIO.read(new File("assets/img/ingame/offScreenCircles/circleRightUp.png"));
            offScreenCircles[6] = ImageIO.read(new File("assets/img/ingame/offScreenCircles/circleRight.png"));
            offScreenCircles[7] = ImageIO.read(new File("assets/img/ingame/offScreenCircles/circleRightDown.png"));
        } catch (IOException ex) {
            System.out.println("Bild konnte nicht geladen werden!");
        }
        activeOffScreenCircle = offScreenCircles[0];
    }

    /**
     * Render-Methode des IRenderable Interfaces
     * Zeichnet den Spieler, dessen hitbox, hurtbox und, falls nötig, einen Kreis
     */
    @Override
    public void render(Graphics2D g) {
        if(renderHurtboxes) {
            if (player.getHurtbox().isHurting()) {
                g.setColor(new Color(255, 0, 0, 100));
            } else {
                g.setColor(new Color(0, 255, 0, 100));
            }
            g.fill(renderHurtbox);
        }
        g.setColor(new Color(70,120,255));
        if(player.isShieldActive()) {
            g.setColor(new Color(150,150,150));
        }
        g.fill(player.getRenderHitbox());
        if(!(currentPlayerImage==null)) {
            g.drawImage(currentPlayerImage, (int) (player.getRenderHitbox().getX() + animationOffsetX), (int) (player.getRenderHitbox().getY() + animationOffsetY), (int) (currentPlayerImage.getWidth(null) * gameWidth / 1920), (int) (currentPlayerImage.getHeight(null) * gameHeight / 1080), null);
        }
        if(!player.getHitbox().intersects(0,0,1920,1080)){
            g.drawImage(activeOffScreenCircle, (int)(circleX/1920*gameWidth), (int)(circleY/1080*gameHeight), (int)(150.0/1920*gameWidth), (int)(150.0/1080*gameHeight), null);
        }
    }

    /**
     * Update-Methode des IUpdatable Interfaces
     */
    @Override
    public void update() {
        adjustRenderHitbox();
        if (renderHurtboxes) {
            renderHurtbox.setRect(player.getHurtbox().x / 1920 * gameWidth, player.getHurtbox().y / 1080 * gameHeight, player.getHurtbox().width / 1920 * gameWidth, player.getHurtbox().height / 1080 * gameHeight);
        }
        updateAnimationLoop();
    }

    /**
     * Zählt den AnimationTimer durch und setzt das dazu passende Bild als aktives Bild
     */
    private void updateAnimationLoop(){
        if(animationTimer > 0){
            animationTimer -= dt;
        }else{
            currentMove = "Standing";
            animationOffsetX = 0;
            animationOffsetY = 0;
        }
        currentAnimation = currentMove;
        if(player.getLookingAt() == 1){
            currentAnimation += "Right";
        }else{
            currentAnimation += "Left";
        }
        if(currentMove.equals("Standing")){
            standingAnimationTimer -= dt;
            if(standingAnimationTimer <= 0){
                standingAnimationTimer = 1.5;
            }
            int b = playerImages.get(currentAnimation).size();
            if(b > 0) {
                double i = (-b / 1.5) * standingAnimationTimer + b;
                currentPlayerImage = playerImages.get(currentAnimation).get((int) i);
            }
        }else{
            if(animationTimer >= 0){
                int b = playerImages.get(currentAnimation).size();
                if(b > 0) {
                    double i = (-b / currentAnimationStartTime) * animationTimer + b;
                    currentPlayerImage = playerImages.get(currentAnimation).get((int) i);
                }
            }
        }
        /*if(standingAnimationTimer > 1.5){
            standingAnimationTimer = 0;
        }
        int i = 0;
        i += (int)(standingAnimationTimer * (5/1.5));
        if(i > 4){
            i = 4;
        }
        if(player.getLookingAt() == 1){
            currentPlayerImage = playerImages[i+5];
        }else {
            currentPlayerImage = playerImages[i];
        }
        standingAnimationTimer += dt;*/
    }

    /**
     * Legt die RenderHitbox des Spielers fest
     * Wenn der Spieler im Fenster ist, dann wird die Hitbox nur auf Fenstergröße angepasst
     * Wenn der Spieler außerhalb ist, wird an der richtigen Stelle ein Kreis gezeichnet und der Spieler innerhalb des Kreises
     * Je weiter der Spieler vom Fensterrand entfernt ist, desto kleiner wird er im Kreis gezeichnet
     */
    private void adjustRenderHitbox(){
        rx = player.getHitbox().x/1920*gameWidth;
        ry = player.getHitbox().y/1080*gameHeight;
        rwidth = player.getHitbox().width/1920*gameWidth;
        rheight = player.getHitbox().height/1080*gameHeight;
        if(!player.getHitbox().intersects(0,0,1920,1080)){
            double factor = 0;
            if(player.getHitbox().intersects(-200,-200,200,200) || player.getHitbox().intersects(1920,-200,200,200) || player.getHitbox().intersects(-200,1080,200,200) || player.getHitbox().intersects(1920,1080,200,200)) {
                if(player.getHitbox().intersects(-200,-200,200,200)){
                    circleX = 0;
                    circleY = 0;
                    activeOffScreenCircle = offScreenCircles[3];
                    offScreenX = 75.0/1920*gameWidth - offScreenWidth * 0.5;
                    offScreenY = 75.0/1080*gameHeight - offScreenHeight * 0.5;
                    corner.setLocation(0,0);
                }else if(player.getHitbox().intersects(1920,-200,200,200)){
                    circleX = 1770;
                    circleY = 0;
                    activeOffScreenCircle = offScreenCircles[5];
                    offScreenX = 1845.0/1920*gameWidth - offScreenWidth * 0.5;
                    offScreenY = 75.0/1080*gameHeight - offScreenHeight * 0.5;
                    corner.setLocation(1920,0);
                }else if(player.getHitbox().intersects(-200,1080,200,200)){
                    circleX = 0;
                    circleY = 930;
                    activeOffScreenCircle = offScreenCircles[1];
                    offScreenX = 75.0/1920*gameWidth - offScreenWidth * 0.5;
                    offScreenY = 1005.0/1080*gameHeight - offScreenHeight * 0.5;
                    corner.setLocation(0,1080);
                }else if(player.getHitbox().intersects(1920,1080,200,200)){
                    circleX = 1770;
                    circleY = 930;
                    activeOffScreenCircle = offScreenCircles[7];
                    offScreenX = 1845.0/1920*gameWidth - offScreenWidth * 0.5;
                    offScreenY = 1005.0/1080*gameHeight - offScreenHeight * 0.5;
                    corner.setLocation(1920,1080);
                }
                factor = corner.distance(player.getX(),player.getY()) * -0.0029 + 1.32;
            }else{
                if(player.getHitbox().intersects(0,-200,1920,200)){
                    circleX = player.getX() - (150-player.getWidth())/2;
                    circleY = 0;
                    activeOffScreenCircle = offScreenCircles[4];
                    offScreenX = rx + 0.5 * rwidth - offScreenWidth * 0.5;
                    offScreenY = 75.0/1080*gameHeight - offScreenHeight * 0.5;
                    factor = Math.abs(player.getY()) * -0.005 + 1.5;
                }else if(player.getHitbox().intersects(0,1080,1920,200)){
                    circleX = player.getX() - (150-player.getWidth())/2;
                    circleY = 930;
                    activeOffScreenCircle = offScreenCircles[0];
                    offScreenX = rx + 0.5 * rwidth - offScreenWidth * 0.5;
                    offScreenY = 1005.0/1080*gameHeight - offScreenHeight * 0.5;
                    factor = (player.getY()-1080) * -0.005 + 1;
                }else if(player.getHitbox().intersects(-200,0,200,1080)){
                    circleX = 0;
                    circleY = player.getY() - (150-player.getHeight())/2;
                    activeOffScreenCircle = offScreenCircles[2];
                    offScreenX = 75.0/1920*gameWidth - offScreenWidth * 0.5;
                    offScreenY = ry + 0.5 * rheight - offScreenHeight * 0.5;
                    factor = (Math.abs(player.getX())) * -0.0033 + 1.1667;
                }else if(player.getHitbox().intersects(1920,0,200,1080)){
                    circleX = 1770;
                    circleY = player.getY() - (150-player.getHeight())/2;
                    activeOffScreenCircle = offScreenCircles[6];
                    offScreenX = 1845.0/1920*gameWidth - offScreenWidth * 0.5;
                    offScreenY = ry + 0.5 * rheight - offScreenHeight * 0.5;
                    factor = (Math.abs(player.getX()-1920)) * -0.0033 + 1;
                }
            }
            offScreenWidth = rwidth * factor;
            offScreenHeight = rheight * factor;
            player.getRenderHitbox().setRect(offScreenX, offScreenY, offScreenWidth, offScreenHeight);
        }
    }

    public void triggerAnimation(String currentMove, double animationTime, double animationOffsetX, double animationOffsetY){
        this.currentMove = currentMove;
        this.animationTimer = animationTime;
        this.currentAnimationStartTime = animationTime;
        this.animationOffsetX = animationOffsetX/1920*gameWidth;
        this.animationOffsetY = animationOffsetY/1080*gameHeight;
    }

    public ArrayList<String> getAllPlayerMoves() {
        return allPlayerMoves;
    }

    public void setAnimationOffsetX(double animationOffsetX) {
        this.animationOffsetX = animationOffsetX;
    }

    public void setAnimationOffsetY(double animationOffsetY) {
        this.animationOffsetY = animationOffsetY;
    }

    public void setCurrentMove(String currentMove) {
        this.currentMove = currentMove;
    }
}
