package org.ahstu.mi.rpc.netty.client;

import com.bozhong.common.util.StringUtil;
import org.ahstu.mi.common.MiException;
import org.ahstu.mi.common.MiLogger;
import org.ahstu.mi.common.MiResult;
import org.ahstu.mi.common.MiUtil;
import org.ahstu.mi.consumer.manager.MiResultStore;
import org.ahstu.mi.lock.InsistLock;
import org.ahstu.mi.lock.InsistLockStore;
import org.ahstu.mi.rpc.netty.InsistChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

/**
 * Created by renyueliang on 17/5/17.
 */
public class RpcNettyClientCallHandler extends InsistChannelHandlerAdapter {



    public void channelActive(ChannelHandlerContext ctx)  {

        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();

        String remoteIp = inetSocketAddress.getAddress().getHostAddress();
        int port = inetSocketAddress.getPort() ;

        String ipAndPortKey = MiUtil.ipAndPortCreateKey(remoteIp.equals("localhost") ? "127.0.0.1" : remoteIp,port);

        NettyChannelHandlerStore.add(ipAndPortKey, ctx);
        InsistLock insistLock = InsistLockStore.get(ipAndPortKey);
        if(insistLock!=null){
            synchronized (insistLock) {
                insistLock.unlock();
            }
        }

        MiLogger.record(StringUtil.format("RpcNettyClientCallHandler.channelActive ipAndPortKey:%s  insistLock:%s connect success !",
                ipAndPortKey,insistLock==null));
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        MiResult result = (MiResult)msg;
        MiResultStore.add(result);
        InsistLock insistLock = InsistLockStore.get(result.getRequestId());
        if(insistLock!=null){
            synchronized (insistLock) {
                insistLock.unlock();
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

        InsistLock insistLock = InsistLockStore.get(ipAndPortKey);

        if(insistLock!=null){
            insistLock.unlock();
        }
        ctx.close();
        throw new MiException(cause.getMessage(),cause);

    }
}
