package org.ahstu.mi.common;

import java.io.Serializable;

/**
 * Created by renyueliang on 17/5/15.
 */
public class MiSendDTO implements Serializable {

    private static final long serialVersionUID = -8415740210572840701L;

    private String requestId;

    private String interfaceName;

    private String method;

    private Object[] args;

    private Class<?>[] argsType;

    private String serverIp;

    private int port;

    private String version;

    private String group;

    private long clientTimeout;

    private String clientIp;

    public MiSendDTO(String requestId, String interfaceName,
                     String method, Object[] args,
                     Class<?>[] argsType, String serverIp, int port,
                     String group, long clientTimeout, String clientIp,
                     String version) {
        this.requestId = requestId;
        this.interfaceName=interfaceName;
        this.method = method;
        this.args = args;
        this.argsType=argsType;
        this.serverIp=serverIp;
        this.port=port;
        this.group=group;
        this.clientTimeout=clientTimeout;
        this.clientIp=clientIp;
        this.version=version;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public Class<?>[] getArgsType() {
        return argsType;
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getPort() {
        return port;
    }

    public String getVersion() {
        return version;
    }

    public String getGroup() {
        return group;
    }

    public long getClientTimeout() {
        return clientTimeout;
    }

    public String getClientIp() {
        return clientIp;
    }
}
