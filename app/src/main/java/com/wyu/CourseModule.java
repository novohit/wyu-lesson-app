package com.wyu;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.wyu.model.CourseVO;
import com.wyu.config.ContextHolder;
import com.wyu.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import com.wyu.data.Course;
import com.wyu.data.CourseList;
import com.wyu.config.RequestInfo;
import com.wyu.config.MyState;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        new MyOkHttp2(handler) {
            @Override
            protected void responsedSolve(Call call, Response response) throws IOException {
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
//                    String html = response.body().string();
//                    String path = "courseTable_" + account + "_" + week + ".json";
//                    //Log.i(MyState.TAG,html);
//                    //System.out.println(path);
//                    CourseList courseList = storeCourseList(html, account, path);
//                    if (courseList != null) {
//                        Message msg = new Message();
//                        Bundle bundle = new Bundle();
//                        DataConf dataConf = new DataConf(account, String.valueOf(week), path);
//                        bundle.putString("kind", "courseTable");
//                        bundle.putSerializable("dataConf", dataConf);
//                        bundle.putSerializable("data", courseList);
//                        msg.what = MyState.SUCCESS;
//                        msg.setData(bundle);
//                        handler.sendMessage(msg);
//                    }
                }
            }
        }.get(url, RequestInfo.cookies);
    }

    public CourseList storeCourseList(String html, String xh, String path) {
        Document doc = Jsoup.parse(html);
        Elements isRight = doc.select("div#mxhDiv");
        if (isRight.size() == 0) {
            Message msg = new Message();
            msg.what = MyState.INCORRECT_HTML;
            msg.obj = "获取课表失败！";
            handler.sendMessage(msg);
            return null;
        }
        Elements table = isRight.first().select("tr");
        if (table.size() == 0) {
            Message msg = new Message();
            msg.what = MyState.JSON_ERROR;
            msg.obj = "生成课表失败！";
            handler.sendMessage(msg);
            return null;
        }
        CourseList courseList = new CourseList();
        courseList.setInfo("ok");
        courseList.setXh(xh);
        List<Course> courses = new ArrayList<Course>();
        for (Element cur : table) {
            Course course = new Course();
            Elements part = cur.select("td");
            if (part.size() < 12) {
                Message msg = new Message();
                msg.what = MyState.JSON_ERROR;
                msg.obj = "生成课表失败！";
                handler.sendMessage(msg);
                return null;
            }
            course.setBanji(part.get(1).text());
            course.setRenshu(part.get(2).text());
            course.setBianhao(part.get(4).text());
            course.setKecheng("计算机网络");
            course.setJiaoshi(part.get(6).text());
            course.setYuanxi(part.get(7).text());
            course.setShijian(part.get(8).text());
            course.setDidian(part.get(9).text());
            course.setZhouci(part.get(10).text());
            course.setDanshuang(part.get(11).text());
            course.setFenzu(part.get(12).text());
            //System.out.println(course.toString());
            courses.add(course);
        }
        //System.out.println(courses.size());
        courseList.setTable(courses);
        if (courses.size() > 0)
            courseList.setCode("ok");
        else
            courseList.setCode("-1");
        //Log.i(MyState.TAG,courseTable.toString());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();  //setPrettyPrinting()格式化json
        String json = gson.toJson(courseList);
        //Log.i(MyState.TAG,json);
        if (MyFileHelper.JsonToFile(json, path) != 0) {
            Message msg = new Message();
            msg.what = MyState.FILE_ERROR;
            msg.obj = "保存课表失败！";
            handler.sendMessage(msg);
            return null;
        }
        return courseList;
    }

    public void getPersonTable(String xq) {
        getPersonTable(xq, "");
    }

    public void getPersonTable(String xq, String zc) {
        String url = personTableBaseURL + "&istsxx=no&xnxqh=" + xq + "&zc=" + zc + "&xs0101id=" + account;
        System.out.println("课表网址：" + url);
        new MyOkHttp2(handler) {
            @Override
            protected void responsedSolve(Call call, Response response) throws IOException {
                byte[] ff = response.body().bytes();
                System.out.println(new String(ff));
                File file = new File("D:\\test.html");
                FileOutputStream out = new FileOutputStream(file);
                out.write(ff);
                out.close();
                System.out.println("文件输出完成");
            }
        }.get(url, cookies);
    }

}
