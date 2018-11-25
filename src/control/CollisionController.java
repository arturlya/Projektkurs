package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Entity;
import de.gurkenlabs.litiengine.util.geom.Vector2D;
import model.GravitationalObject;
import model.Player;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;


public class CollisionController implements IUpdateable {

    private ArrayList<GravitationalObject> gravObjects;
    private ArrayList<Player> players;
    private int gravObjectAmount;

    public CollisionController(GameController gameController){
        Game.getLoop().attach(this);
        this.gravObjects = gameController.getGravObjects();
        players = gameController.getPlayers();
    }

    @Override
    public void update() {
        checkHurtboxToPlayerCollision();
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

    private void playerHit(Player attacker, Player defender){
        Point attackerCoords = new Point((int)attacker.getCenter().getX(), (int)attacker.getHurtbox().getCenterY());
        Vector2D smallDir = new Vector2D(attackerCoords,defender.getCenter());
        Vector2D scaledDir = smallDir.scale(100/smallDir.length());
        defender.registerHit(scaledDir, attacker.getHurtbox());
    }
}
