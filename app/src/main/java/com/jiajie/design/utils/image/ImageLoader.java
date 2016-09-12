package com.jiajie.design.utils.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by jiajie on 16/9/12.
 */
public class ImageLoader {

    private static ImageLoader mInstance;

    /**
     * 图片缓存的核心对象
     */
    private LruCache<String, Bitmap> mLruCache;

    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    private static final int DEFAULT_THREAD_COUNT = 1;

    /**
     * 队列的调度方式
     */
    private Type mType = Type.LIFO;

    public enum Type {
        FIFO, LIFO
    }

    /**
     * 任务队列
     */
    private LinkedList<Runnable> mTaskQueue;

    /**
     * 后台轮询线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;

    /**
     * UI线程中的Handler
     */
    private Handler mUIHandler;

    //为了不让 mPoolThreadHandler 为空
    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
    //为了真正做到FIFO,LIFO
    private Semaphore mSemaphoreThreadPool;


    private ImageLoader(int threadCount, Type type) {
        init(threadCount, type);
    }

    private void init(int threadCount, Type type) {
        //后台轮询线程
        mPoolThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        //线程池去取出一个任务执行
                        mThreadPool.execute(getTask());

                        //为了让线程队列起作用，当线程池>threadCount时阻塞，当任务执行完后再release
                        try {
                            mSemaphoreThreadPool.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                };
                //释放一个信号量
                //（因为要等 mPoolThreadHandler 初始化完毕后才能让addTask执行，
                // 所以等mPoolThreadHandler初始化完毕后才释放信号量，就避免了空指针）
                mSemaphorePoolThreadHandler.release();
                Looper.loop();
            }
        };
        mPoolThread.start();

        //LruCache
        //获取应用最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) { //测量每个bitmap的大小
                return value.getRowBytes() * value.getHeight();
            }
        };

        //创建线程池
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<>();
        mType = type;

        mSemaphoreThreadPool = new Semaphore(threadCount);

    }

    public static ImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(DEFAULT_THREAD_COUNT, Type.FIFO);
                }
            }
        }
        return mInstance;
    }

    public static ImageLoader getInstance(int threadCount, Type type) {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }

    /**
     * 从任务队列取出一个方法
     *
     * @return
     */
    public Runnable getTask() {
        if (mType == Type.FIFO) {
            return mTaskQueue.removeFirst();
        } else if (mType == Type.LIFO) {
            return mTaskQueue.removeLast();
        }
        return null;
    }

    /**
     * 根据path为imageView设置图片
     *
     * @param path
     * @param imageView
     */
    public void loadImage(final String path, final ImageView imageView) {
        imageView.setTag(path);
        if (mUIHandler == null) {
            mUIHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    //获取得到图片，为imageView回调设置图片
                    ImageBeanHolder holder = (ImageBeanHolder) msg.obj;
                    Bitmap bm = holder.bitmap;
                    ImageView imageView = holder.imageView;
                    String path = holder.path;

                    //将path与getTag存储路径进行比较
                    if (imageView.getTag().toString().equals(path)) {
                        imageView.setImageBitmap(bm);
                    }

                }
            };
        }

        Bitmap bm = getBitmapFromLruCache(path);
        if (bm != null) {
            refreshBitmap(bm, path, imageView);
        } else {
            addTasks(new Runnable() {
                @Override
                public void run() {
                    //加载图片
                    //图片压缩
                    //1.获得图片需要显示的大小
                    ImageSize imageSize = getImageViewSize(imageView);
                    //2.压缩图片
                    Bitmap bm = decodeSampledBitmapFromPath(path, imageSize.width, imageSize.height);
                    //3.把图片加入缓存
                    addBitmapToLruCache(path, bm);
                    refreshBitmap(bm, path, imageView);

                    mSemaphoreThreadPool.release();
                }
            });
        }

    }

    private void refreshBitmap(Bitmap bm, String path, ImageView imageView) {
        Message message = Message.obtain();
        ImageBeanHolder holder = new ImageBeanHolder();
        holder.bitmap = bm;
        holder.path = path;
        holder.imageView = imageView;
        message.obj = holder;
        mUIHandler.sendMessage(message);
    }

    protected void addBitmapToLruCache(String path, Bitmap bm) {
        if (getBitmapFromLruCache(path) == null) {
            if (bm != null) {
                mLruCache.put(path, bm);
            }
        }
    }

    protected Bitmap decodeSampledBitmapFromPath(String path, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, width, height);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            int widthRadio = Math.round(width * 1.0f / reqWidth);
            int heightRadio = Math.round(height * 1.0f / reqHeight);
            inSampleSize = Math.max(widthRadio, heightRadio);
        }
        return inSampleSize;
    }


    /**
     * 根据ImageView获取适当的压缩的宽和高
     *
     * @param imageView
     *
     * @return
     */
    protected ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();
        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();

        //width
        int width = imageView.getWidth();//获取实际宽度
        if (width <= 0) {//wrap_content 或者 match_parent
            width = lp.width;//获取在layout中声明的宽度
        }
        if (width <= 0) {
            width = imageView.getMaxWidth();//检查最大值
        }
        if (width <= 0) {
            width = displayMetrics.widthPixels;
        }
        //height
        int height = imageView.getHeight();//获取实际高度
        if (height <= 0) {//wrap_content 或者 match_parent
            height = lp.height;//获取在layout中声明的高度
        }
        if (height <= 0) {
            height = imageView.getMaxHeight();//检查最大值
        }
        if (height <= 0) {
            height = displayMetrics.heightPixels;
        }

        imageSize.width = width;
        imageSize.height = height;

        return imageSize;
    }

    /**
     * synchronized 和 mSemaphorePoolThreadHandler 解决了并发的问题
     *
     * @param runnable
     */
    private synchronized void addTasks(Runnable runnable) {
        mTaskQueue.add(runnable);
        //当mSemaphorePoolThreadHandler默认是0的时候acquire肯定会被阻塞
        try {
            if (mPoolThreadHandler == null) {
                mSemaphorePoolThreadHandler.acquire();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mPoolThreadHandler.sendEmptyMessage(0x110);
    }

    private Bitmap getBitmapFromLruCache(String path) {
        return mLruCache.get(path);
    }

    private class ImageSize {
        int width;
        int height;
    }

    private class ImageBeanHolder {
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }

}
