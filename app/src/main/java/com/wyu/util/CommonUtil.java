package com.wyu.util;

import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author novo
 * @since 2023-05-20
 */
public class CommonUtil {

    public static Resp getResp(Response response) {
        try {
            assert response.body() != null;
            String json = response.body().string();
            JSONObject jsonObject = new JSONObject(json);
            Resp resp = new Resp();
            resp.setCode(jsonObject.getString("code"));
            String msg = jsonObject.getString("msg");
            if (msg != null) {
                resp.setMsg(msg);
            }
            String data = jsonObject.getString("data");
            if (data != null) {
                resp.setData(data);
            }
            return resp;

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCurrentTerm() {
        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH) + 1;
        int year = now.get(Calendar.YEAR);
        if (month < 8) {
            return year - 1 + "02";
        } else {
            return year + "01";
        }
    }

    public static int getCurrentWeek(String startStr) {
        int week = 1;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date start = dateFormat.parse(startStr);

            // 获得当前日期与本周日相差的天数
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            long daysBetween = (Calendar.getInstance().getTime().getTime() - start.getTime() + 1000000) / (60 * 60 * 24 * 1000);
            week = (int) (daysBetween / 7 + 1);
            if (dayOfWeek + daysBetween % 7 > 7) {
                week += 1;
            }
        } catch (ParseException e) {

        }
        return week;
    }
}
