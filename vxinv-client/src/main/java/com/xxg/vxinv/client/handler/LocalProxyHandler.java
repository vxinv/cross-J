package com.xxg.vxinv.client.handler;

import com.xxg.vxinv.client.global.ChannelHolder;
import com.xxg.vxinv.common.handler.VxinvCommonHandler;
import com.xxg.vxinv.common.protocol.LengthMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wucao on 2019/3/2.
 */
@ChannelHandler.Sharable
public class LocalProxyHandler extends VxinvCommonHandler {

    Logger Log = LoggerFactory.getLogger(LocalProxyHandler.class);

    private Short id;

    public LocalProxyHandler(Short id) {
        this.id = id;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] data = (byte[]) msg;
        Log.info("LocalProxyHandler read  Client write {}", new String(data));
        LengthMessage message = new LengthMessage();
        message.setData(data);
        message.setId(id);
        message.setLength(data.length);
        ChannelHolder.clientHandler.getCtx().writeAndFlush(message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Log.info("LocalProxy {} active", id);
        super.channelActive(ctx);
        ChannelHolder.proxyClientHandlerMap.put(id, LocalProxyHandler.this);
    }


}
