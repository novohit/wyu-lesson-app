package com.wyu.util;

import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ToastUtil {
    public static Toast toast;
    /**
     * 传入文字
     * */
    public static void show(String text){
        if (toast == null){
            toast = Toast.makeText( MyApplication.getContext(), text , Toast.LENGTH_SHORT);
        }else {
            //如果当前Toast没有消失， 直接显示内容，不需要重新设置
            toast.setText(text);
        }
        toast.show();
    }
    /**
     * 传入资源文件
     * */
    public static void show(int resId){
        if (toast == null){
            toast = Toast.makeText( MyApplication.getContext(), resId , Toast.LENGTH_SHORT);
        }else {
            //如果当前Toast没有消失， 直接显示内容，不需要重新设置
            toast.setText(resId);
        }
        toast.show();
    }
    /**
     * 传入文字,在中间显示
     * */
    public static void showCenter( String text){

        if (toast == null){
            toast = Toast.makeText( MyApplication.getContext(), text , Toast.LENGTH_SHORT);
        }else {
            //如果当前Toast没有消失， 直接显示内容，不需要重新设置
            toast.setText(text);
        }
        toast.setGravity(Gravity.CENTER , 0 , 0);
        toast.show();
    }
    /**
     * 传入文字，带图片
     * */
    public static void showImg( String text , int resImg){

        if (toast == null){
            toast = Toast.makeText( MyApplication.getContext(), text , Toast.LENGTH_SHORT);
        }else {
            //如果当前Toast没有消失， 直接显示内容，不需要重新设置
            toast.setText(text);
        }
        //添加图片的操作,这里没有设置图片和文字显示在一行的操作呢...
        LinearLayout view = (LinearLayout) toast.getView();
        ImageView imageView = new ImageView(MyApplication.getContext());
        imageView.setImageResource(resImg);
        view.addView(imageView);

        toast.show();
    }
}
