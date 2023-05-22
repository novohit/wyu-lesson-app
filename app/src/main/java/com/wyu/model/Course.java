package com.wyu.model;



/**
 * @author zwx
 * @date 2022-11-23 13:27
 */
public class Course {
    private String id;

    private String name;

    private String teacher;

    private String type;

    private String dayOfWeek;

    private String date;

    private String section;

    private String week;

    private String location;

    private String grade;

    private String stuCount;

    private String content;

    private Integer count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStuCount() {
        return stuCount;
    }

    public void setStuCount(String stuCount) {
        this.stuCount = stuCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Course() {
    }

    public Course(String id, String name, String teacher, String type, String dayOfWeek, String date, String section, String week, String location, String grade, String stuCount, String content) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.type = type;
        this.dayOfWeek = dayOfWeek;
        this.date = date;
        this.section = section;
        this.week = week;
        this.location = location;
        this.grade = grade;
        this.stuCount = stuCount;
        this.content = content;
    }
}
