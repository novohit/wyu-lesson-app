package com.wyu.data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class ScoresList implements Serializable {
    private String info;
    private String xh;
    private String xm;
    private String xq;
    private List<Subject> subjects;
    private String code;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXq() {
        return xq;
    }

    public void setTerm(String xq) {
        this.xq = xq;
    }

    public List<Subject> getSubjects() {
        List<Subject> subjects = new ArrayList<Subject>();
        for(Subject sub:this.subjects){
            subjects.add((Subject)sub.clone());
        }
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = new ArrayList<Subject>();
        for(Subject sub:subjects){
            this.subjects.add((Subject) sub.clone());
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ScoreList[info="+info
                +",xh="+xh
                +",xm="+xm
                +",xq="+xq
                +",subjects="+subjects
                +",code="+code
                +"]";
    }
}
