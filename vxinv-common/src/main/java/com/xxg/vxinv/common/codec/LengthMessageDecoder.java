package com.xxg.vxinv.common.codec;

import com.xxg.vxinv.common.protocol.LengthMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LengthMessageDecoder extends MessageToMessageDecoder<ByteBuf> {
    Logger Log = LoggerFactory.getLogger(LengthMessageDecoder.class);
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {

        LengthMessage lengthMessage = new LengthMessage();
        short id = msg.readShort();
        int length = msg.readInt();
        lengthMessage.setLength(length);
        lengthMessage.setId(id);
        byte[] bytes = new byte[length];
        msg.readBytes(bytes);
        lengthMessage.setData(bytes);
        Log.info("server   decode  length {} ", bytes.length);
        out.add(lengthMessage);
    }
}
