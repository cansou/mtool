package com.easyctrl.ldy.net;

public class Packet {
    private byte[] data;
    private int id = AtomicIntegerUtil.getIncrementID();

    public int getId() {
        return this.id;
    }

    public void pack(String txt) {
        this.data = txt.getBytes();
    }

    public void pack(byte[] data) {
        this.data = data;
    }

    public byte[] getPacket() {
        return this.data;
    }
}
