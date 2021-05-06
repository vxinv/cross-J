package com.xxg.vxinv.client.global;


import com.xxg.vxinv.client.handler.LocalProxyHandler;
import com.xxg.vxinv.client.handler.VxinvClientHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存放 channel handler 引用
 */
public class ChannelHolder {
    // 和client 链接 转发流量
    public static VxinvClientHandler clientHandler;
    // 监听本地连接的 channelHandler
    public static Map<Short, LocalProxyHandler> proxyClientHandlerMap = new ConcurrentHashMap<>(4);
    public static Map<Short, LocalProxyHandler> pm = new ConcurrentHashMap<>(4);
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
