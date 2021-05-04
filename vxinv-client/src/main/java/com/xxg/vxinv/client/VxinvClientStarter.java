package com.xxg.vxinv.client;

import com.xxg.vxinv.client.handler.LocalProxyHandler;
import com.xxg.vxinv.client.net.TcpConnection;
import com.xxg.vxinv.common.util.ybeans.Proxys;
import com.xxg.vxinv.common.util.ybeans.YamlBean;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class VxinvClientStarter {

    static Logger Log = LoggerFactory.getLogger(VxinvClientStarter.class);

    public static void clientRun(YamlBean yb) throws Exception {

        Log.info("client run start");
        String server_addr = yb.getClient_connect_addr();
        int server_port = yb.getClient_connect_port();


        VxinvClient client = new VxinvClient();
        client.connect(server_addr, server_port);

        // a监听本地
        for (Proxys proxy : yb.getClient_proxys()) {
            TcpConnection localConnection = new TcpConnection();
            // 对代理地址端口监听
            try {
                localConnection.connect(proxy.getProxy_addr(), proxy.getProxy_port(), new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                new ByteArrayDecoder(),
                                new ByteArrayEncoder(),
                                new LocalProxyHandler(proxy.getId()));
                    }
                });
                Log.info("链接端口 {} 成功", proxy.getProxy_port());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
