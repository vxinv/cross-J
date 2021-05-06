package com.xxg.vxinv.client.handler;


import com.xxg.vxinv.client.VxinvClient;
import com.xxg.vxinv.client.global.ChannelHolder;
import com.xxg.vxinv.common.handler.VxinvCommonHandler;
import com.xxg.vxinv.common.protocol.LengthMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class VxinvClientHandler extends VxinvCommonHandler {

    Logger Log = LoggerFactory.getLogger(VxinvClientHandler.class);


    public VxinvClientHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ChannelHolder.clientHandler = this;
        Log.info("client and server success");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LengthMessage message = (LengthMessage) msg;
        Log.info("client read len  {} , id {}", message.getLength(), message.getId());
        short id = message.getId();
        Log.info("retry 1");
        LocalProxyHandler handler = ChannelHolder.pm.get(id);

        if (!handler.getCtx().channel().isActive()) {
            Log.info("retry 2");
            Channel channel = VxinvClient.connectLocal(id);
            if (channel != null) {
                channel.writeAndFlush(message.getData());
            }
        }
        ChannelHolder.pm.get(id).getCtx().writeAndFlush(message.getData());

    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Log.info("Loss connection to vxinv server, Please restart!");
    }


}
