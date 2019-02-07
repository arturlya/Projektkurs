package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Entity;

/**
 * Klasse Timer
 * Implementiert IUpdatable
 * enthält das statische dt-Attribut, das für Zeitmessung und ähnliches verwendet wird
 */
public class Timer implements IUpdateable{

    private int intDT;
    public static double dt;
    private Long lastLoop, elapsedTime;

    public Timer(){
        Game.getLoop().attach(this);
        lastLoop = System.nanoTime();
    }

    /**
     * Update-Methode des IUpdatable-Interfaces
     * Aktualisiert das dt-Attribut
     */
    @Override
    public void update() {
        elapsedTime = System.nanoTime() - lastLoop;
        lastLoop = System.nanoTime();
        intDT = (int)((elapsedTime/1000000L) + 0.5);
        dt = ((double)intDT) / 1000;
    }
}
