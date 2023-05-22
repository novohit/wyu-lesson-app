package com.wyu.config;


public class Form {
    protected static final  String personType = "0";
    protected static String account;
    protected static String password;
    protected static String captcha;
    protected final static String DOMAIN = "http://116.62.108.144:8081"; //教务网域名
    protected final static String URL_LOGIN = DOMAIN + "/user/login";
    protected final static String loginParent = DOMAIN +"/kdjw/xscjcx_check.jsp";
    protected final static String parentGradesURL = DOMAIN + "/kdjw/xscjcx.jsp";
    protected final static String verifyImgSrcURL = DOMAIN + "/kdjw/verifycode.servlet";

    protected final static String personTableBaseURL = DOMAIN + "/kdjw/tkglAction.do?method=goListKbByXs";
    protected final static String URL_COURSE = DOMAIN + "/user/courses";
    protected final static String scoresListBaseURL = DOMAIN + "/kdjw/xszqcjglAction.do?method=queryxscj";
    protected final static String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko";

    protected static StringBuilder cookies = new StringBuilder();       //save cookies
    protected volatile static  int  state = 0;


    public static String getCookies() {
        return cookies.toString();
    }

    public static String getAccount() {
        return account;
    }
}
