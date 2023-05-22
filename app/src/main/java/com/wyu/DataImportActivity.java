package com.wyu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wyu.data.CourseCard;
import com.wyu.data.CourseList;
import com.wyu.data.CourseTableDivide;
import com.wyu.data.DataConf;
import com.wyu.data.ScoresList;
import com.wyu.R;
import com.wyu.config.ContextHolder;
import com.wyu.config.Form;
import com.wyu.config.MyState;
import com.wyu.util.ToastUtil;

public class DataImportActivity extends AppCompatActivity {

    private List<View> tabViews;
    private TabLayout mTabTl;
    private ViewPager mViewPager;

    private AppCompatSpinner spStartYear;
    private List<String> stYearList;
    private int stYearPos, stSemesterPos, edSemesterPos;


    private ImageView loginIVerify;
    private TextInputEditText loginUserNumber;
    private TextInputEditText loginPassword;
    private TextInputEditText loginVerifyCode;

    LoginModule loginModule;
    CourseTableModule courseTableModule;
    ScoresListModule scoresListModule;
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        boolean beginCount = false;
        int sum, cnt, suc;

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MyState.GET_VERIFYCODE_SUCCESSFUL:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    loginIVerify.setImageBitmap(bitmap);
                    break;
                case MyState.LOGIN_FAILED:
                    ToastUtil.show("登录失败,检查学号、密码、验证码是否正确");
                    loginModule.getVerifyCodeImage();
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
                    if (!Form.getAccount().equals(ContextHolder.userNumber)) {
                        ContextHolder.resetUserInfo(Form.getAccount());
                    }
                    ContextHolder.firstYearPos = stYearPos;
                    ContextHolder.firstYear = spStartYear.getSelectedItem().toString();

                    beginCount = true;
                    cnt = suc = 0;

                    String str = "202201";
                    courseTableModule.getCourseList(str);
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(ContextHolder.data);
                    scoresListModule.getScoresList(str);
                    Log.i(MyState.TAG, "正在获取学期：" + str);

                    break;
                case MyState.SUCCESS:
                    suc++;
                    String kind = msg.getData().getString("kind");
                    DataConf dataConf = (DataConf) msg.getData().getSerializable("dataConf");
                    if ("courseTable".equals(kind)) {
                        if (!ContextHolder.myCourseTableList.contains(dataConf.xq)) {
                            ContextHolder.myCourseTableList.add(dataConf.xq);
                            ContextHolder.courseConf.add(dataConf);
                        }
                        CourseList courseList = (CourseList) msg.getData().getSerializable("data");
                        ContextHolder.allCourse.put(dataConf.xq, courseList);
                        List<CourseCard> courseCards = CourseTableDivide.courseTableDivide(courseList.getTable());
                        ContextHolder.courseCards.put(dataConf.xq, courseCards);
                        Log.i(MyState.TAG, "nnn课程数" + ContextHolder.myCourseTableList.size());
                    } else if ("scoresList".equals(kind)) {
                        if (!ContextHolder.myScoresListsList.contains(dataConf.xq)) {
                            ContextHolder.myScoresListsList.add(dataConf.xq);
                            ContextHolder.scoresConf.add(dataConf);
                        }
                        ScoresList scoresList = (ScoresList) msg.getData().getSerializable("data");
                        ContextHolder.allScores.put(dataConf.xq, scoresList);
                    }
                    ToastUtil.show("获取成功!文件：" + dataConf.path);
                    Log.i(MyState.TAG, "获取成功!文件：" + dataConf.path);
                    break;
                default:
            }
            if (beginCount) {
                cnt++;
                if (cnt >= sum + 1) {
                    beginCount = false;
                    sum = cnt = 0;
                    Log.i(MyState.TAG, "zzz课程数" + ContextHolder.myCourseTableList.size());
                    Collections.sort(ContextHolder.myCourseTableList);
                    Collections.sort(ContextHolder.myScoresListsList);
                    ContextHolder.curSemPos = ContextHolder.myCourseTableList.size() - 1;
                    Log.i(MyState.TAG, "当前学期" + ContextHolder.curSemPos);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_import);
        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        mTabTl = (TabLayout) findViewById(R.id.data_ipt_tab);
        mViewPager = (ViewPager) findViewById(R.id.data_ipt_pageview);

        toolbar.setTitle("数据导入");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
        // 登录相关
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
        courseTableModule = new CourseTableModule(handler);
        scoresListModule = new ScoresListModule(handler);
    }

    //Tab和PageView关联
    private void relateTabAndViewPager() {
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabTl));
        mTabTl.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    //ViewPager相关
    private void initViewPager() {

        //------------------------ViewPager用法----------------------------------------------
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.fragment_login, null);
        View view2 = inflater.inflate(R.layout.fragment_course_table, null);
        initLogin(view1);
        tabViews = new ArrayList<View>();
        tabViews.add(view1);
        tabViews.add(view2);
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return tabViews.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(tabViews.get(position));
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(tabViews.get(position));
                return tabViews.get(position);
            }
        };
        mViewPager.setAdapter(pagerAdapter);
        //=========================================================================
    }

    private void initLogin(View view) {

        loginIVerify = (ImageView) view.findViewById(R.id.iv_verify_code);  //注意要用view.findViewById()
        loginUserNumber = (TextInputEditText) view.findViewById(R.id.et_user_number);
        loginPassword = (TextInputEditText) view.findViewById(R.id.et_password);
        loginVerifyCode = (TextInputEditText) view.findViewById(R.id.et_verify_code);
        spStartYear = (AppCompatSpinner) view.findViewById(R.id.sp_start_year);

        if (loginUserNumber.getText().length() == 0) {
            loginUserNumber.setText(ContextHolder.userNumber);
        }
        stYearList = new ArrayList<>();
/*        for(int i = 0; i <= 7; ++i){
            stYearList.add(String.valueOf(EnvironmentPool.currentYear-6+i));
        }*/
        stYearList.add("202201");
        stYearList.add("202202");

        //---------------------------------spinner用法
        ArrayAdapter<String> stYearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stYearList);
        stYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStartYear.setAdapter(stYearAdapter);
        if (ContextHolder.firstYear.length() == 4) {
            ContextHolder.firstYearPos = stYearList.indexOf(ContextHolder.firstYear);
            spStartYear.setSelection(ContextHolder.firstYearPos);
        }
        //Log.i(MyState.TAG,spStartYear.getPrompt().toString());
        spStartYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //选择列表项的操作

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //未选中时候的操作
            }
        });
        loginModule.getVerifyCodeImage();

    }

    //=============================================================================================
    //Login相关
    public void onClickLogin(View view) {
        stYearPos = spStartYear.getSelectedItemPosition();

        Log.i(MyState.TAG, stYearPos + " " + stSemesterPos + " " + edSemesterPos + "");
        String userName = loginUserNumber.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();
        String verifyCode = loginVerifyCode.getText().toString().trim();
        if ((!"".equals(userName)) && (!"".equals(password)) && (!"".equals(verifyCode))) {
            ToastUtil.show("正在获取");
            loginModule.studentSubmit(userName, password, verifyCode);
            Log.i(MyState.TAG, userName + " " + password + " " + verifyCode + " " + Form.getCookies());
        } else {
            ToastUtil.show("请将信息输入完整");
        }

    }

    public void refreshVerifyCode(View view) {
        //ToastUtil.show("获取验证码");
        loginModule.getVerifyCodeImage();
    }
}
