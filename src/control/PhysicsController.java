package control;

import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Entity;
import model.GravitationalObject;

import java.util.ArrayList;

import static control.Timer.dt;

public class PhysicsController extends Entity implements IUpdateable {
    private ArrayList<GravitationalObject> gravObjects;

    public PhysicsController(ArrayList<GravitationalObject> gravObjects){
        this.gravObjects = gravObjects;
    }

    @Override
    public void update() {
        for(int i = 0; i < gravObjects.size(); i++){
            if(gravObjects.get(i).isInAir()){
                gravObjects.get(i).setVerticalSpeed(gravObjects.get(i).getVerticalSpeed() + 500 * dt);
            }
        }
    }
}
