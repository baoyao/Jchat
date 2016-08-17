package ext;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author houen.bao
 * @date Aug 15, 2016 6:02:59 PM
 */
public class BaoUtils {

    public static InetAddress getLocalHost() {
        Enumeration<NetworkInterface> netInterfaces = null;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    InetAddress ip = ips.nextElement();
                    if (ip.isSiteLocalAddress()) {
                        return ip;
                    }
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("getLocalHost Exception "+e);
        }
        return null;
    }

}
