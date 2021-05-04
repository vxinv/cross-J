package com.xxg.vxinv.server.handler;

import com.xxg.vxinv.common.handler.VxinvCommonHandler;
import com.xxg.vxinv.common.protocol.LengthMessage;
import com.xxg.vxinv.server.global.ChannelHolder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ChannelHandler.Sharable
public class RemoteProxyHandler extends VxinvCommonHandler {

    Logger Log = LoggerFactory.getLogger(RemoteProxyHandler.class);

    short id;

    public RemoteProxyHandler(short id) {
        Log.info(" construct id {}", id);
        this.id = id;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Log.info("hashcode: {}", this.hashCode());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Log.info("hashcode: {}", this.hashCode());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ChannelHolder.proxyHandlerMap.put(id, this);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.info("hashcode: {} pipline {} id {}", this.hashCode(), this.ctx.pipeline().channel().hashCode(), id);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] data = (byte[]) msg;

        LengthMessage message = new LengthMessage();
        message.setLength(data.length);
        message.setId(id);
        message.setData(data);
        Log.info("server bind read  {}", new String(data));
        ChannelHolder.serverhandler.getCtx().writeAndFlush(message);
    }
}
