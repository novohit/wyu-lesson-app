package com.wyu.model;


/**
 * @author zwx
 * @date 2022-11-24 15:09
 */
public class Score {
    private String username;

    private String studentNum;

    private String term;

    private String category;

    private String subCategory;

    private String courseName;

    private String studyType;

    private String score;

    private String credit;

    public Score() {
    }

    public Score(String username, String studentNum, String term, String category, String subCategory, String courseName, String studyType, String score, String credit) {
        this.username = username;
        this.studentNum = studentNum;
        this.term = term;
        this.category = category;
        this.subCategory = subCategory;
        this.courseName = courseName;
        this.studyType = studyType;
        this.score = score;
        this.credit = credit;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStudyType() {
        return studyType;
    }

    public void setStudyType(String studyType) {
        this.studyType = studyType;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}
