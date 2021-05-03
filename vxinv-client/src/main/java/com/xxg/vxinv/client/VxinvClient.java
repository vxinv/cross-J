package com.xxg.vxinv.client;

import com.xxg.vxinv.client.handler.HeartBeatClientHandle;
import com.xxg.vxinv.client.handler.VxinvClientHandler;
import com.xxg.vxinv.client.net.TcpConnection;
import com.xxg.vxinv.common.codec.LengthMessageDecoder;
import com.xxg.vxinv.common.codec.LengthMessageEncoder;
import com.xxg.vxinv.common.util.ybeans.Proxys;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 只负责与 Server进行链接
 */
public class VxinvClient {

    Logger Log = LoggerFactory.getLogger(VxinvClient.class);

    public void connect(String serverAddress, int serverPort, List<Proxys> proxys) throws IOException, InterruptedException {
        TcpConnection tcpConnection = new TcpConnection();
        ChannelFuture future = tcpConnection.connect(serverAddress, serverPort, new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {

                ch.pipeline().addLast(
                        new IdleStateHandler(0, 25, 0, TimeUnit.SECONDS),
                        new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 2, 4, 0, 0),
                        new LengthMessageDecoder(),
                        new LengthMessageEncoder(),
                        new HeartBeatClientHandle(),
                        new VxinvClientHandler(proxys));
            }
        });
        Log.info("client connect server IP {} port {} success ",serverAddress,serverPort);
        // channel close retry connect
        future.addListener(future1 -> new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        connect(serverAddress, serverPort,proxys);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }.start());
    }
}
