package com.xxg.vxinv.client.handler;


import com.xxg.vxinv.client.global.ChannelHolder;
import com.xxg.vxinv.common.handler.VxinvCommonHandler;
import com.xxg.vxinv.common.protocol.LengthMessage;
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
        ChannelHolder.proxyClientHandlerMap.get(id).getCtx().writeAndFlush(message.getData());
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.info("lose {}", 1);
        System.out.println("Loss connection to vxinv server, Please restart!");
    }


}
