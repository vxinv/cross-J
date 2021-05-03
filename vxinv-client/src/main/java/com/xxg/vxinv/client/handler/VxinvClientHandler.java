package com.xxg.vxinv.client.handler;


import com.xxg.vxinv.client.net.TcpConnection;
import com.xxg.vxinv.common.handler.VxinvCommonHandler;
import com.xxg.vxinv.common.protocol.LengthMessage;
import com.xxg.vxinv.common.util.ybeans.Proxys;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@ChannelHandler.Sharable
public class VxinvClientHandler extends VxinvCommonHandler {

    List<Proxys> proxys;
    Logger Log = LoggerFactory.getLogger(VxinvClientHandler.class);


    private ConcurrentHashMap<Short, String> channelHandlerMap = new ConcurrentHashMap<>();
    private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public VxinvClientHandler(List<Proxys> proxys) {
        this.proxys = proxys;
        doLocalConncet();
    }

    public void doLocalConncet() {
        VxinvClientHandler handler = this;
        // 通道建立 开始监听配置
        for (Proxys proxy : proxys) {
            TcpConnection localConnection = new TcpConnection();
            // 对代理地址端口监听
            try {
                localConnection.connect(proxy.getProxy_addr(), proxy.getProxy_port(), new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        LocalProxyHandler localProxyHandler = new LocalProxyHandler(handler, proxy.getId());
                        ch.pipeline().addLast(
                                new ByteArrayDecoder(),
                                new ByteArrayEncoder(),
                                localProxyHandler);

                        channelHandlerMap.put(proxy.getId(), ch.id().asLongText());
                        channelGroup.add(ch);
                    }
                });
                Log.info("connect local port {} success", proxy.getProxy_port());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LengthMessage message = (LengthMessage) msg;
        processData(message);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.info("lose {}", 1);
        channelGroup.close();
        System.out.println("Loss connection to vxinv server, Please restart!");
    }



    private void processData(LengthMessage lengthMessage) {
        short Id = lengthMessage.getId();
        String textId = channelHandlerMap.get(Id);
        channelGroup.writeAndFlush(lengthMessage.getData(),channel -> channel.id().asLongText().equals(textId));
    }


}
