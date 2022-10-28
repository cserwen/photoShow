package utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonUtil {

    public static final Gson gson = new Gson();

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }
}
