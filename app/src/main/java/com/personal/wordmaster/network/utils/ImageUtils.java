package com.personal.wordmaster.network.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.personal.wordmaster.constant.AppConstant;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class ImageUtils {

    public static String bitmapToBase64(Bitmap bitmap) {
        Bitmap compressed = compressBitmap(bitmap);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compressed.compress(Bitmap.CompressFormat.JPEG, AppConstant.IMAGE_QUALITY, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static Bitmap compressBitmap(Bitmap original) {
        int width = original.getWidth();
        int height = original.getHeight();

        if (width <= AppConstant.IMAGE_MAX_WIDTH && height <= AppConstant.IMAGE_MAX_HEIGHT) {
            return original;
        }

        float ratio = Math.min(
            (float) AppConstant.IMAGE_MAX_WIDTH / width,
            (float) AppConstant.IMAGE_MAX_HEIGHT / height
        );

        int newWidth = Math.round(width * ratio);
        int newHeight = Math.round(height * ratio);

        return Bitmap.createScaledBitmap(original, newWidth, newHeight, true);
    }

    public static String buildDataUri(String base64) {
        return "data:image/jpeg;base64," + base64;
    }
}
