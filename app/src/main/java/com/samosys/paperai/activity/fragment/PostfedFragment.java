package com.samosys.paperai.activity.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.CircularProgressBar;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.OnSwipeTouchListener;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostfedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostfedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostfedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static int CAMERA_REQUEST_CODE_VEDIO = 0;
    private static String mRecognitionResults = "";
    public JSONObject jsonModels = null;
    public View mView = null;
    CircularProgressBar circularProgressbar;
    TextView txt_record, txt_rec;
    ImageView img_mic;
    RelativeLayout parent_postfeeed;
    int prog = 0;

    Timer timer;
    CustomFonts customFonts;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Handler mHandler = null;
    private OnFragmentInteractionListener mListener;

    public PostfedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostfedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostfedFragment newInstance(String param1, String param2) {
        PostfedFragment fragment = new PostfedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        findview();
        View v = inflater.inflate(R.layout.fragment_postfed, container, false);

        findview(v);
        return v;
    }


    private void findview(View v) {
        customFonts = new CustomFonts(getActivity());
        circularProgressbar = (CircularProgressBar) v.findViewById(R.id.circularProgressbar);
        img_mic = (ImageView) v.findViewById(R.id.img_mic);
        txt_record = (TextView) v.findViewById(R.id.txt_record);
        txt_rec = (TextView) v.findViewById(R.id.txt_rec);
        parent_postfeeed = (RelativeLayout) v.findViewById(R.id.parent_postfeeed);
        txt_record.setTypeface(customFonts.HelveticaNeue);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Uri videoUri = data.getData();
            Log.e("VIDIO", videoUri.toString());
//           String path = Utils.getRealPathFromURI(videoUri, this);
//            manageVideo(path);///What ever you want to do with your vedio
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHandler = new Handler();

        img_mic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        prog = 0;

                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            public void run() {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Your code

                                        prog++;
                                        if (prog <= 60) {
                                            circularProgressbar.setProgress(prog);
                                            Log.e("PROGRESS22", prog + "");

                                        } else {
                                            circularProgressbar.setProgress(0);

                                            timer.cancel();
                                        }
                                    }
                                });
                            }
                        }, 1000, 1000);


                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        prog = 0;
                        timer.cancel();
                        Log.e("PROGRESS11", prog + "");
                        circularProgressbar.setProgress(prog);
                        // RELEASED
                        return true; // if you want to handle the touch event

                }
                return false;
            }
        });

        parent_postfeeed.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeTop() {

            }

            public void onSwipeRight() {
//                callwatson();
            }

            public void onSwipeLeft() {

                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent,
                            CAMERA_REQUEST_CODE_VEDIO);
                }
            }

            public void onSwipeBottom() {

            }

        });
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
