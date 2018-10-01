package util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConnectionUtil {



    public static InetAddress getIP() {
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }
}
