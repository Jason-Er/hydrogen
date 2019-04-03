package com.thumbstage.hydrogen.view.common;

import android.content.Context;
import android.content.Intent;

import com.thumbstage.hydrogen.view.account.AccountActivity;
import com.thumbstage.hydrogen.view.sign.SignActivity;

public class Navigation {

    public static void sign2Account(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AccountActivity.class);
        context.startActivity(intent);
    }

    public static void sign2SignIn(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SignActivity.class);
        context.startActivity(intent);
    }
}
