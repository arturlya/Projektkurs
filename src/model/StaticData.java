package model;

import de.gurkenlabs.litiengine.Game;

import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public final class StaticData {
    public static int ScreenWidth = Toolkit.getDefaultToolkit().getScreenSize().width,ScreenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static String ip;
    public static int moveUp;
    public static int moveDown;
    public static int moveLeft;
    public static int moveRight;
    public static int jump;
    public static int normalAttack;
    public static int specialAttack;
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
