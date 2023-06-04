package com.wyu.adapter;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.wyu.R;
import com.wyu.model.Course;
import com.wyu.model.CourseVO;
import com.wyu.config.ContextHolder;
import com.wyu.constant.Constant;
import com.wyu.util.MyApplication;
import com.kelin.scrollablepanel.library.PanelAdapter;

import java.util.*;

public class CourseGridPanelAdapter extends PanelAdapter {

    private int selectedWeek;

    private String selectedTerm;

    private Map<Integer, CourseVO> selectedTermCourses;


    private static final int FIRST_WIDTH = ContextHolder.dip2px(MyApplication.getContext(), 25);
    private static final int FIRST_HEIGHT = ContextHolder.dip2px(MyApplication.getContext(), 40);
    private static final int NORMAL_HEIGHT = ContextHolder.dip2px(MyApplication.getContext(), 110);
    private static final int NORMAL_WIDTH = (ContextHolder.screenWidth - FIRST_WIDTH) / 7;

    private static final int[] COLORS = new int[]{R.color.cadetblue, R.color.fuchsia, R.color.orange
            , R.color.purple, R.color.plum, R.color.green, R.color.limegreen
            , R.color.slateblue, R.color.aliceblue, R.color.blanchedalmond
            , R.color.lime, R.color.yellow, R.color.wheat, R.color.khaki
            , R.color.mistyrose, R.color.chartreuse, R.color.aqua, R.color.palevioletred};


    private Course[][] grid = new Course[Constant.SECTION_LIST.length][Constant.DAY_OF_WEEK_LIST.length];

    public void setSelectedWeek(int selectedWeek) {
        selectedTermCourses = ContextHolder.courseData.get(selectedTerm);
        this.selectedWeek = selectedWeek;
        if (selectedTermCourses == null) {
            selectedTermCourses = new HashMap<>();
            Log.i("week", "切换为第" + selectedWeek + "周");
        }
        updateGrid();
    }

    public int getSelectedWeek() {
        return selectedWeek;
    }

    public void setSelectedTerm(String selectedTerm) {
        selectedTermCourses = ContextHolder.courseData.get(selectedTerm);
        this.selectedTerm = selectedTerm;
        if (selectedTermCourses == null) {
            selectedTermCourses = new HashMap<>();
            Log.i("week", "切换为第" + selectedTerm + "学期");
        }
        updateGrid();
    }

    public String getSelectedTerm() {
        return selectedTerm;
    }

    private void updateGrid() {
        grid = new Course[Constant.SECTION_LIST.length][Constant.DAY_OF_WEEK_LIST.length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = null;
            }
        }

        CourseVO currentWeekCourses = selectedTermCourses.get(selectedWeek);
        if (currentWeekCourses != null && currentWeekCourses.getCourseList() != null) {
            List<Course> courseList = currentWeekCourses.getCourseList();
            for (Course course : courseList) {
                String section = course.getSection();
                List<Integer> rows = getRow(section);
                for (Integer row : rows) {
                    // 有人的课程可能会有冲突
                    Course last = grid[row][(Integer.parseInt(course.getDayOfWeek()) + 1) % 7];
                    if (last != null) {
                        last.setName(last.getName() + "\n" + course.getName());
                        //last.setLocation(last.getLocation() + "\n" + course.getLocation());
                        last.conflict = true;
                    } else {
                        grid[row][(Integer.parseInt(course.getDayOfWeek()) + 1) % 7] = course;
                    }
                }
            }
        }
    }

    public CourseGridPanelAdapter(String selectedTerm, int selectedWeek) {
        this.selectedWeek = selectedWeek;
        this.selectedTerm = selectedTerm;
        // 初始化时 如果conf文件有 说明有缓存
        if (ContextHolder.courseData.get(selectedTerm) != null) {
            selectedTermCourses = ContextHolder.courseData.get(selectedTerm);
        } else {
            selectedTermCourses = new HashMap<>();
            ContextHolder.courseData.put(selectedTerm, selectedTermCourses);
        }
        updateGrid();
    }

    private List<Integer> getRow(String section) {
        String[] split = section.split(",");
        List<Integer> res = new ArrayList<>();
        if (split.length < 2) {
            return res;
        }
        // "09,10,11,12"
        Log.i("section", section);
        for (int i = 0; i < split.length; i++) {
            if ((i & 1) == 1) {
                res.add(Integer.parseInt(split[i]) / 2);
            }
        }
        return res;
    }

    @Override
    public int getRowCount() {
        return grid.length;
    }

    @Override
    public int getColumnCount() {
        return grid[0].length;
    }

    @Override
    public int getItemViewType(int row, int column) {
        return super.getItemViewType(row, column);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int row, int column) {
        //Log.i(MyState.TAG, "grid onBindViewHolder" + "(" + row + "," + column + ")");
        TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
        Course course = grid[row][column];
        //titleViewHolder.params.height;//设置当前控件布局的高度
        //titleViewHolder.titleLinearlayout.setLayoutParams(titleViewHolder.params);//将设置好的布局参数应用到控件中
        // 当天高亮
        if (((column - 1) == 0 ? 7 : (column - 1)) == ContextHolder.dayOfWeek && selectedWeek == ContextHolder.currentWeek) {
            titleViewHolder.titleLinearlayout.setBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.lightskyblue));
        } else {
            titleViewHolder.titleLinearlayout.setBackgroundColor(0x00000000);
        }
        if (row == 0 && column == 0) {
            titleViewHolder.params.width = FIRST_WIDTH;
            titleViewHolder.params.height = FIRST_HEIGHT;
            titleViewHolder.titleLinearlayout.setLayoutParams(titleViewHolder.params);
            titleViewHolder.titleTextView.setText(Calendar.getInstance().get(Calendar.MONTH) + 1 + "月");
        } else if (row == 0) {
            titleViewHolder.params.width = NORMAL_WIDTH;
            titleViewHolder.params.height = FIRST_HEIGHT;
            titleViewHolder.titleLinearlayout.setLayoutParams(titleViewHolder.params);
            titleViewHolder.titleCardView.setMinimumWidth(50);
            String date = "";
            if (selectedTermCourses != null) {
                CourseVO courseVO = selectedTermCourses.get(selectedWeek);
                if (courseVO != null) {
                    Map<String, String> dateMap = courseVO.getDate();
                    // 日期左移一位
                    date = dateMap.get(String.valueOf((column - 1) == 0 ? 7 : (column - 1)));
                    date = date.substring(5);
                }
            }
            titleViewHolder.titleTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            titleViewHolder.titleTextView.setText(Constant.DAY_OF_WEEK_LIST[column] + "\n" + date);
        } else if (column == 0) {
            titleViewHolder.params.width = FIRST_WIDTH;
            titleViewHolder.params.height = NORMAL_HEIGHT;
            titleViewHolder.titleLinearlayout.setLayoutParams(titleViewHolder.params);
            titleViewHolder.titleTextView.setText(Constant.SECTION_LIST[row]);
        } else {
            titleViewHolder.params.width = NORMAL_WIDTH;
            titleViewHolder.params.height = NORMAL_HEIGHT;
            titleViewHolder.titleLinearlayout.setLayoutParams(titleViewHolder.params);
            // 有课
            if (course != null) {
                int id = 1;
                try {
                    id = Integer.parseInt(course.getId());
                } catch (NumberFormatException e) {
                    //throw new RuntimeException(e);
                }
                if (course.conflict) {
                    titleViewHolder.titleLinearlayout.setBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.tomato));
                }
                titleViewHolder.titleCardView.setCardBackgroundColor(MyApplication.getContext().getResources().getColor(COLORS[id % COLORS.length]));
                titleViewHolder.titleTextView.setText(course.getName() + "\n@" + course.getLocation());
            } else {
                // 无课
                titleViewHolder.titleTextView.setText("");
                titleViewHolder.titleCardView.setCardBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.white));
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CourseGridPanelAdapter.TitleViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_cell, parent, false));
    }

    private static class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public CardView titleCardView;
        public LinearLayout titleLinearlayout;
        public ViewGroup.LayoutParams params;

        public TitleViewHolder(View view) {
            super(view);
            this.titleTextView = (TextView) view.findViewById(R.id.item_course_cell_text);
            this.titleCardView = (CardView) view.findViewById(R.id.item_course_cell_card);
            this.titleLinearlayout = (LinearLayout) view.findViewById(R.id.item_course_cell_ll);
            this.params = (ViewGroup.LayoutParams) this.titleLinearlayout.getLayoutParams();//获取当前控件的布局对象
        }
    }
}