package com.thumbstage.hydrogen.view.create;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.thumbstage.hydrogen.model.callback.IStatusCallBack;

public interface ICreateMenuItemFunction {
    void sign(Context context);
    void settings(Fragment fragment);
    void startTopic();
    void publishTopic(IStatusCallBack iStatusCallBack);
}
