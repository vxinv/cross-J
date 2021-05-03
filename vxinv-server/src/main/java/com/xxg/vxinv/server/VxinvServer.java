package com.xxg.vxinv.server;

import com.xxg.vxinv.common.codec.LengthMessageDecoder;
import com.xxg.vxinv.common.codec.LengthMessageEncoder;
import com.xxg.vxinv.common.util.ybeans.Binds;
import com.xxg.vxinv.server.handler.HeartBeatServerHandler;
import com.xxg.vxinv.server.handler.VxinvServerHandler;
import com.xxg.vxinv.server.net.TcpServer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by wucao on 2019/2/27.
 */
public class VxinvServer {

    public void start(int port, List<Binds> binds) throws InterruptedException {

        TcpServer tcpServer = new TcpServer();
        tcpServer.bind(port, new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(
                        new IdleStateHandler(20, 0, 0, TimeUnit.SECONDS),
                        new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 2, 4, 0, 0),
                        new LengthMessageDecoder(),
                        new LengthMessageEncoder(),
                        new HeartBeatServerHandler(),
                        new VxinvServerHandler(binds));
            }
        });
    }
}
