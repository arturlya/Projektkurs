package modell;

import de.gurkenlabs.litiengine.gui.screens.GameScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class IngameScreen extends GameScreen {

    private ArrayList<GravitationalObject> gravObjects;

    public IngameScreen(){
        gravObjects = new ArrayList<>();
    }

    @Override
    public void render(final Graphics2D g){
        super.render(g);

        for(int i = 0; i < gravObjects.size(); i++){
            if(gravObjects.get(i) instanceof Player){
                if(((Player) gravObjects.get(i)).getHurtbox().isHurting()){
                    g.setColor(new Color(255,0,0,100));
                }else{
                    g.setColor(new Color(0,255,0,100));
                }
                g.fill(((Player) gravObjects.get(i)).getHurtbox());
            }
            g.setColor(new Color(70,120,255));
            if(gravObjects.get(i) instanceof Player)if(((Player) gravObjects.get(i)).isShieldActive()) g.setColor(new Color(150,150,150));
            g.fill(gravObjects.get(i).getHitbox());
        }
    }

    public void addGravObject(GravitationalObject g){
        gravObjects.add(g);
    }

    public void removeGravObject(GravitationalObject g){
        gravObjects.remove(g);
    }
}
