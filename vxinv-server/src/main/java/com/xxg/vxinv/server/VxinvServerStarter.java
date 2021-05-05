package com.xxg.vxinv.server;

import com.xxg.vxinv.common.util.ybeans.YamlBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class VxinvServerStarter {
    static Logger Log = LoggerFactory.getLogger(VxinvServerStarter.class);

    public static void serverRun(YamlBean yb) throws InterruptedException {
        Log.info("server run start");
        VxinvServer server = new VxinvServer();
        server.start(yb.getClient_connect_port(), yb.getServer_binds());
    }
}
