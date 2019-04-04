package com.thumbstage.hydrogen.model.callback;

public interface IStatusCallBack {
    enum STATUS {
        OK, FAILURE
    }
    void callback(STATUS status);
}
