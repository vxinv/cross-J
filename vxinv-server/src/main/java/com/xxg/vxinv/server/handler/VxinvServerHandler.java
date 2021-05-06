package com.xxg.vxinv.server.handler;

import com.xxg.vxinv.common.handler.VxinvCommonHandler;
import com.xxg.vxinv.common.protocol.LengthMessage;
import com.xxg.vxinv.server.global.ChannelHolder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;


@ChannelHandler.Sharable
public class VxinvServerHandler extends VxinvCommonHandler {

    private Logger Log = LoggerFactory.getLogger(VxinvServerHandler.class);

    private static ConcurrentHashMap<Short, String> cid = new ConcurrentHashMap<>();

    public VxinvServerHandler() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ChannelHolder.serverhandler = this;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LengthMessage message = (LengthMessage) msg;
        Log.info("server channel read  len{}", message.getLength());
        processData(message);
    }

    private void processData(LengthMessage message) {
        Log.info("message ID {}", message.getId());
        short id = message.getId();
        ChannelHolder.proxyHandlerMap.get(id).getCtx().writeAndFlush(message.getData());
    }

}
