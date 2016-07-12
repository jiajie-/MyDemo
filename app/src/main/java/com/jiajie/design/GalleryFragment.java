package com.jiajie.design;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GalleryFragment.GalleryInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryFragment extends Fragment {

    private static final String TAG = GalleryFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GalleryInteractionListener mListener;

    //test Glide
    private ImageView mImageView;
    public static String[] eatFoodyImages = {
            "http://ww4.sinaimg.cn/small/610dc034jw1f5md1e68p9j20fk0ncgo0.jpg",
            "http://ww1.sinaimg.cn/small/610dc034jw1f5l6tgzc2sj20zk0nqgq0.jpg",
            "http://ww2.sinaimg.cn/small/610dc034jw1f5k1k4azguj20u00u0421.jpg",
            "http://ww1.sinaimg.cn/small/610dc034jw1f5hpzuy3r7j20np0zkgpd.jpg",
            "http://ww3.sinaimg.cn/small/610dc034jw1f5d36vpqyuj20zk0qo7fc.jpg",
            "http://ww2.sinaimg.cn/small/610dc034jw1f5aqgzu2oej20rt15owo7.jpg",
            "http://ww1.sinaimg.cn/small/610dc034jw1f566a296rpj20lc0sggoj.jpg",
            "http://ww3.sinaimg.cn/mw690/81309c56jw1f4v6mic7r5j20m80wan5n.jpg",
            "http://ww4.sinaimg.cn/small/610dc034jw1f4vmdn2f5nj20kq0rm755.jpg",
            "http://ww1.sinaimg.cn/mw690/692a6bbcgw1f4fz7s830fj20gg0o00y5.jpg",
            "http://ww1.sinaimg.cn/mw690/692a6bbcgw1f4fz6g6wppj20ms0xp13n.jpg",
            "http://ww3.sinaimg.cn/mw690/81309c56jw1f4sx4ybttdj20ku0vd0ym.jpg",
            "http://ww4.sinaimg.cn/mw690/9844520fjw1f4fqribdg1j21911w0kjn.jpg",
            "http://ww4.sinaimg.cn/small/610dc034jw1f4nog8tjfrj20eg0mgab7.jpg",
    };

    public GalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment MusicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GalleryFragment newInstance(String param1, String param2) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: " + this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        mImageView = (ImageView) view.findViewById(R.id.iv_image);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        String internetUrl = "http://ww3.sinaimg.cn/small/610dc034gw1f5pu0w0r56j20m80rsjuy.jpg";
        String filePath = "/storage/emulated/0/Pictures/example_video.mp4";
        int resourceId = R.mipmap.ic_launcher;
        File file = new File(Environment.
                getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Screenshots/",
                "Screenshot_2016-03-29-11-00-09.png");
        Uri uri = resourceIdToUri(getContext(), R.mipmap.ic_launcher);

        //from URL
        Glide.with(getContext())
//                .load(internetUrl)
//                .load(resourceId)
//                .load(file)
                .load(uri)
                .into(mImageView);

        //from local

    }

    private Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse("android.resource://" + context.getPackageName() + File.separator
                + resourceId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onGalleryInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
        if (context instanceof GalleryInteractionListener) {
            mListener = (GalleryInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
        mListener = null;
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
    public interface GalleryInteractionListener {
        // TODO: Update argument type and name
        void onGalleryInteraction(Uri uri);
    }
}
