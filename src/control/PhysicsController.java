package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Entity;
import de.gurkenlabs.litiengine.graphics.RenderType;
import model.GravitationalObject;
import model.Maps.Map;
import model.Player;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static control.Timer.dt;


/**
 * Kontrolliert die Physik aller GravitationalObjects
 */
public class PhysicsController implements IUpdateable {
    private ArrayList<GravitationalObject> gravObjects;
    private ArrayList<Player> players;
    private Collection entities;
    private int entityAmount;
    private Map map;


    /**
     *
     */
    public PhysicsController(){
        Game.getLoop().attach(this);
        this.gravObjects = new ArrayList<>();
        entities = Game.getEnvironment().getEntities();
        entityAmount = entities.size();
        players = new ArrayList<>();
        initializeMap();
        updateGravObjects();
    }

    @Override
    public void update() {
        if(Game.getEnvironment().getEntities().size() != entityAmount){
            updateGravObjects();
        }
        for(GravitationalObject g : gravObjects){

            if(g.getVerticalSpeed() >= 0) {
                boolean colliding = false;
                for (int i = 0; i < map.getLines().size() && !colliding; i++) {
                    if (g.getDownLines()[0].intersectsLine(map.getLines().get(i)) || g.getDownLines()[1].intersectsLine(map.getLines().get(i))) {
                        colliding = true;
                        g.setY(map.getLines().get(i).getY1() - g.getHeight());
                        g.setInAir(false);
                    }
                }
                if (!colliding) {
                    g.setInAir(true);
                }
            }
            if(g.isInAir()){
                g.setVerticalSpeed(g.getVerticalSpeed() + 1000 * dt);
            }else{
                g.setVerticalSpeed(0);
            }
        }
    }

    private void updateGravObjects(){
        Iterator itr = Game.getEnvironment().getEntities().iterator();
        while(itr.hasNext()){
            Entity ent = (Entity)itr.next();
            if(ent instanceof GravitationalObject){
                if(!gravObjects.contains(ent)){
                    gravObjects.add((GravitationalObject)ent);
                }
            }
        }
        gravObjects.removeIf(g -> !Game.getEnvironment().getEntities().contains(g));
        entityAmount = Game.getEnvironment().getEntities().size();
        updatePlayers();
    }

    private void updatePlayers(){
        for (GravitationalObject g : gravObjects) {
            if(g instanceof Player){
                if(!players.contains(g)){
                    players.add((Player)g);
                    ((Player)g).spawn(map.getSpawnpoints().get(((Player)g).getPlayerNumber()));
                }
            }
        }
        players.removeIf(p -> !gravObjects.contains(p));
    }

    private void initializeMap(){
        Iterator itr = Game.getEnvironment().getRenderables(RenderType.BACKGROUND).iterator();
        map = (Map)itr.next();
        System.out.println(map);
    }

    public ArrayList<GravitationalObject> getGravObjects() {
        return gravObjects;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
