package view;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.graphics.IRenderable;
import model.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

public class PlayerRenderer implements IUpdateable, IRenderable {

    private Player player;
    private boolean renderHurtboxes = true;
    private Rectangle2D renderHurtbox;
    private double rx, ry, rwidth, rheight, offScreenX, offScreenY, offScreenWidth, offScreenHeight;
    private Point corner;
    private Image[] offScreenCircles;
    private Image activeOffScreenCircle;
    private double circleX, circleY;
    private double gameWidth = Game.getConfiguration().graphics().getResolutionWidth();
    private double gameHeight = Game.getConfiguration().graphics().getResolutionHeight();

    public PlayerRenderer(Player player){
        this.player = player;
        renderHurtbox = new Rectangle2D.Double(0,0,0,0);
        corner = new Point(0,0);
        createCircleImages();
    }

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
        if(!player.getHitbox().intersects(0,0,1920,1080)){
            g.drawImage(activeOffScreenCircle, (int)(circleX/1920*gameWidth), (int)(circleY/1080*gameHeight), (int)(150.0/1920*gameWidth), (int)(150.0/1080*gameHeight), null);
        }
    }

    @Override
    public void update() {
        adjustRenderHitbox();
        if (renderHurtboxes) {
            renderHurtbox.setRect(player.getHurtbox().x / 1920 * gameWidth, player.getHurtbox().y / 1080 * gameHeight, player.getHurtbox().width / 1920 * gameWidth, player.getHurtbox().height / 1080 * gameHeight);
        }
    }

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
                    circleX = player.getX() - 50;
                    circleY = 0;
                    activeOffScreenCircle = offScreenCircles[4];
                    offScreenX = rx + 0.5 * rwidth - offScreenWidth * 0.5;
                    offScreenY = 75.0/1080*gameHeight - offScreenHeight * 0.5;
                    factor = Math.abs(player.getY()) * -0.005 + 1.5;
                }else if(player.getHitbox().intersects(0,1080,1920,200)){
                    circleX = player.getX() - 50;
                    circleY = 930;
                    activeOffScreenCircle = offScreenCircles[0];
                    offScreenX = rx + 0.5 * rwidth - offScreenWidth * 0.5;
                    offScreenY = 1005.0/1080*gameHeight - offScreenHeight * 0.5;
                    factor = (player.getY()-1080) * -0.005 + 1;
                }else if(player.getHitbox().intersects(-200,0,200,1080)){
                    circleX = 0;
                    circleY = player.getY() - 25;
                    activeOffScreenCircle = offScreenCircles[2];
                    offScreenX = 75.0/1920*gameWidth - offScreenWidth * 0.5;
                    offScreenY = ry + 0.5 * rheight - offScreenHeight * 0.5;
                    factor = (Math.abs(player.getX())) * -0.0033 + 1.1667;
                }else if(player.getHitbox().intersects(1920,0,200,1080)){
                    circleX = 1770;
                    circleY = player.getY() - 25;
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
}
