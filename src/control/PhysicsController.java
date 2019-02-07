package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Entity;
import de.gurkenlabs.litiengine.graphics.RenderType;
import model.Gambler;
import model.GravitationalObject;
import model.Maps.Map;
import model.Player;
import model.Warrior;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static control.Timer.dt;


/**
 * Klasse PhysicsController
 * Implementiert IUpdatable
 * Kontrolliert die Physik aller GravitationalObjects
 */
public class PhysicsController implements IUpdateable {

    /**Merkt sich alle GravitiationalObjevts*/
    private ArrayList<GravitationalObject> gravObjects;
    /**Merkt sich alle Spieler*/
    private ArrayList<Player> players;
    /**Merkt sich alle Entities*/
    private Collection entities;
    /**Merkt sich die Anzahl der Entities*/
    private int entityAmount;
    /**Merkt sich die Map*/
    private Map map;


    /**
     * Konstruktor des PhysicsControllers
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

    /**
     * Update-Methode des IUpdatable-Interfaces
     * Bewegt alle Spieler, die in der Luft sind passend
     * Stoppt den Fall, falls die Linien unterhalb der Spieler mit den Plattformen der Map kollidieren
     */
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
                        if(g instanceof Warrior){
                            ((Warrior) g).setHookingTimer(0);
                        }
                    }
                }
                if (!colliding) {
                    g.setInAir(true);
                }
            }
            if(g instanceof Gambler) {
                if (((Gambler) g).isTeleportedToPlayer()) {
                    while(((Gambler) g).isTeleportedToPlayer()) {
                        int randomPort = (int) (Math.random() * players.size());
                        if (players.get(randomPort) != g) {
                            g.setX(players.get(randomPort).getX() - 100);
                            g.setY(players.get(randomPort).getY()-50);
                            ((Gambler) g).setTeleportToPlayer(false);
                        }
                    }
                }
            }
            if(g.isInAir()){
                if(!(g instanceof Warrior && ((Warrior) g).isGettingHooked())) {
                    g.setVerticalSpeed(g.getVerticalSpeed() + 1000 * dt);
                }
            }else{
                g.setVerticalSpeed(0);
            }
        }
    }

    /**
     * Hält die GravitationalObject-ArrayList aktuell
     */
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

    /**
     * Hält die Player-ArrayList aktuell
     */
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

    /**
     * Speichert die aktuell verwendete Map in der 'map' Referenz
     */
    private void initializeMap(){
        Iterator itr = Game.getEnvironment().getRenderables(RenderType.BACKGROUND).iterator();
        map = (Map)itr.next();
    }

    /**@return gibt die GravitationalObject-ArrayList zurück*/
    public ArrayList<GravitationalObject> getGravObjects() {
        return gravObjects;
    }

    /**@return gibt die Player-ArrayList zurück*/
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**@return gibt die Map zurück*/
    public Map getMap(){return map;}
}
