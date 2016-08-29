package com.project.wei.zhbj.utils;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.project.wei.zhbj.R;

/**
 * Created by wei on 2016/8/29 0029.
 */
/*三级缓存

- 优先从内存中加载图片, 速度最快, 不浪费流量
- 其次从本地(sdcard)加载图片, 速度快, 不浪费流量
- 最后从网络下载图片, 速度慢, 浪费流量

> 内存溢出
	不管android设备总内存是多大, 都只给每个app分配一定内存大小, 16M, 一旦超出16M就内存溢出了*/
public class MyBitmapUtils {

    private final NetCacheUtils netCacheUtils;
    private final LocalCacheUtils localCacheUtils;
    private final MemoryCacheUtils memoryCacheUtils;

    public MyBitmapUtils() {
        localCacheUtils = new LocalCacheUtils();
        memoryCacheUtils = new MemoryCacheUtils();
        netCacheUtils = new NetCacheUtils(localCacheUtils,memoryCacheUtils);
    }

    public void display(ImageView imageView, String url) {
        // 设置默认图片
        imageView.setImageResource(R.drawable.pic_item_list_default);

        // 优先从内存中加载图片, 速度最快, 不浪费流量
        Bitmap bitmap = memoryCacheUtils.getMemoryCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Log.i("TAG","从内存加载图片啦");
            return;
        }

        // 其次从本地(sdcard)加载图片, 速度快, 不浪费流量
        bitmap = localCacheUtils.getLocalCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Log.i("TAG","从本地加载图片啦");

            // 写内存缓存
            memoryCacheUtils.setMemoryCache(url, bitmap);
            return;
        }

        // 最后从网络下载图片，速度慢，浪费流量
        netCacheUtils.getBitmapFromNet(imageView,url);

    }
}
