package org.ahstu.mi.rpc.netty.client;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by renyueliang on 17/5/17.
 */
public class NettyChannelHandlerStore {

    private static Map<String, ChannelHandlerContext> channelHandlerContextMap = new HashMap<String, ChannelHandlerContext>();

    public static void add(String key, ChannelHandlerContext chc) {
        channelHandlerContextMap.put(key, chc);
    }

    public static ChannelHandlerContext get(String key) {

        ChannelHandlerContext chc = channelHandlerContextMap.get(key);

        return chc;

    }

    public static void remove(String key) {
        channelHandlerContextMap.remove(key);
    }

}
