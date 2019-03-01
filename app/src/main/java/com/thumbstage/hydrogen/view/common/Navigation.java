package com.thumbstage.hydrogen.view.common;

import android.content.Context;
import android.content.Intent;

import com.avos.avoscloud.AVUser;
import com.thumbstage.hydrogen.view.account.AccountActivity;
import com.thumbstage.hydrogen.view.sign.SignActivity;

public class Navigation {
    public static void sign(Context context) {
        Intent intent = new Intent();
        AVUser currentUser = AVUser.getCurrentUser();
        if(currentUser != null) {
            intent.setClass(context, AccountActivity.class);
        } else {
            intent.setClass(context, SignActivity.class);
        }
        context.startActivity(intent);
    }
}
