package com.wyu;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import com.google.gson.reflect.TypeToken;
import com.wyu.config.ContextHolder;
import com.wyu.data.DataConf;
import com.wyu.data.ScoresList;
import com.wyu.data.Subject;
import com.wyu.config.RequestInfo;
import com.wyu.config.MyState;
import com.wyu.model.CourseVO;
import com.wyu.model.ScoreVO;
import com.wyu.util.CommonUtil;
import com.wyu.util.MyFileHelper;
import com.wyu.util.MyOkHttp2;
import com.wyu.util.Resp;
import okhttp3.Call;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ScoreModule extends RequestInfo {
    private Handler handler;

    ScoreModule(Handler handler) {
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
        new MyOkHttp2(handler) {
            @Override
            protected void responsedSolve(Call call, Response response) {
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

    public ScoresList storeScoresList(String html, String path) {
        Document doc = Jsoup.parse(html);
        Elements isRight = doc.select("div#mxhDiv");
        if (isRight.size() == 0) {
            Message msg = new Message();
            msg.what = MyState.INCORRECT_HTML;
            msg.obj = "获取成绩失败！";
            handler.sendMessage(msg);
            return null;
        }
        Elements table = isRight.first().select("tr");
        if (table.size() == 0) {
            Message msg = new Message();
            msg.what = MyState.JSON_ERROR;
            msg.obj = "生成成绩失败！";
            handler.sendMessage(msg);
            return null;
        }
        ScoresList scoresList = new ScoresList();
        scoresList.setInfo("ok");
        scoresList.setXh(table.select("td").get(2).text());
        scoresList.setXm(table.select("td").get(3).text());
        scoresList.setTerm(table.select("td").get(4).text());
        List<Subject> subjects = new ArrayList<Subject>();
        for (Element sub : table) {
            Subject subject = new Subject();
            Elements part = sub.select("td");
            if (part.size() < 14) {
                Message msg = new Message();
                msg.what = MyState.JSON_ERROR;
                msg.obj = "生成成绩失败！";
                handler.sendMessage(msg);
                return null;
            }
            subject.setMingcheng(part.get(5).text());
            subject.setChengji(part.get(6).text());
            subject.setBiaozhi(part.get(7).text());
            subject.setKechengxz(part.get(8).text());
            subject.setLeibie(part.get(9).text());
            subject.setXueshi(part.get(10).text());
            subject.setXuefen(part.get(11).text());
            subject.setKaoshixz(part.get(12).text());
            subject.setBuchongxq(part.get(13).text());
            subjects.add(subject);
        }
        scoresList.setSubjects(subjects);
        if (subjects.size() > 0)
            scoresList.setCode("ok");
        else
            scoresList.setCode("-1");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();  //setPrettyPrinting()格式化json
        String json = gson.toJson(scoresList);
        if (MyFileHelper.JsonToFile(json, path) != 0) {
            Message msg = new Message();
            msg.what = MyState.FILE_ERROR;
            msg.obj = "保存成绩失败！";
            handler.sendMessage(msg);
            return null;
        }
        return scoresList;
    }
}
