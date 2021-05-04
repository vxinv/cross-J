package com.xxg.vxinv.client.global;


import com.xxg.vxinv.client.handler.LocalProxyHandler;
import com.xxg.vxinv.client.handler.VxinvClientHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 存放 channel handler 引用
 */
public class ChannelHolder {
    // 和client 链接 转发流量
    public static VxinvClientHandler clientHandler;
    // 监听本地连接的 channelHandler
    public static Map<Short, LocalProxyHandler> proxyClientHandlerMap = new HashMap<>(4);
}
