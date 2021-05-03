package com.xxg.vxinv.common.protocol;

/**
 * 基于长度的消息
 */
public class LengthMessage {

    private int  length;
    private short id;
    private byte[] data;

    public LengthMessage() {
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
