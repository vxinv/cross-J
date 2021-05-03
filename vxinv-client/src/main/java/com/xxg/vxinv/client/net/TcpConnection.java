package com.xxg.vxinv.client.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * A TCP connection to server
 */
public class TcpConnection {

    Logger Log = LoggerFactory.getLogger(TcpConnection.class);
    /**
     *
     * @param host
     * @param port
     * @param channelInitializer
     * @throws InterruptedException
     */
    public ChannelFuture connect(String host, int port, ChannelInitializer channelInitializer) throws InterruptedException, IOException {

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(channelInitializer);

            Channel channel = b.connect(host, port).sync().channel();
            //Log.info("connect success Ip {} port {}",host,port);
            return channel.closeFuture().addListener(future -> workerGroup.shutdownGracefully());
        } catch (Exception e) {
            workerGroup.shutdownGracefully();
            throw e;
        }
    }
}