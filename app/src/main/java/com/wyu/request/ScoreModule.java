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
                    Log.i("scoreVO ", String.valueOf(scoreVO.getScoreCount()));

//                    String path = "scoresList_" + account + "_" + term + ".json";
//                    ScoresList scoresList = storeScoresList(html, path);
//                    if (scoresList != null) {
//                        Message msg = new Message();
//                        Bundle bundle = new Bundle();
//                        DataConf dataConf = new DataConf(RequestInfo.account, term, path);
//                        bundle.putString("kind", "scoresList");
//                        bundle.putSerializable("dataConf", dataConf);
//                        bundle.putSerializable("data", scoresList);
//                        msg.what = MyState.SUCCESS;
//                        msg.setData(bundle);
//                        handler.sendMessage(msg);
//                    }
                }
            }
        }.get(url, RequestInfo.cookies);
    }
}
