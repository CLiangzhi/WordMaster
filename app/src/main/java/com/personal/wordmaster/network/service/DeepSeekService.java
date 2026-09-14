package com.personal.wordmaster.network.service;

import com.personal.wordmaster.network.api.DeepSeekApi;

public class DeepSeekService {

    private final DeepSeekApi api;

    public DeepSeekService(String apiKey) {
        this.api = new DeepSeekApi();
        this.api.setApiKey(apiKey);
    }

    public interface ParseCallback {
        void onSuccess(String jsonResult);
        void onError(String message);
    }

    public void parseOcrText(String ocrText, ParseCallback callback) {
        api.parseOcrText(ocrText, new DeepSeekApi.Callback() {
            @Override
            public void onSuccess(String jsonResult) {
                callback.onSuccess(jsonResult);
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }
}
