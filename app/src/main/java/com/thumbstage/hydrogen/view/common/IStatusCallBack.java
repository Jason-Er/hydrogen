package com.thumbstage.hydrogen.view.common;

public interface IStatusCallBack {
    enum STATUS {
        OK, FAILURE
    }
    void callback(STATUS status);
}
