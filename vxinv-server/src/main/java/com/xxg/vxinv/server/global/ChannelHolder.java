package com.xxg.vxinv.server.global;

import com.xxg.vxinv.server.handler.RemoteProxyHandler;
import com.xxg.vxinv.server.handler.VxinvServerHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 存放 channel handler 引用
 */
public class ChannelHolder {
    // 和client 链接 转发流量
    public static VxinvServerHandler serverhandler;
    // 监听本地连接的 channelHandler
    public static Map<Short, RemoteProxyHandler> proxyHandlerMap = new HashMap<>(4);
}
