package com.wyu.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyu.config.ContextHolder;
import com.wyu.constant.MyState;
import com.wyu.model.CourseVO;
import com.wyu.model.ScoreVO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

public class FileUtil {

    private static final Gson gson = new Gson();

    public static int JsonToFile(String json, String path) {
        try {
            File file = new File(path);
            FileOutputStream out = MyApplication.getContext().openFileOutput(path, Context.MODE_PRIVATE);
            out.write(json.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public static String readJsonFile(String path) {
        String json = "";
        FileInputStream fis;
        try {
            fis = MyApplication.getContext().openFileInput(path);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            json = new String(buffer);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static boolean saveInfo() {
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences("data.conf", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Log.i(MyState.TAG, "save");
        String courseData = gson.toJson(ContextHolder.courseData);
        String scoreData = gson.toJson(ContextHolder.scoreData);
        String currentWeek = gson.toJson(ContextHolder.currentWeek);

        editor.putString("courseData", courseData);
        editor.putString("scoreData", scoreData);
        editor.putString("currentWeek", currentWeek);
        editor.commit();
        return true;
    }

    public static void getInfo() {
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("data.conf", Context.MODE_PRIVATE);

        String courseDataStr = sharedPreferences.getString("courseData", "");
        String scoreDataStr = sharedPreferences.getString("scoreData", "");
        String currentWeek = sharedPreferences.getString("currentWeek", "1");

        ContextHolder.courseData = gson.fromJson(courseDataStr, new TypeToken<Map<String, Map<Integer, CourseVO>>>(){}.getType());
        ContextHolder.scoreData = gson.fromJson(scoreDataStr, new TypeToken<Map<String, ScoreVO>>() {}.getType());
        ContextHolder.currentWeek = Integer.parseInt(currentWeek);
    }
}
