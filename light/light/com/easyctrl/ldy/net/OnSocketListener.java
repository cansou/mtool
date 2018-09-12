package com.easyctrl.ldy.net;

public interface OnSocketListener {
    void connectError();

    void connectSuccess();

    void disconnect();

    void receive(byte[] bArr);
}
