package control;

import model.StaticData;

import java.awt.event.KeyEvent;
import java.util.prefs.Preferences;

public class Config {

    public static Preferences pref;

    public Config() {
        pref = Preferences.userNodeForPackage(Config.class);
        StaticData.lookUp = pref.getInt("lookUp",KeyEvent.VK_W);
        StaticData.lookDown = pref.getInt("lookDown",KeyEvent.VK_S);
        StaticData.moveLeft = pref.getInt("moveLeft",KeyEvent.VK_A);
        StaticData.moveRight = pref.getInt("moveRight",KeyEvent.VK_D);
        StaticData.jump = pref.getInt("jump",KeyEvent.VK_UP);
        StaticData.normalAttack = pref.getInt("normalAttack",KeyEvent.VK_LEFT);
        StaticData.specialAttack = pref.getInt("specialAttack",KeyEvent.VK_DOWN);
    }
}
