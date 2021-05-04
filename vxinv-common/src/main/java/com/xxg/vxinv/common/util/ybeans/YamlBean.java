package com.xxg.vxinv.common.util.ybeans;

import java.util.List;

public class YamlBean {
    String run;
    int server_port;
    List<Binds> server_binds;
    String client_connect_addr;
    int client_connect_port;
    List<Proxys> client_proxys;

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public int getServer_port() {
        return server_port;
    }

    public void setServer_port(int server_port) {
        this.server_port = server_port;
    }

    public List<Binds> getServer_binds() {
        return server_binds;
    }

    public void setServer_binds(List<Binds> server_binds) {
        this.server_binds = server_binds;
    }

    public String getClient_connect_addr() {
        return client_connect_addr;
    }

    public void setClient_connect_addr(String client_connect_addr) {
        this.client_connect_addr = client_connect_addr;
    }

    public int getClient_connect_port() {
        return client_connect_port;
    }

    public void setClient_connect_port(int client_connect_port) {
        this.client_connect_port = client_connect_port;
    }

    public List<Proxys> getClient_proxys() {
        return client_proxys;
    }

    public void setClient_proxys(List<Proxys> client_proxys) {
        this.client_proxys = client_proxys;
    }
}
