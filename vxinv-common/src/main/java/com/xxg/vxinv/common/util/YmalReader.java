package com.xxg.vxinv.common.util;

import com.alibaba.fastjson.JSON;
import com.xxg.vxinv.common.util.ybeans.YamlBean;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class YmalReader {

    static Yaml yaml = new Yaml();

    public static YamlBean getMap() {
        String pref_dir = System.getProperty("user.dir");
        System.out.println(pref_dir);
        String yamlUrl = pref_dir + File.separator + "config.yaml";
        try {
            return  JSON.parseObject(JSON.toJSONString(yaml.load(new FileInputStream(yamlUrl))),YamlBean.class);
        } catch (FileNotFoundException e) {
            System.out.println("未发现配置文件");
            e.printStackTrace();
        }
        return null;
    }


}
