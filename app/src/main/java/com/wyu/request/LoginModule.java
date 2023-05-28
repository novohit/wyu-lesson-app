package com.wyu.request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import com.wyu.constant.MyState;
import com.wyu.constant.RequestInfo;
import com.wyu.constant.BizCodeEnum;
import com.wyu.util.CommonUtil;
import com.wyu.util.CustomHttp;
import com.wyu.util.FileUtil;
import com.wyu.util.Resp;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginModule extends RequestInfo {
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

    public void getCaptcha() {
        new CustomHttp(handler) {
            @Override
            protected void success(Call call, Response response) {
                cookies = new StringBuilder();
                if (response.isSuccessful()) {
                    Resp resp = CommonUtil.getResp(response);
                    try {
                        String data = resp.getData();
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
                            msg.what = MyState.GET_CAPTCHA_SUCCESS;
                            msg.obj = bitmap;
                            handler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }.get("http://116.62.108.144:8081/user/captcha");
    }

    public void studentSubmit(final String userNumber, final String password, final String verifyCode) {
        FileUtil.clearInfo();
        Map<String, String> map = new HashMap<>();

        map.put("account", userNumber);
        map.put("password", password);
        map.put("captcha", verifyCode);
        map.put("captchaId", verifyCode);

        JSONObject jsonObject = new JSONObject(map);

        final Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", cookies.toString());
        header.put("User-Agent", userAgent);
        new CustomHttp(handler) {
            @Override
            protected void success(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Resp resp = CommonUtil.getResp(response);
                    Log.i("resp", resp.toString());
                    if (BizCodeEnum.SUCCESS.getCode().equals(resp.getCode())) {
                        RequestInfo.captcha = verifyCode;
                        RequestInfo.account = userNumber;
                        RequestInfo.password = password;
                        Log.i(MyState.TAG, "登陆成功");
                        Message msg = new Message();
                        msg.what = MyState.LOGIN_SUCCESS;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = MyState.LOGIN_FAILED;
                        handler.sendMessage(msg);
                    }
                }
            }
        }.post(URL_LOGIN, header, jsonObject);
    }
}
