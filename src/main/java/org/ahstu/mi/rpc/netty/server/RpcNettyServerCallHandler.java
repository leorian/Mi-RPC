package org.ahstu.mi.rpc.netty.server;


import org.ahstu.mi.common.MiLogger;
import org.ahstu.mi.common.MiResult;
import org.ahstu.mi.common.MiSendDTO;
import org.ahstu.mi.provider.InsistServiceDynamicCall;
import org.ahstu.mi.rpc.netty.InsistChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by renyueliang on 17/5/18.
 */
public class RpcNettyServerCallHandler extends InsistChannelHandlerAdapter {

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        MiSendDTO sendDTO = (MiSendDTO) msg;

        try {
            if (sendDTO != null) {

                MiLogger.record("server receive message requestId:" + sendDTO.getRequestId());
                long start = System.currentTimeMillis();
                MiResult insistResult = InsistServiceDynamicCall.call(sendDTO);
                insistResult.setRequestId(sendDTO.getRequestId());
                long end = System.currentTimeMillis();
                MiLogger.record("server method call requestId:" + sendDTO.getRequestId()
                        + ", spend time: " + (end - start) + "ms");
                sendMessage(insistResult, ctx);
                long sendEnd = System.currentTimeMillis();
                MiLogger.record("server send result requestId:" + sendDTO.getRequestId() + ", spend time: "
                        + (sendEnd - end) + "ms, " + (sendEnd - start) + "ms");

            }
        } catch (Throwable e) {
            MiLogger.getLogger().error("requestId:" + sendDTO.getRequestId() + " errorCode:" + e.getMessage(), e);
        }

    }

    private void sendMessage(MiResult insistResult, ChannelHandlerContext ctx) {
        ctx.writeAndFlush(insistResult);

    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
