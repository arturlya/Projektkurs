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

/**
 * Klasse CollisionController
 * Implementiert von IUpdatable
 * Kontrolliert die Kollision von Spielern und ihren Angriffen
 */
public class CollisionController implements IUpdateable {

    /**Merkt sich alle GravitationalObjects des Spiels*/
    private ArrayList<GravitationalObject> gravObjects;
    /**Merkt sich alle Player des Spiels*/
    private ArrayList<Player> players;
    /**Merkt sich alle Projectiles des Spiels*/
    private ArrayList<Projectile> projectiles;
    /**Merkt sich die Anzahl der GravObjects im Spiel*/
    private int gravObjectAmount;
    /**Merkt sich die Map*/
    private Map map;

    /**
     * Konstruktor der Klasse CollisionController
     * @param physicsController der PhysicsController der Spiels
     */
    public CollisionController(PhysicsController physicsController){
        Game.getLoop().attach(this);
        gravObjects = physicsController.getGravObjects();
        players = physicsController.getPlayers();
        gravObjectAmount = gravObjects.size();
        projectiles = new ArrayList<>();
        initializeMap();
    }

    /**
     * Update des Interfaces IUpdateable
     */
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

    /**
     * Schaut, ob Spieler außerhalb des Bildschirms sind, falls ja, wird deren Spawn-Methode aufgerufen
     */
    private void checkPlayerOffScreen(){
        for(Player player : players){
            if(!player.getHitbox().intersects(-200,-200,2320,1480)){
                if(player.getStocks() > 0) {
                    player.spawn(getFarthestSpawnpointFromPlayers());
                }
            }

        }
    }

    /**
     * Prüft, ob die Hurtbox eines 'Attackers' mit der Hitbox eines 'Defenders' kollidieren, falls ja, wird ein Hit ausgeführt
     */
    private void checkHurtboxToPlayerCollision(){
        Player gamblerDefender = null;
        boolean gamblerHit = false;
        for(Player attacker : players){
            for(Player defender : players){
                if(attacker != defender){
                    if(attacker.getHurtbox().isHurting()) {
                        if (attacker.getHurtbox().intersects(defender.getHitbox())) {
                            if(!defender.isShieldActive()) {
                                playerHit(attacker, defender);
                                gamblerDefender = defender;
                                gamblerHit = true;
                            }
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

    /**
     * Prüft die Kollision zwischen Projektilen und Spielern
     */
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
                            if(!player.isShieldActive()) {
                                Vector2D smallDir = new Vector2D(projectile.getCenter(), player.getCenter());
                                Vector2D scaledDir = smallDir.scale(100 / smallDir.length());
                                player.registerHit(scaledDir, projectile.getHurtbox().getDamage(), projectile.getHurtbox().getKnockback());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Ermittelt einen passenden Spawnpunkt für einen Spieler, sodass er möglichst weit entfernt von anderen spawnt
     * @return Der ermittelte Spawnpunkt
     */
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

    /**
     * Speichert die aktuell verwendete Map in der 'map' Referenz
     */
    private void initializeMap(){
        Iterator itr = Game.getEnvironment().getRenderables(RenderType.BACKGROUND).iterator();
        map = (Map)itr.next();
    }

    /**
     * Ermittelt den Vektor mit dem ein Spieler zurückgeschlagen wird und führt mit diesem die 'registerHit'-Methode vom Spieler aus
     * @param attacker der Spieler, von dem der Angriff ausgeht
     * @param defender der Spieler, der von dem Angriff getroffen wird
     */
    private void playerHit(Player attacker, Player defender){
        Point attackerCoords = new Point((int)attacker.getCenter().getX(), (int)attacker.getHurtbox().getCenterY());
        Vector2D smallDir = new Vector2D(attackerCoords,defender.getCenter());
        Vector2D scaledDir = smallDir.scale(100/smallDir.length());
        defender.registerHit(scaledDir, attacker.getHurtbox().getDamage(), attacker.getHurtbox().getKnockback());
    }

    /**
     * sorgt dafür, dass die projectile ArrayList immer alle im Spiel vorhandenen Projektile enthält
     */
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
