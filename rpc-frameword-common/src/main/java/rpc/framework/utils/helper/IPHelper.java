package rpc.framework.utils.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.*;
import java.util.Enumeration;
import java.util.List;

/**
 * @program rpc-framework-jc
 * @description:
 * @author: JC
 * @create: 2020/08/03 21:09
 */
@Slf4j
public class IPHelper {
    private static String hostIp = StringUtils.EMPTY;

    /**
     * 通过 获取系统所有的networkInterface网络接口 然后遍历 每个网络下的InterfaceAddress组。
     * 获得符合 <code>InetAddress instanceof Inet4Address</code> 条件的一个IpV4地址
     * @return 获取本机Ip
     */
    public static String localIp() {
        return hostIp;
    }

    /**
     * 获取主机第一个有效ip,如果没有效ip，返回空串
     * @return
     */
    public static String getHostFirstIp() {
        return hostIp;
    }
    static {

        String ip = null;
        Enumeration allNetInterfaces;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                List<InterfaceAddress> InterfaceAddress = netInterface.getInterfaceAddresses();
                for (InterfaceAddress add : InterfaceAddress) {
                    InetAddress Ip = add.getAddress();
                    if (Ip != null && Ip instanceof Inet4Address) {
                        if (StringUtils.equals(Ip.getHostAddress(), "127.0.0.1")) {
                            continue;
                        }
                        ip = Ip.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            log.warn("获取本机Ip失败:异常信息:" + e.getMessage());
            throw new RuntimeException(e);
        }
        hostIp = ip;
    }

    public static String getRealIp() {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP

        try {
            Enumeration<NetworkInterface> netInterfaces =
                    NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            boolean finded = false;// 是否找到外网IP
            while (netInterfaces.hasMoreElements() && !finded) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = address.nextElement();
                    if (!ip.isSiteLocalAddress()
                            && !ip.isLoopbackAddress()
                            && !ip.getHostAddress().contains(":")) {// 外网IP
                        netip = ip.getHostAddress();
                        finded = true;
                        break;
                    } else if (ip.isSiteLocalAddress()
                            && !ip.isLoopbackAddress()
                            && !ip.getHostAddress().contains(":")) {// 内网IP
                        localip = ip.getHostAddress();
                    }
                }
            }

            if (netip != null && !"".equals(netip)) {
                return netip;
            } else {
                return localip;
            }
        } catch (SocketException e) {
            log.warn("获取本机Ip失败:异常信息:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Test
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println(localIp());
        System.out.println(getRealIp());
        System.out.println(getHostFirstIp());
    }
}
