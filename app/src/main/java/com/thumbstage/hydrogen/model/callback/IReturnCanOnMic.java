package com.thumbstage.hydrogen.model.callback;

import com.thumbstage.hydrogen.model.bo.CanOnMic;

import java.util.Set;

public interface IReturnCanOnMic {
    void callback(Set<CanOnMic> canOnMicList);
}
