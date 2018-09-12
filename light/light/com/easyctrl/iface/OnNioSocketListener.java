package com.easyctrl.iface;

public interface OnNioSocketListener {
    void connectError();

    void connectSuccess();

    void disconnect();
}
