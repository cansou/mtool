package com.easyctrl.iface;

public interface OnReviceLoginListener {
    public static final int LOGIN_TYPE_ENTER = 0;
    public static final int LOGIN_TYPE_ERROE = 1;

    void loginApp(int i);
}
