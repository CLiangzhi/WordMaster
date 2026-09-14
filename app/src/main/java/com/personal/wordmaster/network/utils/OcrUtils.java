package com.personal.wordmaster.network.utils;

import android.graphics.Bitmap;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions;

public class OcrUtils {

    private static final TextRecognizer recognizer =
        TextRecognition.getClient(new ChineseTextRecognizerOptions.Builder().build());

    public interface OcrCallback {
        void onSuccess(String text);
        void onError(String message);
    }

    public static void recognizeText(Bitmap bitmap, OcrCallback callback) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        recognizer.process(image)
            .addOnSuccessListener(result -> {
                String text = result.getText();
                if (text == null || text.trim().isEmpty()) {
                    callback.onError("未识别到文字，请检查图片是否清晰");
                } else {
                    callback.onSuccess(text);
                }
            })
            .addOnFailureListener(e ->
                callback.onError("文字识别失败: " + e.getMessage())
            );
    }
}
