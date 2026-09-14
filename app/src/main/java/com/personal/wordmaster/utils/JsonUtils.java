package com.personal.wordmaster.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final Gson gson = new Gson();

    public static class WordEntry {
        private String word;
        private String meaning;

        public String getWord() { return word; }
        public void setWord(String word) { this.word = word; }
        public String getMeaning() { return meaning; }
        public void setMeaning(String meaning) { this.meaning = meaning; }
    }

    public static List<WordEntry> parseWordList(String jsonStr) {
        List<WordEntry> result = new ArrayList<>();
        try {
            JsonArray array = JsonParser.parseString(jsonStr).getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                JsonObject obj = array.get(i).getAsJsonObject();
                WordEntry entry = new WordEntry();
                entry.setWord(obj.get("word").getAsString().trim());
                entry.setMeaning(obj.get("meaning").getAsString().trim());
                result.add(entry);
            }
        } catch (Exception e) {
            return null;
        }
        return result;
    }
}
