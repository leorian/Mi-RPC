package org.ahstu.mi.common;

import org.ahstu.mi.rpc.RpcCallExcutor;
import org.ahstu.mi.rpc.netty.RpcNettyCallExcutor;

/**
 * Created by renyueliang on 17/5/17.
 */
public class RemoteExcutorFactory {

    public static RpcCallExcutor getRpcExcutor(){
        return RpcNettyCallExcutor.getInstance();
    }
}
