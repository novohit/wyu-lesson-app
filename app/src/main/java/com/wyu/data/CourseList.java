package com.wyu.data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class CourseList implements Serializable {
    private String info;
    private String xh;
    private List<Course> table;
    private String code;

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setTable(List<Course> table) {
        this.table = new ArrayList<Course>();
        for(Course cur:table){
            this.table.add((Course)cur.clone());
        }
    }

    public List<Course> getTable() {
        List<Course> table = new ArrayList<Course>();
        for(Course cur:this.table){
            table.add((Course)cur.clone());
        }
        return table;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "CourseTable[info="+info
                +",xh="+xh
                +",table="+table
                +",code="+code
                +"]";
    }

}
