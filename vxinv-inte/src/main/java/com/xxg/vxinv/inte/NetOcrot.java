package com.xxg.vxinv.inte;

import com.xxg.vxinv.client.VxinvClientStarter;
import com.xxg.vxinv.common.util.YmalReader;
import com.xxg.vxinv.common.util.ybeans.YamlBean;
import com.xxg.vxinv.server.VxinvServerStarter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetOcrot {
    static Logger Log = LoggerFactory.getLogger(NetOcrot.class);
    public static void main(String[] args) throws Exception {

        Options options = new Options();
        options.addOption("s", false, "run server");
        options.addOption("c", false, "run client");

        options.addOption("S", false, "run server");
        options.addOption("C", false, "run client");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        YamlBean yb = YmalReader.getMap();
        if (cmd.hasOption("c") || cmd.hasOption("C")) {
            VxinvClientStarter.clientRun(yb);
        } else if (cmd.hasOption("s") || cmd.hasOption("S")) {
            VxinvServerStarter.serverRun(yb);
        } else {
            Log.error("配置错误");
            throw new Exception(" run 选项配置错误");
        }
    }
}
