package org.ahstu.mi.common;

import com.alibaba.fastjson.JSON;
import com.bozhong.common.util.StringUtil;
import org.ahstu.mi.consumer.MiConsumerMeta;
import org.ahstu.mi.module.ServiceMeta;
import org.ahstu.mi.rpc.netty.server.NettyServer;
import org.ahstu.mi.zk.MiZkClient;
import org.ahstu.mi.zk.api.IZkClient;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by renyueliang on 17/5/16.
 */
public class MiUtil {

    private static String ip = null;
    private static final ReentrantLock reentrantLock = new ReentrantLock();
    private static volatile boolean MI_START_SUCCESS=false;

    public static String ipAndPortCreateKey(ServiceMeta serviceMeta) {
        return ipAndPortCreateKey(serviceMeta.getIp(), serviceMeta.getPort());
    }

    public static String ipAndPortCreateKey(String ip, int port) {
        return ip + MiConstants.LOWER_HORIZONTAL_LINE + port;
    }

    public static String serviceGroupVersionCreateKey(ServiceMeta serviceMeta) {
        return serviceGroupVersionCreateKey(serviceMeta.getInterfaceName(), serviceMeta.getGroup(), serviceMeta.getVersion());
    }

    public static String serviceGroupVersionCreateKey(String serviceName, String group, String version) {
        return serviceName + MiConstants.LOWER_HORIZONTAL_LINE + group + MiConstants.LOWER_HORIZONTAL_LINE + version;

    }

    public static ServiceMeta getByIpPortKey(String ipPortKey) {

        if (StringUtil.isBlank(ipPortKey)) {
            throw new MiException(MiError.MI_IPPORTKEY_ISNULL);
        }

        String[] arr = ipPortKey.split(MiConstants.LOWER_HORIZONTAL_LINE);

        if (arr.length < 2) {
            throw new MiException(MiError.MI_IPPORTKEY_ISNULL);
        }

        ServiceMeta serviceMeta = new ServiceMeta();

        serviceMeta.setIp(arr[0]);
        serviceMeta.setPort(Integer.valueOf(arr[1]));

        return serviceMeta;
    }

    public static String getRequestId() {

        return UUID.randomUUID().toString();

    }


    public static String getLocalIpForReal() {

        String localHostIp = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                        localHostIp = inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            MiLogger.record(ex.getMessage(), ex);
        }

        return localHostIp;

    }

    public static void initIp() {
        if (ip == null) {
            ip = getLocalIpForReal();
        }
    }

    public static String geLocalIp() {
        initIp();
        return ip;
    }

    public static void insistStartUp() {
        if(MI_START_SUCCESS){
            return ;
        }

        try {
            reentrantLock.lock();
            if(MI_START_SUCCESS){
                return ;
            }
            MiLogger.record(StringUtil.format("**************** insist start up ****************"));
            NettyServer.getRpcServer().start();
            //--/insist/consumer/forservice/group/com.xxx.service/version/ip
            //--/insist/prodiver/forservice/group/com.xxx.service/version/ip

            IZkClient zkClient = MiZkClient.getInstance();
            zkClient.connect();

            String providerPath = getBaseProviderZkPath();
            String providerServicePath = getProviderZkPath();
            if (!zkClient.has(providerPath)) {
                zkClient.addNode(providerPath, false);
            }
            if (!zkClient.has(providerServicePath)) {
                zkClient.addNode(providerServicePath, false);
            }

            String consumerPath = getBaseConsumerZkPath();
            String consumerServicePath = getConsumerZkPath();
            if (!zkClient.has(consumerPath)) {
                zkClient.addNode(consumerPath, false);
            }
            if (!zkClient.has(consumerServicePath)) {
                zkClient.addNode(consumerServicePath, false);
            }
            MI_START_SUCCESS=true;
            MiLogger.record(StringUtil.format("**************** insist end ****************"));

        } catch (Throwable e) {
            MiLogger.record("MiZkClient start up error ! errorCode:"+e.getMessage(), e);
            if(e.getMessage().equals(MiError.MI_ZK_HOST_ISNULL.getErrorCode())){
                try {
                    Thread.sleep(3000l);
                    insistStartUp();
                }catch (Throwable e1){
                    MiLogger.record("waiting 3s MiZkClient restart up error ! errorCode:"+e1.getMessage(), e1);
                }
            }else{
                throw new MiException(e.getMessage(), e);
            }

        }finally {
            reentrantLock.unlock();
        }


    }

    public static String getBaseProviderZkPath() {
        return MiConstants.MI_ROOT_PATH + MiConstants.MI_ZK_SLASH +
                MiConstants.MI_ZK_PRODIVER;
    }

    public static String getBaseConsumerZkPath() {
        return MiConstants.MI_ROOT_PATH
                + MiConstants.MI_ZK_SLASH + MiConstants.MI_ZK_CONSUMER;

    }

    public static String getProviderZkPath() {
        return getBaseProviderZkPath() + MiConstants.MI_ZK_SLASH +
                MiConstants.MI_ZK_FORSERVICE;
    }

    public static String getConsumerZkPath() {
        return getBaseConsumerZkPath()
                + MiConstants.MI_ZK_SLASH +
                MiConstants.MI_ZK_FORSERVICE;

    }

    public static String getServiceNameGroupVersionZkPath(String serviceName, String group, String version) {
        return getProviderZkPath() + MiConstants.MI_ZK_SLASH + group + MiConstants.MI_ZK_SLASH +
                serviceName + MiConstants.MI_ZK_SLASH + version;
    }

    public static ServiceMeta jsonToServiceMeta(String json) {
        return JSON.parseObject(json, ServiceMeta.class);
    }

    public static String serviceMetaToJson(ServiceMeta serviceMeta) {
        return JSON.toJSONString(serviceMeta);
    }

    public static MiConsumerMeta jsonToClientMeta(String json) {
        return JSON.parseObject(json, MiConsumerMeta.class);
    }

    public static String clientMetaToJson(MiConsumerMeta clientMeta) {
        return JSON.toJSONString(clientMeta);
    }

    public static boolean firstCharCheck(String path, String hasThisChar) {

        if (path == null || "".equals(path)) {
            return false;
        }

        return path.indexOf(hasThisChar) == 0;


    }

    /**
     * @param path
     * @return
     */
    public static String firstAddChar(String path) {
        return firstAddChar(path, MiConstants.MI_ZK_SLASH);
    }

    /**
     * @param path
     * @param addChar
     * @return
     */
    public static String firstAddChar(String path, String addChar) {

        if (!firstCharCheck(path, addChar)) {
            return addChar + path;
        }

        return path;
    }


    public static void createAllProviderPathNode(String serviceName, String group, String version) {
        IZkClient zkClient = MiZkClient.getInstance();
        String groupPath = MiUtil.getProviderZkPath() + MiConstants.MI_ZK_SLASH + group;
        String serviceGroupPath = groupPath + MiConstants.MI_ZK_SLASH + serviceName;
        String versionServiceGroupPath = serviceGroupPath + MiConstants.MI_ZK_SLASH + version;
        try {

            if (!zkClient.has(groupPath)) {
                zkClient.addNode(groupPath, false);
            }
            if (!zkClient.has(serviceGroupPath)) {
                zkClient.addNode(serviceGroupPath, false);
            }
            if (!zkClient.has(versionServiceGroupPath)) {
                zkClient.addNode(versionServiceGroupPath, false);
            }
            MiLogger.record(StringUtil.format("InsistUtil.createAllProviderPathNode success ! path:%s ", versionServiceGroupPath));
        } catch (Throwable e) {
            MiLogger.record(StringUtil.format("InsistUtil.createAllProviderPathNode error ! versionServiceGroupPath:%s errorCode:%s"
                    , versionServiceGroupPath,
                    e.getMessage()
            ), e);

        }
    }

}
