package model;

import de.gurkenlabs.litiengine.Game;

import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Klasse für die Speicherung wichtiger Daten
 * Gibt allgemeine und für alle Klassen zugängliche Daten weiter
 *
 */
public final class StaticData {

    /** Bildschirm-Breite und Höhe*/
    public static int ScreenWidth,ScreenHeight;
    /** Anpassungs-Multiplikator*/
    public static float ScreenWidthMultiplier,ScreenHeightMultiplier;
    /** Lokale IP*/
    public static String ip;
    /**
     * Integer-Werte für die gespeicherte Tastenbelegung
     */
    public static int lookUp;
    public static int lookDown;
    public static int moveLeft;
    public static int moveRight;
    public static int jump;
    public static int normalAttack;
    public static int specialAttack;
    /** Sound-Lautstärke*/
    public static float audioVolume = Game.getConfiguration().sound().getMusicVolume()*100;

    /**
     * Statischer Konstruktor
     */
    static {
        if (Game.getConfiguration().graphics().isFullscreen()) {
            ScreenWidth = (int)Game.getConfiguration().graphics().getResolution().getWidth();
            ScreenHeight = (int)Game.getConfiguration().graphics().getResolution().getHeight();
        }else{
            ScreenWidth = Game.getConfiguration().graphics().getResolutionWidth();
            ScreenHeight = Game.getConfiguration().graphics().getResolutionHeight();
        }
        System.out.println(ScreenWidth);
        System.out.println(ScreenHeight);
        ScreenWidthMultiplier = ScreenWidth/1920f;
        ScreenHeightMultiplier = ScreenHeight/1080f;
        System.out.println(ScreenWidthMultiplier);
        System.out.println(ScreenHeightMultiplier);
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException err) {
            System.err.println("Couldn't detect ip-address");
        }
    }
}
