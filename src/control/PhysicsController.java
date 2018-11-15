package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Entity;
import model.GravitationalObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static control.Timer.dt;


/**
 * Kontrolliert die Physik aller GravitationalObjects
 */
public class PhysicsController extends Entity implements IUpdateable {
    private ArrayList<GravitationalObject> gravObjects;
    private Collection entities;
    private int entityAmount;


    /**
     *
     */
    public PhysicsController(){
        this.gravObjects = new ArrayList<>();
        entities = Game.getEnvironment().getEntities();
        entityAmount = entities.size();
        updateGravObjects();
    }

    @Override
    public void update() {
        if(Game.getEnvironment().getEntities().size() != entityAmount){
            updateGravObjects();
        }
        for(GravitationalObject g : gravObjects){

            if(g.isInAir()){
                g.setVerticalSpeed(g.getVerticalSpeed() + 500 * dt);
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
        entityAmount = Game.getEnvironment().getEntities().size();
    }

    public ArrayList<GravitationalObject> getGravObjects() {
        return gravObjects;
    }
}
