package com.thumbstage.hydrogen.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thumbstage.hydrogen.R;

public class GlideUtil {

    public static void inject(Context context, String url, ImageView view) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_item_placeholder);
        requestOptions.error(R.drawable.ic_item_error);
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(url).into(view);
    }
}
