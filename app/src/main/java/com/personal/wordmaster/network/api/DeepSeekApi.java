package com.personal.wordmaster.network.api;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.personal.wordmaster.constant.AppConstant;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeepSeekApi {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final OkHttpClient client;
    private final Gson gson;
    private final ExecutorService executor;
    private final Handler mainHandler;
    private String apiKey;

    public DeepSeekApi() {
        this.client = new OkHttpClient.Builder()
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build();
        this.gson = new Gson();
        this.executor = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.apiKey = "";
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public interface Callback {
        void onSuccess(String jsonResult);
        void onError(String message);
    }

    public void parseOcrText(String ocrText, Callback callback) {
        executor.execute(() -> {
            try {
                String result = executeRequest(ocrText);
                mainHandler.post(() -> callback.onSuccess(result));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    private String executeRequest(String ocrText) throws IOException {
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", AppConstant.DEEPSEEK_SYSTEM_PROMPT);

        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", ocrText);

        JsonArray messages = new JsonArray();
        messages.add(systemMessage);
        messages.add(userMessage);

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", AppConstant.DEEPSEEK_MODEL);
        requestBody.add("messages", messages);
        requestBody.addProperty("max_tokens", 4096);
        requestBody.addProperty("temperature", 0.1);

        Request request = new Request.Builder()
            .url(AppConstant.DEEPSEEK_API_URL)
            .addHeader("Authorization", "Bearer " + apiKey)
            .addHeader("Content-Type", "application/json")
            .post(RequestBody.create(requestBody.toString(), JSON))
            .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            if (!response.isSuccessful()) {
                throw new IOException("API返回" + response.code() + ": " + responseBody);
            }
            JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);
            JsonArray choices = responseJson.getAsJsonArray("choices");
            JsonObject firstChoice = choices.get(0).getAsJsonObject();
            JsonObject message = firstChoice.getAsJsonObject("message");
            return message.get("content").getAsString();
        }
    }
}
