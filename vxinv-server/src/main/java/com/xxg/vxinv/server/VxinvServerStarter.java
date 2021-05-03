package com.xxg.vxinv.server;

import com.xxg.vxinv.common.util.YmalReader;
import com.xxg.vxinv.common.util.ybeans.YamlBean;
import org.apache.commons.cli.ParseException;


/**
 * Created by wucao on 2019/10/23.
 */
public class VxinvServerStarter {

    public static void main(String[] args) throws ParseException, InterruptedException {


        YamlBean map = YmalReader.getMap();

        VxinvServer server = new VxinvServer();

        server.start(map.getClient_connect_port(),map.getServer_binds());

        System.out.println("vxinv server started on port " + map.getClient_connect_port());

    }
}
