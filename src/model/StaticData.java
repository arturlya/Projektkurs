package model;

import de.gurkenlabs.litiengine.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

public final class StaticData {
    public static int ScreenWidth = Toolkit.getDefaultToolkit().getScreenSize().width,ScreenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static String ip;
    public static int moveUp = KeyEvent.VK_W;
    public static int moveDown = KeyEvent.VK_S;
    public static int moveLeft = KeyEvent.VK_A;
    public static int moveRight = KeyEvent.VK_D;
    public static int jump = KeyEvent.VK_UP;
    public static int normalAttack = KeyEvent.VK_LEFT;
    public static int specialAttack = KeyEvent.VK_DOWN;
    public static float audioVolume = Game.getConfiguration().sound().getMusicVolume()*100;


    public StaticData() {
        if (Game.getConfiguration().graphics().isFullscreen()) {
            ScreenWidth = (int)Game.getConfiguration().graphics().getResolution().getWidth();
            ScreenHeight = (int)Game.getConfiguration().graphics().getResolution().getHeight();
        }else{
            ScreenWidth = Game.getConfiguration().graphics().getResolutionWidth();
            ScreenHeight = Game.getConfiguration().graphics().getResolutionHeight();
        }
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException err) {
            System.err.println("Couldn't detect ip-address");
        }
    }
}
