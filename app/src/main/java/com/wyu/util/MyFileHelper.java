package com.wyu.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.List;

import com.wyu.data.CourseCard;
import com.wyu.data.CourseList;
import com.wyu.data.CourseTableDivide;
import com.wyu.data.DataConf;
import com.wyu.data.ScoresList;
import com.wyu.config.ContextHolder;

public class MyFileHelper {
    public static int JsonToFile(String json,String path){
        try{
            File file = new File(path);
            FileOutputStream out = MyApplication.getContext().openFileOutput(path,Context.MODE_PRIVATE);
            out.write(json.getBytes());
            out.close();
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
        return 0;
    }
    public static String readJsonFile(String path){
        String json = "";
        FileInputStream fis;
        try{
            fis = MyApplication.getContext().openFileInput(path);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            json = new String(buffer);
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return json;
    }
    public static boolean saveInfo(){
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences("data.conf",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String myCourseTableList = gson.toJson(ContextHolder.myCourseTableList);
        String myScoresListsList = gson.toJson(ContextHolder.myScoresListsList);
        String courseConf = gson.toJson(ContextHolder.courseConf);
        String scoresConf = gson.toJson(ContextHolder.scoresConf);

        editor.putString("myCourseTableList",myCourseTableList);
        editor.putString("myScoresListsList",myScoresListsList);
        editor.putString("courseConf",courseConf);
        editor.putString("scoresConf",scoresConf);
        editor.putInt("curSemPos", ContextHolder.curSemPos);
        editor.putInt("semWeekStart", ContextHolder.semWeekStart);  //第一周的位置
        editor.putString("firstYear", ContextHolder.firstYear);
        editor.putString("userNumber", ContextHolder.userNumber);
        editor.putString("password", ContextHolder.password);
        editor.commit();
        return true;
    }
    public static void getInfo(){
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences("data.conf",Context.MODE_PRIVATE);

        Gson gson = new Gson();
        Type listType1 = new TypeToken<List<String>>(){}.getType();
        Type listType2 = new TypeToken<List<DataConf>>(){}.getType();

        String myCourseTableList = sp.getString("myCourseTableList","");
        String myScoresListsList = sp.getString("myScoresListsList","");
        String courseConf = sp.getString("courseConf","");
        String scoresConf = sp.getString("scoresConf","");


        ContextHolder.myCourseTableList = gson.fromJson(myCourseTableList,listType1);
        ContextHolder.myScoresListsList = gson.fromJson(myScoresListsList,listType1);
        ContextHolder.courseConf = gson.fromJson(courseConf,listType2);
        ContextHolder.scoresConf = gson.fromJson(scoresConf,listType2);

        ContextHolder.curSemPos = sp.getInt("curSemPos", ContextHolder.curSemPos);
        ContextHolder.semWeekStart = sp.getInt("semWeekStart", ContextHolder.semWeekStart);  //第一周的位置
        if(ContextHolder.semWeekStart>0){
            ContextHolder.semWeekNow = ContextHolder.currentWeek- ContextHolder.semWeekStart+1;
        }
        ContextHolder.firstYear = sp.getString("firstYear", ContextHolder.firstYear);
        ContextHolder.userNumber = sp.getString("userNumber", ContextHolder.userNumber);
        ContextHolder.password = sp.getString("password", ContextHolder.password);

        if(ContextHolder.courseConf!=null){
            for(DataConf dataConf: ContextHolder.courseConf){
                String json = readJsonFile(dataConf.path);
                CourseList courseList = gson.fromJson(json, CourseList.class);
                ContextHolder.allCourse.put(dataConf.xq, courseList);
                List<CourseCard> courseCards = CourseTableDivide.courseTableDivide(courseList.getTable());
                ContextHolder.courseCards.put(dataConf.xq,courseCards);
            }
        }
        if(ContextHolder.scoresConf!=null){
            for(DataConf dataConf: ContextHolder.scoresConf){
                String json = readJsonFile(dataConf.path);
                ScoresList scoresLists = gson.fromJson(json,ScoresList.class);
                ContextHolder.allScores.put(dataConf.xq,scoresLists);
            }
        }
    }
}
