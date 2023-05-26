package com.wyu.config;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.wyu.constant.MyState;
import com.wyu.model.CourseVO;
import com.wyu.model.ScoreVO;
import com.wyu.util.CommonUtil;
import com.wyu.util.MyApplication;

public class ContextHolder {
    public static Map<String, Map<Integer, CourseVO>> courseData;

    public static Map<String, ScoreVO> scoreData;

    public static String currentTerm; // 当前学期
    public static int currentWeek; // 当前学期第几周
    public static int dayOfWeek; // 当天星期几

    public static DisplayMetrics dm = MyApplication.getContext().getResources().getDisplayMetrics();
    public static int screenWidth;     //屏幕宽
    public static int screenHeight;    //屏幕高


    static {
        courseData = new HashMap<>();
        scoreData = new HashMap<>();
        currentTerm = CommonUtil.getCurrentTerm();
        currentWeek = 1;
        dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;

        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        Log.i(MyState.TAG, "屏幕长" + screenWidth + "屏幕宽" + screenHeight);
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
