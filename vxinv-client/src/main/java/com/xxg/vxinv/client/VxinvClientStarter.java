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
        client.connect(server_addr, server_port,yb);
    }


}
