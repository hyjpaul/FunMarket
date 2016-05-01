package com.hyj.administrator.funmarket.http.protocol;

import com.hyj.administrator.funmarket.http.HttpHelper;
import com.hyj.administrator.funmarket.uiutils.IOUtils;
import com.hyj.administrator.funmarket.uiutils.StringUtils;
import com.hyj.administrator.funmarket.uiutils.UiUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 访问网络的基类
 */
public abstract class BaseProtocol<T> {

    // index表示的是从哪个位置开始返回20条数据, 用于分页,这跟所用的服务器有关，还有一种是服务器这一页的数据包含下一页的数据链接
    public T getData(int index) {
        // 先判断是否有缓存, 有的话就加载缓存
        String result = getCache(index);

        if (StringUtils.isEmpty(result)) {// 如果没有缓存,或者缓存失效
            // 请求服务器
            result = getDataFromServer(index);
        }

        // 开始解析
        if (result != null) {
            T data = parseData(result);
            return data;
        }

        return null;

    }

    // 从网络获取数据   用别人写好的专门获取MyWebServer服务器数据的HttpHelper
    private String getDataFromServer(int index) {
// http://www.itheima.com/home?index=0&name=zhangsan&age=18    // index表示的是从哪个位置开始返回20条数据, 用于分页,服务器链接不变根据index的不用返回不用位置开始的数据

        HttpHelper.HttpResult httpResult = HttpHelper.get(HttpHelper.URL + getKey()
                + "?index=" + index + getParams());

        if (httpResult != null) {
            String result = httpResult.getString();
            System.out.println("访问结果:" + result);
            // 写缓存
            if (!StringUtils.isEmpty(result)) {
                setCache(index, result);
            }
            return result;

        }
        return null;
    }

    // 获取网络链接关键词, 子类必须实现

    protected abstract String getKey();

    // 获取网络链接参数, 子类必须实现
    protected abstract String getParams();

    // 写缓存
    // 以url为key, 以json为value
    public void setCache(int index, String json) {
        // 以url为文件名, 以json为文件内容,保存在本地
        File cacheDir = UiUtil.getContext().getCacheDir();// 本应用的缓存文件夹
        // 生成缓存文件
        File cacheFile = new File(cacheDir, getKey() + "?index=" + index
                + getParams());

        FileWriter writer = null;
        try {
            writer = new FileWriter(cacheFile);
            // 缓存失效的截止时间
            long deadLine = System.currentTimeMillis() + 30 * 60 * 1000;// 半个小时有效期
            writer.write(deadLine + "\n");// 在第一行写入缓存时间, 换行
            writer.write(json);// 写入json
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(writer);
        }

    }

    // 读缓存
    public String getCache(int index) {
        // 以url为文件名, 以json为文件内容,保存在本地
        File cacheDir = UiUtil.getContext().getCacheDir();// 本应用的缓存文件夹
        // 生成缓存文件
        File cacheFile = new File(cacheDir, getKey() + "?index=" + index
                + getParams());

        // 判断缓存是否存在
        if (cacheFile.exists()) {
            // 判断缓存是否有效
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(cacheFile));
                String deadLine = reader.readLine();
                long deadTime = Long.parseLong(deadLine);

                if (System.currentTimeMillis() < deadTime) {
                    // 当前时间小于截止时间,说明缓存有效
                    StringBuffer sb = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    return sb.toString();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(reader);
            }
        }

        return null;
    }

    // 解析json数据, 子类必须实现
    public abstract T parseData(String result);
}
