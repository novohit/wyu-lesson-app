package com.wyu.constant;

public class RequestInfo {
    protected static String account;
    protected static String password;
    protected static String captcha;
    //protected final static String DOMAIN = "http://116.62.108.144:8081";
    protected final static String DOMAIN = "http://10.10.26.111:8082";
    protected final static String URL_LOGIN = DOMAIN + "/user/login";
    protected final static String URL_CAPTCHA = DOMAIN + "/user/captcha";
    protected final static String URL_COURSE = DOMAIN + "/user/courses";
    protected final static String URL_SCORE = DOMAIN + "/user/score";
    protected final static String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko";
    protected static StringBuilder cookies = new StringBuilder();
    protected volatile static  int  state = 0;

    public static String getCookies() {
        return cookies.toString();
    }

    public static String getAccount() {
        return account;
    }
}
