package com.jiajie.design;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiajie.design.api.DataResponse;
import com.jiajie.design.api.DataResult;
import com.jiajie.design.api.GankService;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class GalleryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = GalleryFragment.class.getSimpleName();
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private SwipeRefreshLayout refresh;
    private static final int REFRESH = 1;
    private GalleryItemAdapter adapter;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    //刷新一次就添加1页数据
                    mImages.add(0, "http://ww4.sinaimg.cn/large/610dc034jw1f5md1e68p9j20fk0ncgo0.jpg");

                    //通知界面改变
                    adapter.notifyDataSetChanged();
                    //刷新状态改变了
                    refresh.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    };

    //test Glide
    public static List<String> mImages = new ArrayList<>();

    static {
//        mImages.add("http://ww4.sinaimg.cn/large/610dc034jw1f5md1e68p9j20fk0ncgo0.jpg");
//        mImages.add("http://ww1.sinaimg.cn/large/610dc034jw1f5l6tgzc2sj20zk0nqgq0.jpg");
//        mImages.add("http://ww2.sinaimg.cn/large/610dc034jw1f5k1k4azguj20u00u0421.jpg");
//        mImages.add("http://ww1.sinaimg.cn/large/610dc034jw1f5hpzuy3r7j20np0zkgpd.jpg");
//        mImages.add("http://ww3.sinaimg.cn/large/610dc034jw1f5d36vpqyuj20zk0qo7fc.jpg");
//        mImages.add("http://ww2.sinaimg.cn/large/610dc034jw1f5aqgzu2oej20rt15owo7.jpg");
//        mImages.add("http://ww1.sinaimg.cn/large/610dc034jw1f566a296rpj20lc0sggoj.jpg");
//        mImages.add("http://ww3.sinaimg.cn/mw690/81309c56jw1f4v6mic7r5j20m80wan5n.jpg");
//        mImages.add("http://ww4.sinaimg.cn/large/610dc034jw1f4vmdn2f5nj20kq0rm755.jpg");
//        mImages.add("http://ww1.sinaimg.cn/mw690/692a6bbcgw1f4fz7s830fj20gg0o00y5.jpg");
//        mImages.add("http://ww3.sinaimg.cn/mw690/81309c56jw1f4sx4ybttdj20ku0vd0ym.jpg");
//        mImages.add("http://ww4.sinaimg.cn/mw690/9844520fjw1f4fqribdg1j21911w0kjn.jpg");
//        mImages.add("http://ww4.sinaimg.cn/large/610dc034jw1f4nog8tjfrj20eg0mgab7.jpg");
//        mImages.add("http://i.kinja-img.com/gawker-media/image/upload/s--B7tUiM5l--/gf2r69yorbdesguga10i.gif");
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GalleryFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static GalleryFragment newInstance(int columnCount) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        Context context = view.getContext();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);

        adapter = new GalleryItemAdapter(getContext(), mImages, mListener);

        // Set the adapter
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated: ");
        //set loading
        //start load images
        new Thread(new Runnable() {
            @Override
            public void run() {
                GankService.getGankApi().getData("福利", 10, 1)
                        .enqueue(new Callback<DataResponse<DataResult>>() {
                            @Override
                            public void onResponse(Response<DataResponse<DataResult>> response
                                    , Retrofit retrofit) {
                                if (response.body() != null) {
                                    DataResponse<DataResult> gank = response.body();
//                                    Log.d(TAG, gank.toString());
//
                                    List<DataResult> results = gank.getResults();
                                    for (DataResult result : results) {
                                        Log.i(TAG, result.toString());
                                        mImages.add(result.getUrl());
                                        adapter.notifyDataSetChanged();
                                        //set to recycler view
                                    }

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


    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(REFRESH, 200);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(String item);
    }
}
