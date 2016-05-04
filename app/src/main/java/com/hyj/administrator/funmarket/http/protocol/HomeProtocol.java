package com.hyj.administrator.funmarket.http.protocol;

import com.hyj.administrator.funmarket.domain.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 首页网络数据解析
 */
public class HomeProtocol extends BaseProtocol<ArrayList<AppInfo>> {
    @Override
    protected String getKey() {
        return "home";//服务器的关键词
    }

    @Override
    protected String getParams() {
        return "";// 如果没有参数,就传空串,不要传null
    }

    @Override
    public ArrayList<AppInfo> parseData(String result) {
        // 也可使用Gson
        // 这里使用JsonObject解析方式: 如果遇到{},就是JsonObject;如果遇到[], 就是JsonArray
        try {
            JSONObject joResult = new JSONObject(result);

            // 解析应用列表数据
            JSONArray jaList = joResult.getJSONArray("list");
            ArrayList<AppInfo> appInfoList = new ArrayList<>();
            for (int i = 0; i < jaList.length(); i++) {
                JSONObject jo = jaList.getJSONObject(i);

                AppInfo appInfo = new AppInfo();
                appInfo.des = jo.getString("des");
                appInfo.downloadUrl = jo.getString("downloadUrl");
                appInfo.iconUrl = jo.getString("iconUrl");
                appInfo.id = jo.getString("id");
                appInfo.name = jo.getString("name");
                appInfo.packageName = jo.getString("packageName");
                appInfo.size = jo.getLong("size");
                appInfo.stars = (float) jo.getDouble("stars");

                appInfoList.add(appInfo);

            }

            // 初始化轮播条的数据
            JSONArray jaPic = joResult.getJSONArray("picture");

            pictures = new ArrayList<String>();
            for (int i = 0; i < jaPic.length(); i++) {
                String pic = jaPic.getString(i);
                pictures.add(pic);
            }

            return appInfoList;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String> pictures;

    public ArrayList<String> getPictureList() {
        return pictures;
    }
}
