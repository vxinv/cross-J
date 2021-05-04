package com.xxg.vxinv.client.handler;


import com.xxg.vxinv.client.global.ChannelHolder;
import com.xxg.vxinv.common.handler.VxinvCommonHandler;
import com.xxg.vxinv.common.protocol.LengthMessage;
import com.xxg.vxinv.common.util.ybeans.Proxys;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ChannelHandler.Sharable
public class VxinvClientHandler extends VxinvCommonHandler {

    List<Proxys> proxys;
    Logger Log = LoggerFactory.getLogger(VxinvClientHandler.class);


    public VxinvClientHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ChannelHolder.clientHandler = this;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LengthMessage message = (LengthMessage) msg;
        Log.info("client read {}", new String(message.getData()));
        processData(message);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.info("lose {}", 1);
        System.out.println("Loss connection to vxinv server, Please restart!");
    }


    private void processData(LengthMessage lengthMessage) {
        short id = lengthMessage.getId();
        ChannelHolder.proxyClientHandlerMap.get(id).getCtx().writeAndFlush(lengthMessage.getData());
    }


}
