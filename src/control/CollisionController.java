package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.util.geom.Vector2D;
import model.*;
import model.Maps.Map;

import java.awt.*;
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
        checkProjectileCollision();
        checkPlayerOffScreen();
        if(gravObjectAmount != gravObjects.size()){
            updateProjectiles();
        }
        gravObjectAmount = gravObjects.size();
    }

    private void checkPlayerOffScreen(){
        for(Player player : players){
            if(!player.getHitbox().intersects(-200,-200,2320,1480)){
                player.setStocks(player.getStocks()-1);
                if(player.getStocks() > 0) {
                    player.spawn(getFarthestSpawnpointFromPlayers());
                }else{
                    Game.getEnvironment().removeRenderable(player);
                    Game.getEnvironment().remove(player);
                    player.removeRenderer();
                }
            }
        }
    }

    private void checkHurtboxToPlayerCollision(){
        Player gamblerDefender = null;
        boolean gamblerHit = false;
        for(Player attacker : players){
            for(Player defender : players){
                if(attacker != defender){
                    if(attacker.getHurtbox().isHurting()) {
                        if (attacker.getHurtbox().intersects(defender.getHitbox())) {
                            playerHit(attacker, defender);
                            gamblerDefender = defender;
                            gamblerHit = true;
                        }
                    }
                }
            }
            if(gamblerHit) {
                if (attacker instanceof Gambler && ((Gambler) attacker).getSlotCooldown() <= 0 && ((Gambler) attacker).getBuffTimer() <= 0) {
                    if (attacker.getHurtbox().intersects(gamblerDefender.getHitbox())) {
                        ((Gambler) attacker).addSlotValue(1);
                        System.out.println("Attack hitted");
                    } else {
                        ((Gambler) attacker).addSlotValue(-1);
                    }
                    ((Gambler) attacker).setSlotCooldown(1);
                }
            }
        }
    }

    private void checkProjectileCollision(){
        for(Projectile projectile : projectiles){
            if(!projectile.isInAir()){
                if(projectile instanceof Warrior.GrapplingHook){
                    ((Warrior)projectile.getPlayer()).pullToHook();
                }
                Game.getEnvironment().remove(projectile);
                Game.getEnvironment().removeRenderable(projectile);
                projectile.getPlayer().setProjectile(null);
            }else{
                for(Player player : players){
                    if(player != projectile.getPlayer()) {
                        if (projectile.getHurtbox().intersects(player.getHitbox())) {
                            System.out.println(2);
                            Vector2D smallDir = new Vector2D(projectile.getCenter(), player.getCenter());
                            Vector2D scaledDir = smallDir.scale(100 / smallDir.length());
                            player.registerHit(scaledDir, projectile.getHurtbox().getDamage(), projectile.getHurtbox().getKnockback());
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
        defender.registerHit(scaledDir, attacker.getHurtbox().getDamage(), attacker.getHurtbox().getKnockback());
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
