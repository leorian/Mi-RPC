package org.ahstu.mi.rpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.internal.InternalThreadLocalMap;
import java.net.SocketAddress;
import java.util.Map;

/**
 * Created by xiezg@317hu.com on 2017/7/14 0014.
 */
public abstract class InsistChannelHandlerAdapter  extends ChannelInboundHandlerAdapter {
    boolean added;

    public InsistChannelHandlerAdapter() {
    }

    public boolean isSharable() {
        Class clazz = this.getClass();
        Map cache = InternalThreadLocalMap.get().handlerSharableCache();
        Boolean sharable = (Boolean)cache.get(clazz);
        if(sharable == null) {
            sharable = Boolean.valueOf(clazz.isAnnotationPresent(Sharable.class));
            cache.put(clazz, sharable);
        }

        return sharable.booleanValue();
    }

    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    }

    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }

    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
    }

    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.fireChannelRead(msg);
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        ctx.fireUserEventTriggered(evt);
    }

    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelWritabilityChanged();
    }

    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }

    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }

    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.disconnect(promise);
    }

    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.close(promise);
    }

    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }

    public void read(ChannelHandlerContext ctx) throws Exception {
        ctx.read();
    }

    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(msg, promise);
    }

    public void flush(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

}
