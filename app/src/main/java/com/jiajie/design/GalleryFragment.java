package com.jiajie.design;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiajie.design.api.DataResponse;
import com.jiajie.design.api.DataResult;
import com.jiajie.design.api.GankService;
import com.jiajie.design.widgets.DeleteRecyclerView;

import java.lang.ref.WeakReference;
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
public class GalleryFragment extends Fragment implements LoadDataScrollController.OnRecycleRefreshListener {

    private static final String TAG = GalleryFragment.class.getSimpleName();
//    private static final String ARG_COLUMN_COUNT = "column-count";
//    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private SwipeRefreshLayout mRefreshLayout;
    private static final int MSG_REFRESH = 1;
    private static final int MSG_LOAD_MORE = 2;
    private GalleryItemAdapter adapter;
    private MyHandler handler;
    private int currentPage = 1;

    private LoadDataScrollController mController;

    private static class MyHandler extends Handler {

        private final WeakReference<GalleryFragment> mFragment;

        public MyHandler(GalleryFragment fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GalleryFragment gallery = mFragment.get();
            switch (msg.what) {
                case MSG_REFRESH:
                    //刷新一次就添加1页数据
//                    gallery.mImages.add(0, "http://ww4.sinaimg.cn/large/610dc034jw1f5md1e68p9j20fk0ncgo0.jpg");
                    //通知界面改变
                    gallery.adapter.notifyDataSetChanged();
                    //刷新状态改变了
                    gallery.mRefreshLayout.setRefreshing(false);
                    gallery.mController.setLoadDataStatus(false);
                    break;

                case MSG_LOAD_MORE:
                    gallery.currentPage++;
                    gallery.loadData();

//                    gallery.mController.setLoadDataStatus(false);


                    break;
                default:
                    break;
            }


        }
    }

    //test Glide
    public List<DataResult> mImages = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GalleryFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static GalleryFragment newInstance() {
        GalleryFragment fragment = new GalleryFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        Context context = view.getContext();
        handler = new MyHandler(this);

        DeleteRecyclerView recyclerView = (DeleteRecyclerView) view.findViewById(R.id.list);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mController = new LoadDataScrollController(this);
        recyclerView.addOnScrollListener(mController);
        mRefreshLayout.setOnRefreshListener(mController);

        adapter = new GalleryItemAdapter(getContext(), mImages, mListener);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

//        if (mColumnCount <= 1) {
//        } else {
//            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//        }
        // Set the adapter

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
        loadData();
    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GankService.getGankApi().getData("福利", 10, currentPage)
                        .enqueue(new Callback<DataResponse<DataResult>>() {
                            @Override
                            public void onResponse(Response<DataResponse<DataResult>> response
                                    , Retrofit retrofit) {
                                if (response.body() != null) {
                                    DataResponse<DataResult> gank = response.body();
//                                    Log.d(TAG, gank.toString());
                                    List<DataResult> results = gank.getResults();
                                    for (DataResult result : results) {
                                        Log.i(TAG, result.toString());
                                        mImages.add(result);
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
        mController.setLoadDataStatus(false);
    }

    @Override
    public void refresh() {
        Log.e(TAG, "refresh: ");
        handler.sendEmptyMessageDelayed(MSG_REFRESH, 200);
    }

    @Override
    public void loadMore() {
        Log.e(TAG, "loadMore: ");
        handler.sendEmptyMessageDelayed(MSG_LOAD_MORE, 200);
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
        void onListFragmentInteraction(DataResult item);
    }
}
