package com.project.wei.zhbj.utils;

import android.content.Context;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class CacheUtil {
//  把获取到的json ，以url为key,以json为value存入SharedPreferences中
//  也可以用文件缓存: 以MD5(url)为文件名, 以json为文件内容
    public static void setCache(String url, String json, Context context) {
        SharedPrefUtil.setString(context,url,json);
    }
//  从SharedPreferences中读取缓存
//  文件缓存: 查找有没有一个文件叫做MD5(url)的, 有的话,说明有缓存
    public static String getCache(String url, Context context) {
        return SharedPrefUtil.getString(context,url,null);
    }
}
