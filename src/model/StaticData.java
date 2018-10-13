package model;

import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public final class StaticData {
    static int ScreenWidth = Toolkit.getDefaultToolkit().getScreenSize().width,ScreenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
    static String ip;
    public StaticData() {
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException err) {
            System.err.println("Couldn't detect ip-address");
        }
    }

    public static String getIp() {
        return ip;
    }

    public static int getScreenWidth() {
        return ScreenWidth;
    }

    public static int getScreenHeight() {
        return ScreenHeight;
    }
}
