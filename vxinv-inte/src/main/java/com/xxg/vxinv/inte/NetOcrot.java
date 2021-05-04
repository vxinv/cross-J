package com.xxg.vxinv.inte;

import com.xxg.vxinv.client.VxinvClientStarter;
import com.xxg.vxinv.common.util.YmalReader;
import com.xxg.vxinv.common.util.ybeans.YamlBean;
import com.xxg.vxinv.server.VxinvServerStarter;

public class NetOcrot {

    public static void main(String[] args) throws Exception {
        YamlBean yb = YmalReader.getMap();
        if (yb.getRun().equals("c")) {
            VxinvClientStarter.clientRun(yb);
        } else if (yb.getRun().equals("s")) {
            VxinvServerStarter.serverRun(yb);
        } else {
            throw new Exception("config.yaml run 选项配置错误");
        }
    }
}
