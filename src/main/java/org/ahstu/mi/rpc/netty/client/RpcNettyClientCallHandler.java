package org.ahstu.mi.rpc.netty.client;

import org.ahstu.mi.common.StringUtil;
import org.ahstu.mi.common.MiException;
import org.ahstu.mi.common.MiLogger;
import org.ahstu.mi.common.MiResult;
import org.ahstu.mi.common.MiUtil;
import org.ahstu.mi.consumer.manager.MiResultStore;
import org.ahstu.mi.lock.MiLock;
import org.ahstu.mi.lock.MiLockStore;
import org.ahstu.mi.rpc.netty.MiChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

/**
 * Created by renyueliang on 17/5/17.
 */
public class RpcNettyClientCallHandler extends MiChannelHandlerAdapter {



    public void channelActive(ChannelHandlerContext ctx)  {

        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();

        String remoteIp = inetSocketAddress.getAddress().getHostAddress();
        int port = inetSocketAddress.getPort() ;

        String ipAndPortKey = MiUtil.ipAndPortCreateKey(remoteIp.equals("localhost") ? "127.0.0.1" : remoteIp,port);

        NettyChannelHandlerStore.add(ipAndPortKey, ctx);
        MiLock miLock = MiLockStore.get(ipAndPortKey);
        if(miLock!=null){
            synchronized (miLock) {
                miLock.unlock();
            }
        }

        MiLogger.record(StringUtil.format("RpcNettyClientCallHandler.channelActive ipAndPortKey:%s  miLock:%s connect success !",
                ipAndPortKey,miLock==null));
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        MiResult result = (MiResult)msg;
        MiResultStore.add(result);
        MiLock miLock = MiLockStore.get(result.getRequestId());
        if(miLock!=null){
            synchronized (miLock) {
                miLock.unlock();
            }
        }
    }
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)	throws Exception {

        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();

        String remoteIp = inetSocketAddress.getAddress().getHostAddress();
        int port = inetSocketAddress.getPort() ;
        String ipAndPortKey = MiUtil.ipAndPortCreateKey(remoteIp.equals("localhost") ? "127.0.0.1" : remoteIp,port);

        NettyChannelHandlerStore.remove(ipAndPortKey);

        MiLock miLock = MiLockStore.get(ipAndPortKey);

        if(miLock!=null){
            miLock.unlock();
        }
        ctx.close();
        throw new MiException(cause.getMessage(),cause);

    }
}
