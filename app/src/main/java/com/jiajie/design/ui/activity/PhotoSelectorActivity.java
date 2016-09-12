package com.jiajie.design.ui.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiajie.design.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * PhotoSelectorActivity
 * Created by jiajie on 16/9/12.
 */
public class PhotoSelectorActivity extends AppCompatActivity {

    private static final String TAG = "PhotoSelectorActivity";

    private static final int DATA_LOADED = 0x110;

    private GridView mGridView;
    private List<String> mImages;
    private ImageAdapter mAdapter;

    private RelativeLayout mBottomLayout;
    private TextView mDirName;
    private TextView mDirCount;

    private File mCurrentDir;
    private int mMaxCount;

    private List<FolderBean> mFolderBeans = new ArrayList<>();

    private ProgressDialog mProgressDialog;

    private ListImageDirPopupWindow mPopupWindow;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DATA_LOADED) {
                mProgressDialog.dismiss();
                //绑定数据到View
                dataToView();

                initDirPopupWindow();
            }
        }
    };

    private void initDirPopupWindow() {
        mPopupWindow = new ListImageDirPopupWindow(this, mFolderBeans);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });

        mPopupWindow.setOnDirSelectedListener(new ListImageDirPopupWindow.OnDirSelectedListener() {
            @Override
            public void onSelected(FolderBean folderBean) {
                mCurrentDir = new File(folderBean.getDir());
                mImages = Arrays.asList(mCurrentDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        return (filename.endsWith(".jpg") ||
                                filename.endsWith(".jpeg") ||
                                filename.endsWith(".png"));
                    }
                }));

                mAdapter = new ImageAdapter(PhotoSelectorActivity.this, mImages,
                        mCurrentDir.getAbsolutePath());
                mGridView.setAdapter(mAdapter);

                mDirCount.setText(String.valueOf(mImages.size()));
                mDirName.setText(folderBean.getName());

                mPopupWindow.dismiss();
            }
        });

    }

    /**
     * 内容区域变亮
     */
    private void lightOn() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_selector);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {

        mBottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.setAnimationStyle(R.style.dir_popup_anim);
                mPopupWindow.showAsDropDown(mBottomLayout, 0, 0);
                lightOff();
            }
        });


    }

    /**
     * 内容区域变暗
     */
    private void lightOff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    protected void dataToView() {
        if (mCurrentDir == null) {
            Toast.makeText(this, "未扫描到任何图片", Toast.LENGTH_SHORT).show();
            return;
        }

        mImages = Arrays.asList(mCurrentDir.list());

        mAdapter = new ImageAdapter(this, mImages, mCurrentDir.getAbsolutePath());
        mGridView.setAdapter(mAdapter);

        mDirCount.setText(String.valueOf(mMaxCount));
        mDirName.setText(mCurrentDir.getName());

    }

    /**
     * 利用ContentProvider扫描手机图片
     */
    private void initData() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "当前存储卡不可用！", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

        new Thread() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = getContentResolver();

                Cursor cursor = cr.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + " = ? or " +
                                MediaStore.Images.Media.MIME_TYPE + " = ? ",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                Set<String> mDirPaths = new HashSet<>();

                FilenameFilter filter = new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        return (filename.endsWith(".jpg") ||
                                filename.endsWith(".jpeg") ||
                                filename.endsWith(".png"));
                    }
                };

                if (cursor == null) {
                    Log.e(TAG, "run: cursor == null");
                    return;
                }

                while (cursor.moveToNext()) {
                    //图片路径
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                    //parentFile可能有很多图片，要避免重复生成parentFile
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null) continue;

                    String dirPath = parentFile.getAbsolutePath();
                    FolderBean folderBean = null;

                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        folderBean = new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImagePath(path);
                    }

                    if (parentFile.list() == null) continue;

                    int picSize = parentFile.list(filter).length;

                    folderBean.setCount(picSize);
                    mFolderBeans.add(folderBean);

                    if (picSize > mMaxCount) {
                        mMaxCount = picSize;
                        mCurrentDir = parentFile;
                    }
                }
                cursor.close();

                mHandler.sendEmptyMessage(DATA_LOADED);

            }
        }.start();


    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.grid_view);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mDirName = (TextView) findViewById(R.id.dir_name);
        mDirCount = (TextView) findViewById(R.id.dir_count);
    }


}
