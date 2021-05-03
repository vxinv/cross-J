package com.xxg.vxinv.client.handler;

import com.xxg.vxinv.common.protocol.LengthMessage;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeartBeatClientHandle extends SimpleChannelInboundHandler<LengthMessage> {

    static short hearBeatId = 21888;
    Logger Log = LoggerFactory.getLogger(HeartBeatClientHandle.class);
    static String msg = "heartbeat" ;
    static LengthMessage lengthMessage = null;
    static {
        lengthMessage = new LengthMessage();
        byte[] data = msg.getBytes();
        lengthMessage.setId((short) 0);
        lengthMessage.setLength(data.length);
        lengthMessage.setData(data);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LengthMessage msg) throws Exception {
        if (msg.getId() == hearBeatId){
            ctx.writeAndFlush(lengthMessage);
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * 如果5s没有读请求，则向客户端发送心跳
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //Log.info("userEventTriggered {}" ,msg);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (IdleState.WRITER_IDLE.equals((event.state()))) {
                ctx.writeAndFlush(lengthMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE) ;
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}
