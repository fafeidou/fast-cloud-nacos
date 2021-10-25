package fast.cloud.nacos.cat.monitor.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class HostUtil {

    private final static Logger logger = LoggerFactory.getLogger(HostUtil.class);

    private static Map<String, String> map = new HashMap<String, String>();

    static {
        map.put("ip", getLinuxLocalIp());
    }

    public static String getHostIp() {
        String ip = map.get("ip");
        if (ip == null) {
            String ip1 = getLinuxLocalIp();
            map.put("ip", ip1);
            return ip1;
        }
        return ip;
    }

    /**
     * 获取Linux下的IP地址
     */
    private static String getLinuxLocalIp() {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:")
                                    && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ip = "127.0.0.1";
            logger.error("get ip address error :" + ex.getMessage());
        }
        return ip;
    }

}