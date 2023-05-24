package com.wyu.config;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wyu.data.CourseCard;
import com.wyu.data.CourseList;
import com.wyu.data.DataConf;
import com.wyu.data.ScoresList;
import com.wyu.model.CourseVO;
import com.wyu.util.CommonUtil;
import com.wyu.util.MyApplication;

public class ContextHolder {
    public static Map<String, Map<Integer, CourseVO>> data;

    public static String currentTerm; // 当前学期
    public static int currentWeek; // 当前学期第几周
    public static int dayOfWeek; // 当天星期几


    public static List<String> allSemesters;        //所有学期列表
    public static List<String> myCourseTableList;   //课程表学期列表
    public static List<String> myScoresListsList;    //成绩学期列表
    public static Map<String, CourseList> allCourse; //所有课程列表
    public static Map<String, CourseList> diyCourse; //所有自定义课程列表
    public static Map<String, ScoresList> allScores;  //所有成绩列表

    public static Map<String, List<CourseCard>> courseCards; //课程表显示信息
    public static List<DataConf> courseConf;               //课表存储路径
    public static List<DataConf> scoresConf;               //成绩存储路径


    public static int curSemPos;      //当前选中学期的位置,一般选列表最后那个吧

    public static int semWeekStart;  //第一周的位置
    public static int semWeekNow;    //学期的第几周


    public static String firstYear;    //入学年份
    public static int firstYearPos;    //入学年份位置
    public static String userNumber;   //学号
    public static String password;     //密码

    public static DisplayMetrics dm = MyApplication.getContext().getResources().getDisplayMetrics();
    public static int screenWidth;     //屏幕宽
    public static int screenHeight;    //屏幕高


    static {
        data = new HashMap<>();
        currentTerm = CommonUtil.getCurrentTerm();
        currentWeek = 1;
        dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;

        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        Log.i(MyState.TAG, "屏幕长" + screenWidth + "屏幕宽" + screenHeight);


        myCourseTableList = new ArrayList<>();
        myScoresListsList = new ArrayList<>();
        allCourse = new HashMap<>();
        diyCourse = new HashMap<>();
        allScores = new HashMap<>();
        courseConf = new ArrayList<>();
        scoresConf = new ArrayList<>();
        courseCards = new HashMap<>();
        curSemPos = 1;
        semWeekStart = 0;  //第一周的位置
        semWeekNow = 4;    //当前第几周
        firstYear = "";    //入学年份
        firstYearPos = 0;
        userNumber = "";
        password = "";
    }

    public static void resetUserInfo(String xh) {
        //重置
        myCourseTableList = new ArrayList<>();
        myScoresListsList = new ArrayList<>();
        allCourse = new HashMap<>();
        diyCourse = new HashMap<>();
        allScores = new HashMap<>();
        courseConf = new ArrayList<>();
        scoresConf = new ArrayList<>();
        userNumber = xh;
        password = "";

    }

    /**
     * dp转为px
     *
     * @param context  上下文
     * @param dipValue dp值
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }

}
