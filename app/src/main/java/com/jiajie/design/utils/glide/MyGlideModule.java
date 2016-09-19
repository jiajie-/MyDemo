package com.jiajie.design.utils.glide;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

/**
 * MyGlideModule
 * Created by jiajie on 16/9/19.
 */
public class MyGlideModule implements GlideModule {

    private static final String TAG = "MyGlideModule";

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        /**
         * .setMemoryCache(MemoryCache memoryCache)
         * .setBitmapPool(BitmapPool bitmapPool)
         * .setDiskCache(DiskCache.Factory diskCacheFactory)
         * .setDiskCacheService(ExecutorService service)
         * .setResizeService(ExecutorService service)
         * .setDecodeFormat(DecodeFormat decodeFormat)
         */

        //增加 Glide 的图片质量
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

        /**
         * 缓存管理
         */
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        Log.e(TAG, "applyOptions: customMemoryCacheSize: " + customMemoryCacheSize);
        Log.e(TAG, "applyOptions: customBitmapPoolSize: " + customBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

        //自定义磁盘缓存（可以设置 内部存储 和 外部存储 或 特定目录）
        String downloadDirectoryPath = Environment.getDownloadCacheDirectory().getPath();
        Log.e(TAG, "applyOptions: downloadDirectoryPath: " + downloadDirectoryPath);

        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, customMemoryCacheSize));
//        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, customMemoryCacheSize));
//        builder.setDiskCache(new DiskLruCacheFactory(downloadDirectoryPath, customMemoryCacheSize));


    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
