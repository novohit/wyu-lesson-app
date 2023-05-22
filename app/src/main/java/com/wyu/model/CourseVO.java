package com.wyu.model;


import java.util.List;

/**
 * @author zwx
 * @date 2022-11-23 14:48
 */
public class CourseVO {

    private List<Course> courseList;


    private Integer courseCount;

    public CourseVO() {

    }

    public CourseVO(List<Course> courseList) {

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
}
