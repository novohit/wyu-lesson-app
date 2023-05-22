package com.wyu.data;

import java.util.ArrayList;
import java.util.List;

public class CourseTableDivide {


    public static  List<CourseCard> courseTableDivide(List<Course> courses){
        List<CourseCard> courseCards = new ArrayList<>();
        int cnt = 0;
        for(Course course:courses){
            CourseCard courseCard = new CourseCard();
            courseCard.kecheng = course.kecheng;
            courseCard.didian = course.didian;
            courseCard.jiaoshi = course.jiaoshi;
            courseCard.banji = course.banji;
            courseCard.xingqi = Integer.valueOf(course.shijian.substring(0,1));
            courseCard.jiestart = Integer.valueOf(course.shijian.substring(1,3));
            courseCard.jieend = Integer.valueOf(course.shijian.substring(course.shijian.length()-2));
            courseCard.no = cnt++;
            courseCard.zhouci = course.zhouci;
            courseCard.zhou = new boolean[25];
            solveZhou(course.zhouci,courseCard.zhou);
            courseCard.jie = new boolean[15];
            solveJie(course.shijian.substring(1),courseCard.jie);
            courseCards.add(courseCard);
        }
        return courseCards;
    }
    private static void solveZhou(String zhouStr,boolean[] zhou){
        String groupStr[] = zhouStr.split(",");
        for(String sstr:groupStr){
            String[] lstr = sstr.split("-");
            if(lstr.length==1) zhou[Integer.valueOf(lstr[0])]=true;
            else if (lstr.length==2) {
                int st = Integer.valueOf(lstr[0]);
                int ed = Integer.valueOf(lstr[1]);
                for(int i = st; i <= ed; ++i)
                    zhou[i]=true;
            }
        }
    }
    private static void solveJie(String jieStr,boolean[] jie){
        int size = jieStr.length();
        for(int i = 0; i < size; i += 2){
            String str = jieStr.substring(i,i+2);
            jie[Integer.valueOf(str)]=true;
        }
    }


}
