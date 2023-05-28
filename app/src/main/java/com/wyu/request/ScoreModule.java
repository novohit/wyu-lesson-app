package com.wyu.request;

import android.os.Handler;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyu.config.ContextHolder;
import com.wyu.constant.RequestInfo;
import com.wyu.model.ScoreVO;
import com.wyu.util.CommonUtil;
import com.wyu.util.CustomHttp;
import com.wyu.util.Resp;
import okhttp3.Call;
import okhttp3.Response;


public class ScoreModule extends RequestInfo {
    private Handler handler;

    public ScoreModule(Handler handler) {
        this.handler = handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }

    public void getScoresList(String term) {
        String url = URL_SCORE + String.format("?term=%s", term);
        new CustomHttp(handler) {
            @Override
            protected void success(Call call, Response response) {
                if (response.isSuccessful()) {
                    Resp resp = CommonUtil.getResp(response);
                    ScoreVO scoreVO = new Gson().fromJson(resp.getData(), new TypeToken<ScoreVO>() {
                    }.getType());
                    ContextHolder.scoreData.put(term, scoreVO);
                }
            }
        }.get(url, RequestInfo.cookies);
    }
}
