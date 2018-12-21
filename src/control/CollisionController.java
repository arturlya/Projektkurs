package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Entity;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.util.geom.Vector2D;
import model.GravitationalObject;
import model.Maps.Map;
import model.Player;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;


public class CollisionController implements IUpdateable {

    private ArrayList<GravitationalObject> gravObjects;
    private ArrayList<Player> players;
    private int gravObjectAmount;
    private Map map;

    public CollisionController(GameController gameController){
        Game.getLoop().attach(this);
        this.gravObjects = gameController.getGravObjects();
        players = gameController.getPlayers();
        initializeMap();
    }

    @Override
    public void update() {
        checkHurtboxToPlayerCollision();
        checkPlayerOffScreen();
    }

    private void checkPlayerOffScreen(){
        for(Player player : players){
            if(!player.getHitbox().intersects(Game.getScreenManager().getBounds())){
                player.spawn(getFarthestSpawnpointFromPlayers());
            }
        }
    }

    private void checkHurtboxToPlayerCollision(){
        for(Player attacker : players){
            for(Player defender : players){
                if(attacker != defender){
                    if(attacker.getHurtbox().isHurting()) {
                        if (attacker.getHurtbox().intersects(defender.getHitbox())) {
                            playerHit(attacker, defender);
                        }
                    }
                }
            }
        }
    }

    public Point getFarthestSpawnpointFromPlayers(){
        int pointIndex = 0;
        double pointDistance = 0;

       for(int i = 0; i < map.getSpawnpoints().size(); i++){
           if(Game.getScreenManager().getBounds().contains(map.getSpawnpoints().get(i))) {
               double playerDistance = 0;
               for (Player player : players) {
                   if (!player.getHitbox().intersects(Game.getScreenManager().getBounds())) {
                       double distance = player.getCenter().distance(map.getSpawnpoints().get(i));
                       if (playerDistance < distance) {
                           playerDistance = distance;
                       }
                   }
               }
               if (playerDistance > pointDistance) {
                   pointDistance = playerDistance;
                   pointIndex = i;
               }
           }
       }

        return map.getSpawnpoints().get(pointIndex);
    }

    private void initializeMap(){
        Iterator itr = Game.getEnvironment().getRenderables(RenderType.BACKGROUND).iterator();
        map = (Map)itr.next();
    }

    private void playerHit(Player attacker, Player defender){
        Point attackerCoords = new Point((int)attacker.getCenter().getX(), (int)attacker.getHurtbox().getCenterY());
        Vector2D smallDir = new Vector2D(attackerCoords,defender.getCenter());
        Vector2D scaledDir = smallDir.scale(100/smallDir.length());
        defender.registerHit(scaledDir, attacker.getHurtbox());
    }
}
