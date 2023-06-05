package com.wyu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.kelin.scrollablepanel.library.ScrollablePanel;
import com.wyu.*;
import com.wyu.adapter.CourseGridPanelAdapter;
import com.wyu.adapter.CourseTodayListAdapter;
import com.wyu.adapter.CustomPagerAdapter;
import com.wyu.config.ContextHolder;
import com.wyu.constant.MyState;
import com.wyu.model.CourseVO;
import com.wyu.request.CourseModule;
import com.wyu.request.LoginModule;
import com.wyu.request.ScoreModule;
import com.wyu.util.CommonUtil;
import com.wyu.constant.Constant;
import com.wyu.util.FileUtil;
import com.wyu.util.MyApplication;
import com.wyu.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private Toolbar toolbar;

    private ActionBarDrawerToggle drawerToggle;
    private CoordinatorLayout coordinatorLayout;
    private NavigationView navigationView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private CourseGridPanelAdapter courseGridPanelAdapter;
    private ScrollablePanel scrollablePanel;
    private RecyclerView recyclerView;
    private CourseTodayListAdapter courseTodayListAdapter;

    CourseModule courseModule;
    ScoreModule scoreModule;

    public Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyState.OTHER_TERM:
                    String currentTerm = CommonUtil.getCurrentTerm();
                    getOtherTermCoursesAndScore(currentTerm);
                    Toast.makeText(MyApplication.getContext(), "其他学期数据加载完成", Toast.LENGTH_LONG).show();
                    ActionMenuItemView item = findViewById(R.id.ic_mark);
                    item.setClickable(true);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(MyState.TAG, "主界面创建");
        FileUtil.getInfo();
        toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_cl);
        tabLayout = (TabLayout) findViewById(R.id.main_tab);
        viewPager = (ViewPager) findViewById(R.id.main_pageview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.main_nav_view);

        initView();
        relateTabAndViewPager();
        initViewPager();
        initGetInfoModule();
    }

    private void initGetInfoModule() {
        courseModule = new CourseModule(null);
        scoreModule = new ScoreModule(null);
    }

    private void initView() {
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                WindowManager windowManager = (WindowManager) getSystemService(getApplicationContext().WINDOW_SERVICE);
                Display display = windowManager.getDefaultDisplay();
                coordinatorLayout.layout(navigationView.getRight(), 0, display.getWidth() + navigationView.getRight(), display.getHeight());
                super.onDrawerSlide(drawerView, slideOffset);
            }

        };
        drawerLayout.addDrawerListener(drawerToggle);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
        navigationView.setCheckedItem(R.id.nav_import);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_import:
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivityForResult(intent, 1);
                        break;
                    case R.id.nav_scores:
                        startActivity(new Intent(MainActivity.this, ScoreListActivity.class));
                        break;
                    case R.id.nav_chat:
                        startActivity(new Intent(MainActivity.this, ChatActivity.class));
                        break;
                    case R.id.nav_info:
                        new AlertDialog.Builder(MainActivity.this).setTitle("关于").setMessage("https://github.com/novohit").setPositiveButton("确定", null).show();
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_custom_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.ic_cur_week:
                if (courseGridPanelAdapter.getSelectedWeek() != ContextHolder.currentWeek) {
                    updateCourseGrid(ContextHolder.currentWeek);
                    tabLayout.getTabAt(1).setText("第" + (ContextHolder.currentWeek) + "周");
                }
                break;
            case R.id.ic_mark:
                String selectedTerm = courseGridPanelAdapter.getSelectedTerm();
                int lastIndex = 0;
                for (int i = 0; i < Constant.SELECTED_TERM_LIST.length; i++) {
                    if (Constant.SELECTED_TERM_LIST[i].equals(selectedTerm)) {
                        lastIndex = i;
                    }
                }
                new AlertDialog.Builder(MainActivity.this).setTitle("选择要查看的学期").setSingleChoiceItems(Constant.SELECTED_TERM_LIST, lastIndex, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        updateCourseGrid(Constant.SELECTED_TERM_LIST[which], courseGridPanelAdapter.getSelectedWeek());
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", null).show();
                break;
            case R.id.ic_settings:
                ToastUtil.show("TODO");
                break;
            default:
        }
        return true;
    }

    //Tab和PageView关联
    private void relateTabAndViewPager() {
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        tabLayout.getTabAt(1).setText("第" + (ContextHolder.currentWeek) + "周");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    new AlertDialog.Builder(MainActivity.this).setTitle("选择要查看的周").setSingleChoiceItems(Constant.SELECTED_WEEK_LIST, courseGridPanelAdapter.getSelectedWeek() - 1 >= 0 ? courseGridPanelAdapter.getSelectedWeek() - 1 : 0, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            updateCourseGrid(which + 1);
                            tabLayout.getTabAt(1).setText("第" + (which + 1) + "周");
                            dialog.dismiss();
                        }
                    }).setNegativeButton("取消", null).show();
                }
            }
        });
    }

    //ViewPager相关
    private void initViewPager() {
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.fragment_course_list, null);
        View view2 = inflater.inflate(R.layout.fragment_course_table, null);
        //初始化子布局
        initCourseList(view1);
        initCourseGrid(view2);
        List<View> tabViewList = new ArrayList<>();
        tabViewList.add(view1);
        tabViewList.add(view2);
        CustomPagerAdapter pagerAdapter = new CustomPagerAdapter(tabViewList);
        viewPager.setAdapter(pagerAdapter);
    }

    private void initCourseList(View view) {

        courseTodayListAdapter = new CourseTodayListAdapter();
        recyclerView = (RecyclerView) view.findViewById(R.id.course_list_recycler_view);
        recyclerView.setAdapter(courseTodayListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    private void initCourseGrid(View view) {
        courseGridPanelAdapter = new CourseGridPanelAdapter(CommonUtil.getCurrentTerm(), ContextHolder.currentWeek);
        scrollablePanel = (ScrollablePanel) view.findViewById(R.id.scrollable_panel);
        scrollablePanel.setPanelAdapter(courseGridPanelAdapter);
    }


    public void updateCourseGrid(int week) {
        courseGridPanelAdapter.setSelectedWeek(week);
        scrollablePanel.notifyDataSetChanged();
    }

    public void updateCourseGrid(String term) {
        courseGridPanelAdapter.setSelectedTerm(term);
        scrollablePanel.notifyDataSetChanged();
    }

    public void updateCourseGrid(String term, int week) {
        courseGridPanelAdapter.setSelectedTerm(term);
        scrollablePanel.notifyDataSetChanged();
        courseGridPanelAdapter.setSelectedWeek(week);
        scrollablePanel.notifyDataSetChanged();
    }

    public void updateCourseList() {
        courseTodayListAdapter.updateList();
        courseTodayListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                int state = data.getIntExtra("state", -1);
                // 登录成功
                if (state >= 0) {
                    Message msg = new Message();
                    msg.what = MyState.OTHER_TERM;
                    handler.sendMessage(msg);

                    ActionMenuItemView item = findViewById(R.id.ic_mark);
                    item.setClickable(false);

                    Log.i(MyState.TAG, "onActivityResult");
                    String currentTerm = CommonUtil.getCurrentTerm();
                    Map<Integer, CourseVO> currentTermData = ContextHolder.courseData.get(currentTerm);
                    assert currentTermData != null;
                    CourseVO courseVO = currentTermData.get(1);
                    assert courseVO != null;
                    Map<String, String> date = courseVO.getDate();
                    String startStr = date.get("7");
                    int currentWeek = CommonUtil.getCurrentWeek(startStr);
                    ContextHolder.currentWeek = currentWeek;
                    tabLayout.getTabAt(1).setText("第" + currentWeek + "周");
                    // TODO bug 账户切换后数据视图没有更新
                    updateCourseGrid(currentTerm, currentWeek);
                    updateCourseList();
                }
            }
        }
    }

    private void getOtherTermCoursesAndScore(String defaultTerm) {
        ExecutorService pool = Executors.newFixedThreadPool(4, new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "pool-" + threadNumber.getAndIncrement());
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
                //pool.execute(() -> {
                    courseModule.getCourseList(term);
                    Log.i(MyState.TAG, "course " + term + " complete");
                //});
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //pool.execute(() -> {
                    scoreModule.getScoresList(term);
                    Log.i(MyState.TAG, "score " + term + " complete");
                //});
            }
        }
    }

    @Override
    protected void onStop() {
        // TODO 设置一个flag 不用每次都保存
        FileUtil.saveInfo();
        super.onStop();
    }

    /**
     * 现在全屏手机退出后台后不一定会执行onDestroy 跟手机厂商有关
     */
//    @Override
//    protected void onDestroy() {
//        FileUtil.saveInfo();
//        super.onDestroy();
//    }
}
