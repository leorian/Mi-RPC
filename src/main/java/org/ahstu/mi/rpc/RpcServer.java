package org.ahstu.mi.rpc;

import org.ahstu.mi.rpc.netty.server.NettyServer;

/**
 * Created by renyueliang on 17/5/18.
 */
public abstract class RpcServer {

    private String serverIp;

    private int serverPort;

    public static RpcServer getRpcServer(){
        return NettyServer.getInstance();
    }


    public RpcServer(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public abstract void start();

    public abstract void reset();

    public abstract void close();

    public abstract void changPort();

    public abstract void changePortAndStart();

    public void setServerPort(int serverPort){
        this.serverPort=serverPort;
    }
}
