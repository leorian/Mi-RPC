package org.ahstu.mi.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by renyueliang on 17/5/17.
 */
public class MiFstEncoder extends MessageToByteEncoder<Object> {


    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        int lengthPos = out.writerIndex();
        out.writeBytes(LENGTH_PLACEHOLDER);
        out.writeBytes(HessianSerializerTool.serialize(msg));
        //int store LENGTH_PLACEHOLDER de length type
        out.setInt(lengthPos, out.writerIndex() - lengthPos - 4);
    }
}
