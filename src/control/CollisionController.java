package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Entity;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.util.geom.Vector2D;
import model.GravitationalObject;
import model.Maps.Map;
import model.Player;
import model.Projectile;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;


public class CollisionController implements IUpdateable {

    private ArrayList<GravitationalObject> gravObjects;
    private ArrayList<Player> players;
    private ArrayList<Projectile> projectiles;
    private int gravObjectAmount;
    private Map map;

    public CollisionController(PhysicsController physicsController){
        Game.getLoop().attach(this);
        gravObjects = physicsController.getGravObjects();
        players = physicsController.getPlayers();
        gravObjectAmount = gravObjects.size();
        projectiles = new ArrayList<>();
        initializeMap();
    }

    @Override
    public void update() {
        checkHurtboxToPlayerCollision();
        checkPlayerOffScreen();
        if(gravObjectAmount != gravObjects.size()){
            updateProjectiles();
        }
        gravObjectAmount = gravObjects.size();
    }

    private void checkPlayerOffScreen(){
        for(Player player : players){
            if(!player.getHitbox().intersects(-200,-200,2320,1480)){
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

    private void updateProjectiles(){
        for(GravitationalObject g : gravObjects){
            if(g instanceof Projectile){
                if(!projectiles.contains(g)) {
                    projectiles.add((Projectile) g);
                }
            }
        }
        projectiles.removeIf(p -> !gravObjects.contains(p));
    }
}
