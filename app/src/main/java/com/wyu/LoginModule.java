package com.wyu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.wyu.config.Form;
import com.wyu.config.MyState;
import com.wyu.util.BizCodeEnum;
import com.wyu.util.Resp;
import com.wyu.util.CommonUtil;
import com.wyu.util.MyOkHttp2;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginModule extends Form {
    private Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }

    public LoginModule(Handler handler) {
        this.handler = handler;
    }

    public void getVerifyCodeImage() {
        new MyOkHttp2(handler) {
            @Override
            protected void responsedSolve(Call call, Response response) throws IOException {
                cookies = new StringBuilder();
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    try {
                        JSONObject obj = new JSONObject(json);
                        String code = obj.getString("code");
                        String data = obj.getString("data");
                        JSONObject dataJson = new JSONObject(data);
                        String img = dataJson.getString("img");


                        byte[] Picture_bt = Base64.decode(img, Base64.DEFAULT);
                        Log.i(MyState.TAG, "验证码大小:" + Picture_bt.length + "B");
                        Bitmap bitmap = BitmapFactory.decodeByteArray(Picture_bt, 0, Picture_bt.length);
                        Headers headers = response.headers();
                        List<String> sessions = headers.values("Set-Cookie");
                        if (sessions.size() == 0) {
                            Message msg = new Message();
                            msg.what = MyState.FAILED;
                            msg.obj = "获取cookie失败！";
                            handler.sendMessage(msg);
                        } else {
                            for (String session : sessions) {
                                cookies.append(session.substring(0, session.indexOf(";"))).append(";");
                            }
                            Log.i(MyState.TAG, cookies.toString());
                            Message msg = new Message();
                            msg.what = MyState.GET_VERIFYCODE_SUCCESSFUL;
                            msg.obj = bitmap;
                            handler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            //}.myConnect(verifyImgSrcURL);
        }.get("http://116.62.108.144:8081/user/captcha");
    }

    public void studentSubmit(final String userNumber, final String password, final String verifyCode) {
        Map<String, String> map = new HashMap<>();

        map.put("account", userNumber);
        map.put("password", password);
        map.put("captcha", verifyCode);
        map.put("captchaId", verifyCode);

        JSONObject jsonObject = new JSONObject(map);

        final Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", cookies.toString());
        header.put("User-Agent", userAgent);
        new MyOkHttp2(handler) {
            @Override
            protected void responsedSolve(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Resp resp = CommonUtil.getResp(response);
                    Log.i("resp", resp.toString());
                    if (BizCodeEnum.SUCCESS.getCode().equals(resp.getCode())) {
                        Form.captcha = verifyCode;
                        Form.account = userNumber;
                        Form.password = password;
                        Log.i(MyState.TAG, "登陆成功~~~");
                        Message msg = new Message();
                        msg.what = MyState.LOGIN_SUCCESSFUL;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = MyState.LOGIN_FAILED;
                        handler.sendMessage(msg);
                    }
                }
            }
        }.get(URL_LOGIN, header, jsonObject);
    }

    public void parentGetGrades(String stuName, String stuIdNum, String verifyCode) {
        String extUrl;
        try {
            extUrl = "xsxm=" + URLEncoder.encode(stuName, "utf-8") + "&xssfzh=" + stuIdNum + "&yzm=" + verifyCode;
            Log.i(MyState.TAG, extUrl);
            Map<String, String> header = new HashMap<String, String>();
            header.put("cookie", cookies.toString());
            header.put("User-Agent", userAgent);
            MediaType type = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody rBody = RequestBody.create(type, extUrl);
            new MyOkHttp2(handler) {
                @Override
                protected void responsedSolve(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String html = response.body().string().trim();
                        Log.i("mtest", html.length() + "");
                        String url = parentGradesURL + "?yzbh=" + html;
                        new MyOkHttp2(handler).get(url, cookies);
                    }
                }
            }.get(loginParent, header, rBody);
        } catch (Exception e) {
            e.printStackTrace();
            Message msg = new Message();
            msg.what = MyState.STRING_ERROR;
            msg.obj = "生成成绩链接出错！";
            handler.sendMessage(msg);
        }
    }
}
