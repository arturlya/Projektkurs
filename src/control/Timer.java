package control;

import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Entity;

public class Timer extends Entity implements IUpdateable{

    private int intDT;
    public static double dt;
    private Long lastLoop, elapsedTime;

    public Timer(){
        lastLoop = System.nanoTime();
    }

    @Override
    public void update() {
        elapsedTime = System.nanoTime() - lastLoop;
        lastLoop = System.nanoTime();
        intDT = (int)((elapsedTime/1000000L) + 0.5);
        dt = ((double)intDT) / 1000;
    }
}
