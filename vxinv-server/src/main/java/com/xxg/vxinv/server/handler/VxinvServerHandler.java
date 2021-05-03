package com.xxg.vxinv.server.handler;

import com.xxg.vxinv.common.handler.VxinvCommonHandler;
import com.xxg.vxinv.common.protocol.LengthMessage;
import com.xxg.vxinv.common.util.ybeans.Binds;
import com.xxg.vxinv.server.net.TcpServer;
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

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@ChannelHandler.Sharable
public class VxinvServerHandler extends VxinvCommonHandler {

    private Logger Log = LoggerFactory.getLogger(VxinvServerHandler.class);

    private ConcurrentHashMap<Short, String> cid = new ConcurrentHashMap<>();

    private List<Binds> binds;

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static boolean register = false;

    public VxinvServerHandler(List<Binds> binds) {
        this.binds = binds;
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        processRegister();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LengthMessage message = (LengthMessage) msg;
        //Log.info("server channel read {}", new String(message.getData()));
        processData(message);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

    private void processRegister() {
        if (register) {
            return;
        }
        for (Binds bind : binds) {
            Log.info("bind local port {}", bind.getPort());
            try {
                TcpServer tcpServer = new TcpServer();
                VxinvServerHandler thisHandler = this;
                tcpServer.bind(bind.getPort(), new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                new ByteArrayDecoder(),
                                new ByteArrayEncoder(),
                                new RemoteProxyHandler(thisHandler, bind.getId()));
                        cid.put(bind.getId(), ch.id().asLongText());
                        channels.add(ch);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        register = true;

    }


    private void processData(LengthMessage message) {
        //Log.info("message ID {}", message.getId());
        channels.writeAndFlush(message.getData(), channel -> channel.id().asLongText().equals(cid.get(message.getId())));
    }


}
