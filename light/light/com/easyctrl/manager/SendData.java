package com.easyctrl.manager;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SendData {
    private static SendData instance;
    private ConcurrentLinkedQueue<byte[]> sendData = new ConcurrentLinkedQueue();

    private SendData() {
    }

    public static SendData getInstance() {
        if (instance == null) {
            instance = new SendData();
        }
        return instance;
    }

    public void add(byte[] data) {
        this.sendData.offer(data);
    }

    public ConcurrentLinkedQueue<byte[]> getSendData() {
        return this.sendData;
    }
}
