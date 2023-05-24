package com.wyu;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.wyu.config.ContextHolder;
import com.wyu.config.MyState;
import com.wyu.model.Course;
import com.wyu.model.CourseVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseTodayListAdapter extends RecyclerView.Adapter<CourseTodayListAdapter.ViewHolder> {

    private List<Course> todayCourseList;


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseName;
        TextView location;
        TextView week;
        TextView section;
        TextView teacher;
        TextView grade;

        public ViewHolder(View view) {
            super(view);
            courseName = (TextView) view.findViewById(R.id.today_course_name);
            location = (TextView) view.findViewById(R.id.today_course_location);
            week = (TextView) view.findViewById(R.id.today_course_week);
            section = (TextView) view.findViewById(R.id.today_course_section);
            teacher = (TextView) view.findViewById(R.id.today_course_teacher);
            grade = (TextView) view.findViewById(R.id.today_course_grader);
        }
    }

    public CourseTodayListAdapter() {
        updateList();
    }

    public void updateList() {
        String currentTerm = ContextHolder.currentTerm;
        int currentWeek = ContextHolder.currentWeek;
        Map<Integer, CourseVO> currentTermCourse = ContextHolder.data.get(currentTerm);
        todayCourseList = new ArrayList<>();
        if (currentTermCourse != null) {
            CourseVO courseVO = currentTermCourse.get(currentWeek);
            if (courseVO != null) {
                for (Course course : courseVO.getCourseList()) {
                    if (course.getDayOfWeek().equals(String.valueOf(ContextHolder.dayOfWeek))) {
                        todayCourseList.add(course);
                    }
                }
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_course_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i(MyState.TAG, "CourseTodayListAdapter onBindViewHolder");
        Course course = todayCourseList.get(position);
        holder.courseName.setText(course.getName());
        holder.grade.setText(course.getGrade());
        holder.teacher.setText(course.getTeacher());
        holder.location.setText(course.getLocation());
        holder.week.setText(course.getWeek() + "周");
        holder.section.setText(CourseGridPanelAdapter.weekList[ContextHolder.dayOfWeek] + " " + course.getSection() + "节");
    }

    @Override
    public int getItemCount() {
        return todayCourseList.size();
    }
}