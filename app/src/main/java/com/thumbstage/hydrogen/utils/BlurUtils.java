package com.thumbstage.hydrogen.utils;

import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlurUtils {

    private RenderScript rs;

    private static final float BITMAP_SCALE = 0.4f;

    //Set the radius of the Blur. Supported range 0 < radius <= 25
    private static float BLUR_RADIUS = 10.5f;

    @Inject
    public BlurUtils(RenderScript rs) {
        this.rs = rs;
    }

    public Bitmap blur(@NonNull Bitmap image, float blurRadius) {

        Bitmap outputBitmap;

        if (blurRadius == 0) {
            return image;
        }

        if (blurRadius < 1) {
            blurRadius = 1;
        }

        if (blurRadius > 25) {
            blurRadius = 25;
        }

        BLUR_RADIUS = blurRadius;

        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        outputBitmap = Bitmap.createBitmap(inputBitmap);

        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);


        return outputBitmap;
    }
}
