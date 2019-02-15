package com.thumbstage.hydrogen.utils;

import android.webkit.URLUtil;

public class StringUtil {
    public static boolean isUrl(String url) {
        return URLUtil.isValidUrl(url);
    }
}
