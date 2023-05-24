package com.wyu.model;


import java.util.List;
import java.util.Map;

/**
 * @author zwx
 * @date 2022-11-23 14:48
 */
public class CourseVO {

    private List<Course> courseList;

    private Map<String, String> date;

    private Integer courseCount;

    public CourseVO() {

    }

    public CourseVO(List<Course> courseList, Map<String, String> date) {
        this.date = date;
        this.courseList = courseList;
        int sum = 0;
        for (Course course : courseList) {
            int count = course.getCount();
            sum += count;
        }
        this.courseCount = sum;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public Integer getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(Integer courseCount) {
        this.courseCount = courseCount;
    }

    public Map<String, String> getDate() {
        return date;
    }

    public void setDate(Map<String, String> date) {
        this.date = date;
    }
}
