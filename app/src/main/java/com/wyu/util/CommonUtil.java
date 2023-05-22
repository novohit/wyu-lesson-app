package com.wyu.util;

import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
}
