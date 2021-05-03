package com.xxg.vxinv.client;

import com.xxg.vxinv.common.util.YmalReader;
import com.xxg.vxinv.common.util.ybeans.YamlBean;


class VxinvClientStarter {

    public static void main(String[] args) throws Exception {

        YamlBean yb = YmalReader.getMap();

        String server_addr = yb.getClient_connect_addr();
        int server_port = yb.getClient_connect_port();


        VxinvClient client = new VxinvClient();
        client.connect(server_addr, server_port, yb.getClient_proxys());
    }
}
