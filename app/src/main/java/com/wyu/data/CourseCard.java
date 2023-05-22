package com.wyu.data;

public class CourseCard{
    public String kecheng;
    public String didian;
    public String jiaoshi;
    public String banji;
    public String zhouci;
    public int jiestart;
    public int jieend;
    public int xingqi;
    public boolean[] jie;
    public boolean[] zhou;
    public int no;
    @Override
    public String toString() {
        return kecheng+"\n@"+didian;
    }

    public CourseCard() {
    }

    public CourseCard(String kecheng, String didian, String jiaoshi, String banji, String zhouci, int jiestart, int jieend, int xingqi, boolean[] jie, boolean[] zhou, int no) {
        this.kecheng = kecheng;
        this.didian = didian;
        this.jiaoshi = jiaoshi;
        this.banji = banji;
        this.zhouci = zhouci;
        this.jiestart = jiestart;
        this.jieend = jieend;
        this.xingqi = xingqi;
        this.jie = jie;
        this.zhou = zhou;
        this.no = no;
    }
}