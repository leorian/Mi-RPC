package org.ahstu.mi.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by renyueliang on 17/5/17.
 */
public class MiFstDecoder extends LengthFieldBasedFrameDecoder {

    private Class clazz;

    public MiFstDecoder(int maxObjectSize, Class clazz) {
        super(maxObjectSize, 0, 4, 0, 4);
        this.clazz = clazz;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }
        byte[] out = new byte[frame.readableBytes()];
        frame.readBytes(out);
        return HessianSerializerTool.deserialize(out, clazz);
    }

    @Override
    protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {
        return buffer.slice(index, length);
    }
}
