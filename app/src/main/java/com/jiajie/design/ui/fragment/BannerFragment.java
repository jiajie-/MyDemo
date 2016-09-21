package com.jiajie.design.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiajie.design.R;
import com.jiajie.design.api.DataResponse;
import com.jiajie.design.api.DataResult;
import com.jiajie.design.api.GankService;
import com.jiajie.design.widgets.loopview.LoopView;
import com.jiajie.design.widgets.loopview.LoopViewAdapter;
import com.jiajie.design.widgets.loopview.LoopViewItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * BezierFragment for bezier view.
 * Created by jiajie on 16/9/9.
 */
public class BannerFragment extends Fragment {

    private static final String TAG = "BannerFragment";

    private MyHandler mHandler;

    private List<DataResult> dataResults;

    private LoopView mLoopView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new MyHandler(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_banner, container, false);

        mLoopView = (LoopView) rootView.findViewById(R.id.loop_view);

        mLoopView.setOnItemClickListener(new LoopViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.e(TAG, "onItemClick: " + position);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                GankService.getGankApi().getData("福利", 5, 1)
                        .enqueue(new Callback<DataResponse<DataResult>>() {
                            @Override
                            public void onResponse(Response<DataResponse<DataResult>> response
                                    , Retrofit retrofit) {
                                if (response.body() != null) {
                                    DataResponse<DataResult> gank = response.body();
                                    List<DataResult> results = gank.getResults();

                                    //call handler to update UI
                                    Message msg = Message.obtain();
                                    msg.obj = results;
                                    mHandler.sendMessage(msg);
                                } else {
                                    Log.d(TAG, "onResponse: response.body()==null");
                                }

                            }

                            @Override
                            public void onFailure(Throwable t) {
                                t.printStackTrace();
                            }
                        });

            }
        }).start();

        return rootView;
    }

    private static class MyHandler extends Handler {

        private WeakReference<BannerFragment> bannerFragmentWeakReference;

        public MyHandler(BannerFragment bannerFragment) {
            this.bannerFragmentWeakReference = new WeakReference<>(bannerFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            BannerFragment bannerFragment = bannerFragmentWeakReference.get();

            if (bannerFragment != null) {
                bannerFragment.dataResults = (List<DataResult>) msg.obj;

                List<LoopViewItem> data = new ArrayList<>();

                for (int i = 0; i < bannerFragment.dataResults.size(); i++) {
                    LoopViewItem item = new LoopViewItem();
                    item.setImageUrl(bannerFragment.dataResults.get(i).getUrl());
                    item.setDesc(bannerFragment.dataResults.get(i).getDesc());
                    data.add(item);
                }
                bannerFragment.mLoopView.setLoopData(data);
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }
}
