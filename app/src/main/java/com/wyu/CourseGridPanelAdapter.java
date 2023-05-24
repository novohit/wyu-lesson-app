package com.wyu;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.wyu.model.Course;
import com.wyu.model.CourseVO;
import com.wyu.config.ContextHolder;
import com.wyu.util.MyApplication;
import com.kelin.scrollablepanel.library.PanelAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseGridPanelAdapter extends PanelAdapter {

    private int selectedWeek; //当前周

    private String selectedTerm;

    private Map<Integer, CourseVO> selectedTermCourses;


    private static final int firstWidth = ContextHolder.dip2px(MyApplication.getContext(), 18);
    private static final int firstHeight = ContextHolder.dip2px(MyApplication.getContext(), 20);
    private static final int normalHeight = ContextHolder.dip2px(MyApplication.getContext(), 110);
    private static final int normalWidth = (ContextHolder.screenWidth - firstWidth) / 7;
    private static final int[] colors = new int[]{R.color.cadetblue, R.color.fuchsia, R.color.orange, R.color.brown
            , R.color.purple, R.color.plum, R.color.green, R.color.limegreen
            , R.color.slateblue, R.color.aliceblue, R.color.blanchedalmond, R.color.firebrick
            , R.color.lime, R.color.yellow, R.color.wheat, R.color.khaki
            , R.color.mistyrose, R.color.chartreuse, R.color.aqua, R.color.palevioletred};
    public static final String[] weekList = new String[]{"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private static final String[] sectionList = new String[]{"", "01\n02", "03\n04", "05\n06", "07\n08", "09\n10", "11\n12"};

    private Course[][] grid = new Course[sectionList.length][weekList.length];

    public void setSelectedWeek(int selectedWeek) {
        selectedTermCourses = ContextHolder.data.get(selectedTerm);
        this.selectedWeek = selectedWeek;
        Log.i("week", "切换为第" + selectedWeek + "周");
        updateGrid();
    }

    public int getSelectedWeek() {
        return selectedWeek;
    }

    public void setSelectedTerm(String selectedTerm) {
        selectedTermCourses = ContextHolder.data.get(selectedTerm);
        updateGrid();
    }

    private void updateGrid() {
        grid = new Course[sectionList.length][weekList.length];
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
                    grid[row][Integer.parseInt(course.getDayOfWeek())] = course;
                }
            }
        }
    }

    public CourseGridPanelAdapter(String selectedTerm, int selectedWeek) {
        this.selectedWeek = selectedWeek;
        this.selectedTerm = selectedTerm;
        if (selectedTerm == null || ContextHolder.data == null || ContextHolder.data.get(selectedTerm) == null) {
            selectedTermCourses = new HashMap<>();
            ContextHolder.data.put(selectedTerm, selectedTermCourses);
        } else {
            selectedTermCourses = ContextHolder.data.get(selectedTerm);
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
        if (column == ContextHolder.dayOfWeek && selectedWeek == ContextHolder.currentWeek) {
            titleViewHolder.titleLinearlayout.setBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.lightskyblue));
        } else {
            titleViewHolder.titleLinearlayout.setBackgroundColor(0x00000000);
        }
        if (row == 0 && column == 0) {
            titleViewHolder.params.width = firstWidth;
            titleViewHolder.params.height = firstHeight;
            titleViewHolder.titleLinearlayout.setLayoutParams(titleViewHolder.params);
            titleViewHolder.titleTextView.setText("");
        } else if (row == 0) {
            titleViewHolder.params.width = normalWidth;
            titleViewHolder.params.height = firstHeight;
            titleViewHolder.titleLinearlayout.setLayoutParams(titleViewHolder.params);
            titleViewHolder.titleCardView.setMinimumWidth(50);
            titleViewHolder.titleTextView.setText(weekList[column]);
        } else if (column == 0) {
            titleViewHolder.params.width = firstWidth;
            titleViewHolder.params.height = normalHeight;
            titleViewHolder.titleLinearlayout.setLayoutParams(titleViewHolder.params);
            titleViewHolder.titleTextView.setText(sectionList[row]);
        } else {
            titleViewHolder.params.width = normalWidth;
            titleViewHolder.params.height = normalHeight;
            titleViewHolder.titleLinearlayout.setLayoutParams(titleViewHolder.params);
            // 有课
            if (course != null) {
                int id = 1;
                try {
                    id = Integer.parseInt(course.getId());
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
                titleViewHolder.titleCardView.setCardBackgroundColor(MyApplication.getContext().getResources().getColor(colors[id % colors.length]));

//                if (courseCards.get(loc).zhou[currentWeek]) {
//                    titleViewHolder.titleCardView.setCardBackgroundColor(MyApplication.getContext().getResources().getColor(colors[courseCards.get(loc).no % 20]));
//                } else {
//                    titleViewHolder.titleCardView.setCardBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.silver));
//                }
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