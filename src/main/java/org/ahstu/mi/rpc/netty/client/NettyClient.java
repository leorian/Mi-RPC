package org.ahstu.mi.rpc.netty.client;

import com.bozhong.common.util.StringUtil;
import org.ahstu.mi.lock.InsistLock;
import org.ahstu.mi.lock.InsistLockStore;
import org.ahstu.mi.serialize.InsistFstDecoder;
import org.ahstu.mi.serialize.InsistFstEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutException;
import org.ahstu.mi.common.*;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.ClosedSelectorException;
import java.util.UUID;

/**
 * Created by renyueliang on 17/5/17.
 */
public class NettyClient {

    private static NettyClient nettyClient = null;
    private static String NETTYCLIENT_LOCK = UUID.randomUUID().toString();

    private Bootstrap bootstrap;
    private EventLoopGroup eventLoopGroup;

    public static NettyClient getInstance() {

        if (nettyClient == null) {
            synchronized (NETTYCLIENT_LOCK) {
                if (nettyClient == null) {
                    nettyClient = new NettyClient();
                }
            }
        }

        return nettyClient;

    }

    private NettyClient() {
        init();
    }

    private void init() {

        MiLogger.record("NettyClient.init start !");

        try {

            bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT);
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            eventLoopGroup = new NioEventLoopGroup();
            bootstrap.group(eventLoopGroup);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {

                    socketChannel.pipeline().addLast(new InsistFstDecoder(MiConstants.MAX_OBJECT_SIZE, MiResult.class));
                    socketChannel.pipeline().addLast(new InsistFstEncoder());

                    socketChannel.pipeline().addLast(new RpcNettyClientCallHandler());

                }
            });

            MiLogger.record("NettyClient.init success  !");

        } catch (ReadTimeoutException readTimeoutException) {
            throw new MiException(MiError.CONNECTION_TIMIEOUT, readTimeoutException);
        } catch (ClosedSelectorException ce) {
            throw new MiException(MiError.CLOSED_SELECTOR_EXCEPTION, ce);
        } catch (Throwable e) {
            throw new MiException(e.getMessage(), e);
        } finally {


        }
    }


    public ChannelHandlerContext getChc(final MiSendDTO sendDTO) {
        final String chcKey = InsistUtil.ipAndPortCreateKey(sendDTO.getServerIp(), sendDTO.getPort());
        ChannelHandlerContext chc = NettyChannelHandlerStore.get(chcKey);
        if (chc == null) {
            synchronized (NETTYCLIENT_LOCK) {
                chc = NettyChannelHandlerStore.get(chcKey);
                if (chc == null) {
                    final InsistLock insistLock = new InsistLock(chcKey);
                    InsistLockStore.add(insistLock);
                    synchronized (insistLock) {
                        try {
                            MiLogger.record(StringUtil.format("NettyClient.getChc ip:%s port:%s connect start !",
                                    sendDTO.getServerIp(),
                                    sendDTO.getPort()));

                            bootstrap.connect(sendDTO.getServerIp(), sendDTO.getPort()).sync();

                            insistLock.lock();

                            chc = NettyChannelHandlerStore.get(chcKey);

                            MiLogger.record(StringUtil.format("NettyClient.getChc ip:%s port:%s connect success !",
                                    sendDTO.getServerIp(),
                                    sendDTO.getPort()));

                        } catch (Throwable e) {
                            throw new MiException(MiError.CONNECTION_INTERRUPT, e);
                        }finally {
                            InsistLockStore.del(chcKey);
                        }
                    }
                }
            }
        }
        return chc;
    }


    public void send(MiSendDTO sendDTO) {
        try {
            ChannelHandlerContext ctx = getChc(sendDTO);
            ChannelFuture future = ctx.channel().writeAndFlush(sendDTO);
            if (future.sync().isSuccess()) {

            }
        } catch (Throwable e) {
            if (e instanceof ClosedChannelException) {
                throw new MiException(MiError.CLOSED_SELECTOR_EXCEPTION, e);
            }
            throw new MiException(e.getMessage(), e);
        }
    }

}
