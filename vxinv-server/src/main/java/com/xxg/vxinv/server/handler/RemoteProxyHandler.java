package com.xxg.vxinv.server.handler;

import com.xxg.vxinv.common.handler.VxinvCommonHandler;
import com.xxg.vxinv.common.protocol.LengthMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by wucao on 2019/3/2.
 */
public class RemoteProxyHandler extends VxinvCommonHandler {


    private VxinvCommonHandler serverHandler;
    private Short id;

    public RemoteProxyHandler(VxinvCommonHandler proxyHandler, short id) {
        this.serverHandler = proxyHandler;
        this.id = id;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] data = (byte[]) msg;

        LengthMessage message = new LengthMessage();
        message.setLength(data.length);
        message.setId(id);;
        message.setData(data);

        serverHandler.getCtx().writeAndFlush(message);
    }
}
