package com.xxg.vxinv.server.handler;



import com.xxg.vxinv.common.protocol.LengthMessage;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeartBeatServerHandler extends SimpleChannelInboundHandler<LengthMessage> {

    Logger Log = LoggerFactory.getLogger(HeartBeatServerHandler.class);
    static short hearBeatId = 21888;
    static String msg = "heartbeat" ;
    static LengthMessage lengthMessage = null;
    static {
        lengthMessage = new LengthMessage();
        byte[] data = msg.getBytes();
        lengthMessage.setId(hearBeatId);
        lengthMessage.setLength(data.length);
        lengthMessage.setData(data);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LengthMessage msg) throws Exception {
        if (msg.getId() == hearBeatId){

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
       // Log.info("userEventTriggered {}" ,msg);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (IdleState.READER_IDLE.equals((event.state()))) {
                ctx.writeAndFlush(lengthMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE) ;
            }
        }
    }

}
