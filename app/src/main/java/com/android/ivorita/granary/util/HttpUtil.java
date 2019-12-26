package com.android.ivorita.granary.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 网络工具类
 */
public class HttpUtil {

    /**
     * OKHttp是一个处理网络请求的开源项目，Android当前最火热网络框架，
     * 用于替代HttpUrlConnection和Apache HttpClient(android API23 6.0里已移除HttpClient）。
     */
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        //构建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建Request对象，设置一个url地址，设置为GET请求方式
        Request request = new Request.Builder()
                .url(address)
                .get()
                .build();
        //创建一个call对象,参数就是Request请求对象,最后以异步的方式去执行请求
        okHttpClient.newCall(request).enqueue(callback);
    }

}