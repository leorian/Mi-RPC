package org.ahstu.mi.test;

import org.ahstu.mi.rpc.netty.server.NettyServer;

/**
 * Created by renyueliang on 17/5/23.
 */
public class NettyServerTest {

    public static void main(String[] args)throws Throwable{

        NettyServer.getInstance().start();

        System.in.read();

    }

}
