package org.ahstu.mi.rpc.netty.server;

import com.bozhong.common.util.StringUtil;
import org.ahstu.mi.rpc.RpcServer;
import org.ahstu.mi.serialize.MiFstDecoder;
import org.ahstu.mi.serialize.MiFstEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.ahstu.mi.common.*;

import java.net.BindException;
import java.util.UUID;

/**
 * Created by renyueliang on 17/5/18.
 */
public class NettyServer  extends RpcServer {

    private static NettyServer nettyServer = null;
    private static int port = 20080;

    private static String NETTY_SERVER_LOCK= UUID.randomUUID().toString();
    private ChannelFuture f = null;
    private EventLoopGroup boss = null;
    private EventLoopGroup worker = null;
    private boolean start=false;


    private NettyServer(String serverIp, int serverPort) {
        super(serverIp, serverPort);
    }



    public static NettyServer getInstance(){
        if(nettyServer==null){
            synchronized (NETTY_SERVER_LOCK){

                if(nettyServer==null){
                    String ip= MiUtil.geLocalIp();
                    nettyServer=new NettyServer(ip,port);
                }
            }
        }
        return nettyServer;
    }

    @Override
    public void changPort() {
        ++port;
        setServerPort(port);
    }

    @Override
    public synchronized void changePortAndStart() {
        changPort();
        MiLogger.record("NettyServer.changePortAndStart has excute !  port:"+port);
        start=false;
        start();
    }

    @Override
    public String getServerIp() {
        return super.getServerIp();
    }

    @Override
    public int getServerPort() {
        return super.getServerPort();
    }

    @Override
    public synchronized void reset() {
        MiLogger.record("NettyServer.reset start ! ip:"+getServerIp()+" port:"+getServerPort());
        start=false;
        close();
        start();
    }



    @Override
    public synchronized void start() {
         boss = new NioEventLoopGroup();
         worker = new NioEventLoopGroup();

        try {

            if(start){
                MiLogger.record("NettyServer has start ! ip:"+getServerIp()+" port:"+getServerPort());
                return ;
            }

            MiLogger.record("************** NettyServer start ! ip:"+getServerIp()+" port:"+getServerPort()+" ************");

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG, MiConstants.SO_BACK_LOG); //连接数
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.option(ChannelOption.SO_SNDBUF, MiConstants.SO_SND_BUF);
            bootstrap.option(ChannelOption.SO_RCVBUF, MiConstants.SO_RCV_BUF);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);  //不延迟，消息立即发送
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); //长连接
            bootstrap.option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT);
            bootstrap.childOption(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT); //长连接
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel)
                        throws Exception {
                    ChannelPipeline p = socketChannel.pipeline();
                    p.addLast(new MiFstDecoder(MiConstants.MAX_OBJECT_SIZE,MiSendDTO.class));
                    p.addLast(new MiFstEncoder());
                    p.addLast(new RpcNettyServerCallHandler());
                }
            });
            f = bootstrap.bind(getServerPort()).sync();
            if (f.isSuccess()) {
                start=true;
                MiLogger.record("NettyServer.start IP:" + getServerIp() + " PORT:" + getServerPort()+ "success !");
            }
            // 关闭连接
            // f.channel().closeFuture().sync();

        }catch (Throwable e) {
            if(e instanceof BindException && StringUtil.isNotBlank(e.getMessage()) &&
                    (e.getMessage().contains("Address already in use") || e.getMessage().contains("地址已在使用") )){
                MiLogger.record("NettyServer.start IP:" + getServerIp() + " PORT:" + getServerPort()+" start error ! errorCode:"
                        + MiError.PORT_ALREADY_IN_USE_EXCEPTION.getErrorCode(),e);
                changePortAndStart();
                return ;
            }
            MiLogger.record("NettyServer.start IP:" + getServerIp() + " PORT:" + getServerPort()+" start error ! errorCode:" + e.getMessage(),e);
            throw new MiException(e.getMessage(), e);
        } finally {
            // boss.shutdownGracefully();
            // worker.shutdownGracefully();
        }
    }



    @Override
    public void close() {

        MiLogger.record("NettyServer.close server start ");
        // 关闭连接
        try {
            if(f!=null){
                f.channel().closeFuture().sync();
            }
        }catch (Throwable e){
            MiLogger.record("NettyServer.close server error ! errorCode:" + e.getMessage(),e);
        }finally {
            if(boss!=null){
                boss.shutdownGracefully();
            }
            if(worker!=null){
                worker.shutdownGracefully();
            }
        }
    }


    public static void main(String[] args){
        RpcServer.getRpcServer().start();
    }
}
