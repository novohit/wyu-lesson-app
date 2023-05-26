package com.wyu.request;

import android.os.Handler;
import android.util.Log;

import com.wyu.model.CourseVO;
import com.wyu.config.ContextHolder;
import com.wyu.util.*;
import com.google.gson.Gson;


import com.wyu.constant.RequestInfo;
import com.wyu.constant.MyState;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CourseModule extends RequestInfo {
    private Handler handler;

    public CourseModule(Handler handler) {
        this.handler = handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }

    public void getCourseList(String term) {
        ExecutorService pool = Executors.newFixedThreadPool(5, new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "novo-pool-" + threadNumber.getAndIncrement());
                if (t.isDaemon())
                    t.setDaemon(false);
                if (t.getPriority() != Thread.NORM_PRIORITY)
                    t.setPriority(Thread.NORM_PRIORITY);
                return t;
            }
        });
        for (int i = 1; i <= 20; i++) {
            int j = i;
            pool.execute(() -> {
                getCourseList(term, j, account);
            });
        }
    }

    public void getCourseList(String term, int week, String account) {
        String url = URL_COURSE + String.format("?term=%s&week=%d", term, week);
        Log.i(MyState.TAG, url);
        new CustomHttp(handler) {
            @Override
            protected void success(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Resp resp = CommonUtil.getResp(response);
                    Log.i("course " + week, resp.getData());

                    CourseVO courseVO = new Gson().fromJson(resp.getData(), new TypeToken<CourseVO>() {
                    }.getType());
                    Map<Integer, CourseVO> map = ContextHolder.courseData.get(term);
                    if (map == null) {
                        map = new HashMap<>();
                        ContextHolder.courseData.put(term, map);
                    }
                    map.put(week, courseVO);
                    Log.i("coursevo ", String.valueOf(courseVO.getCourseCount()));
                }
            }
        }.get(url, RequestInfo.cookies);
    }
}
