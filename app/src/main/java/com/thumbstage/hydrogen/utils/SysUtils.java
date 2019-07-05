package com.thumbstage.hydrogen.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

public class SysUtils {
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
