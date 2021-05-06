package com.xxg.vxinv.client;

import com.xxg.vxinv.client.handler.HeartBeatClientHandle;
import com.xxg.vxinv.client.handler.LocalProxyHandler;
import com.xxg.vxinv.client.handler.VxinvClientHandler;
import com.xxg.vxinv.client.net.TcpConnection;
import com.xxg.vxinv.common.codec.LengthMessageDecoder;
import com.xxg.vxinv.common.codec.LengthMessageEncoder;
import com.xxg.vxinv.common.util.ybeans.Proxys;
import com.xxg.vxinv.common.util.ybeans.YamlBean;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 只负责与 Server进行链接
 */
public class VxinvClient {

    static Logger Log = LoggerFactory.getLogger(VxinvClient.class);

    static YamlBean yamlBean;


    public void connect(String serverAddress, int serverPort, YamlBean yb) throws IOException, InterruptedException {

        yamlBean = yb;
        TcpConnection tcpConnection = new TcpConnection();

        ChannelFuture future = tcpConnection.connect(serverAddress, serverPort, new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(
                        new IdleStateHandler(0, 0, 0, TimeUnit.SECONDS),
                        new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 2, 4, 0, 0),
                        new LengthMessageDecoder(),
                        new LengthMessageEncoder(),
                        new HeartBeatClientHandle(),
                        new VxinvClientHandler());
            }
        });

        Log.info("链接公网IP{} 端口 {} 成功", serverAddress, serverPort);

        // channel close retry connect
        future.addListener(future1 -> new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        connect(serverAddress, serverPort,yb);
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

        // Client 监听本地
        for (Proxys proxy : yb.getClient_proxys()) {
           connectLocal(proxy.getProxy_addr(),proxy.getProxy_port(),proxy.getId());
        }
    }

    public static Channel  connectLocal(String addr,int port ,short id){
        TcpConnection localConnection = new TcpConnection();
        // 对代理地址端口监听
        try {
            Channel channel = localConnection.connect(addr, port, new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            new ByteArrayDecoder(),
                            new ByteArrayEncoder(),
                            new LocalProxyHandler(id));
                }
            }).channel();
            return channel;
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Channel connectLocal(short id){
        Log.info("re client connect id {}",id);
        for (Proxys client_proxy : yamlBean.getClient_proxys()) {
            if (client_proxy.getId() == id){
               return connectLocal(client_proxy.getProxy_addr(), client_proxy.getProxy_port(), id);
            }
        }
        return null;
    }

}
