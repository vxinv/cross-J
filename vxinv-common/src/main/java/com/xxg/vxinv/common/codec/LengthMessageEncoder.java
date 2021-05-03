package com.xxg.vxinv.common.codec;


import com.xxg.vxinv.common.protocol.LengthMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LengthMessageEncoder extends MessageToByteEncoder<LengthMessage> {

    Logger Log = LoggerFactory.getLogger(LengthMessageEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, LengthMessage msg, ByteBuf out) throws Exception {

        //Log.info("client  write  encode {}", new String(msg.getData()));
        out.writeShort(msg.getId());
        out.writeInt(msg.getLength());
        out.writeBytes(msg.getData());
    }
}
