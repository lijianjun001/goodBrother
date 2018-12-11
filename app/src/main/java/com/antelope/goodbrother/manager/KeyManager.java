package com.antelope.goodbrother.manager;

public class KeyManager {

    private static volatile KeyManager instance;

    public static KeyManager getInstance() {
        if (instance == null) {
            synchronized (KeyManager.class) {
                instance = new KeyManager();
            }
        }
        return instance;
    }

    public KeyManager() {
        System.loadLibrary("native-lib");
    }

    public native String getWxKey();

    public native String getKey();

    public native String getInitVector();
}
