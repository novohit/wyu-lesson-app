package com.wyu.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.wyu.*;
import com.wyu.adapter.CustomPagerAdapter;
import com.wyu.config.ContextHolder;
import com.wyu.constant.RequestInfo;
import com.wyu.constant.MyState;
import com.wyu.request.CourseModule;
import com.wyu.request.LoginModule;
import com.wyu.request.ScoreModule;
import com.wyu.util.CommonUtil;
import com.wyu.constant.Constant;
import com.wyu.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;


    private ImageView captchaImg;
    private TextInputEditText studentNumber;
    private TextInputEditText password;
    private TextInputEditText captcha;

    LoginModule loginModule;
    CourseModule courseModule;
    ScoreModule scoreModule;

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        boolean beginCount = false;
        int sum, cnt, suc;

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MyState.GET_VERIFYCODE_SUCCESSFUL:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    captchaImg.setImageBitmap(bitmap);
                    break;
                case MyState.LOGIN_FAILED:
                    ToastUtil.show("登录失败,检查学号、密码、验证码是否正确");
                    loginModule.getCaptcha();
                    break;
                case MyState.CONNECTION_ERROR:
                    ToastUtil.show("网络开小差了~");
                    break;
                case MyState.FAILED:
                    ToastUtil.show((String) msg.obj);
                    break;
                case MyState.STRING_ERROR:
                    ToastUtil.show("字符串错误：" + (String) msg.obj);
                    break;
                case MyState.FILE_ERROR:
                    ToastUtil.show("文件错误：" + (String) msg.obj);
                    break;
                case MyState.INCORRECT_HTML:
                    ToastUtil.show("网页处理错误：" + (String) msg.obj);
                    break;
                case MyState.JSON_ERROR:
                    ToastUtil.show("JSON处理错误：" + (String) msg.obj);
                    Log.i(MyState.TAG, "获取失败！");
                    break;
                case MyState.LOGIN_SUCCESSFUL:
                    beginCount = true;
                    cnt = suc = 0;

                    String currentTerm = CommonUtil.getCurrentTerm();

                    long start = System.currentTimeMillis();
                    courseModule.getCourseList(currentTerm);
                    scoreModule.getScoresList(currentTerm);
                    getOtherTermCoursesAndScore(currentTerm);
                    while (ContextHolder.courseData.get(currentTerm) == null || ContextHolder.courseData.get(currentTerm).size() != 20) {

                    }
                    Log.i(MyState.TAG, "耗时：" + (System.currentTimeMillis() - start) + "ms");
                    Log.i(MyState.TAG, "正在获取学期：" + currentTerm);
                    break;
                case MyState.SUCCESS:
                    suc++;
                    break;
                default:
            }
            if (beginCount) {
                cnt++;
                if (cnt >= sum + 1) {
                    beginCount = false;
                    sum = cnt = 0;
                    ToastUtil.show("获取完成,共" + sum + "个,成功" + suc + "个");
                    Log.i(MyState.TAG, "接收完成");
                    //写入配置文件
                    Intent intent = new Intent();
                    intent.putExtra("state", suc);
                    setResult(1, intent);
                    finish();
                }
            }

        }
    };

    private void getOtherTermCoursesAndScore(String defaultTerm) {
        ExecutorService pool = Executors.newFixedThreadPool(4, new ThreadFactory() {
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
        for (int i = 0; i < Constant.SELECTED_TERM_LIST.length; i++) {
            String term = Constant.SELECTED_TERM_LIST[i];
            if (!term.equals(defaultTerm)) {
                pool.execute(() -> {
                    Log.i(MyState.TAG, "正在查询" + term + "的课表");
                    courseModule.getCourseList(term);
                    scoreModule.getScoresList(term);
                });
            }
        }
    }

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_import);
        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.data_ipt_tab);
        viewPager = (ViewPager) findViewById(R.id.data_ipt_pageview);

        toolbar.setTitle("登录");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initGetInfoModule();
        initViewPager();
        relateTabAndViewPager();

    }

    //toolbar相关
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initGetInfoModule() {
        loginModule = new LoginModule(handler);
        courseModule = new CourseModule(handler);
        scoreModule = new ScoreModule(handler);
    }


    private void relateTabAndViewPager() {
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }


    private void initViewPager() {
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.fragment_login, null);
        View view2 = inflater.inflate(R.layout.fragment_course_table, null);
        initLogin(view1);
        List<View> viewList = new ArrayList<>();
        viewList.add(view1);
        viewList.add(view2);
        CustomPagerAdapter pagerAdapter = new CustomPagerAdapter(viewList);
        viewPager.setAdapter(pagerAdapter);
    }

    private void initLogin(View view) {

        captchaImg = (ImageView) view.findViewById(R.id.iv_verify_code);  //注意要用view.findViewById()
        studentNumber = (TextInputEditText) view.findViewById(R.id.et_user_number);
        password = (TextInputEditText) view.findViewById(R.id.et_password);
        captcha = (TextInputEditText) view.findViewById(R.id.et_verify_code);
        loginModule.getCaptcha();

    }


    public void onClickLogin(View view) {

        String userName = studentNumber.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        String verifyCode = captcha.getText().toString().trim();
        if ((!"".equals(userName)) && (!"".equals(password)) && (!"".equals(verifyCode))) {
            ToastUtil.show("正在获取");
            loginModule.studentSubmit(userName, password, verifyCode);
            Log.i(MyState.TAG, userName + " " + password + " " + verifyCode + " " + RequestInfo.getCookies());
        } else {
            ToastUtil.show("请将信息输入完整");
        }

    }

    public void refreshCaptcha(View view) {
        //ToastUtil.show("获取验证码");
        loginModule.getCaptcha();
    }
}
