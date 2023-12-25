package ru.michael.coco.attempt;

import com.google.gson.Gson;

public class JsonParser {
    public static Response parseJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Response.class);
    }
}
