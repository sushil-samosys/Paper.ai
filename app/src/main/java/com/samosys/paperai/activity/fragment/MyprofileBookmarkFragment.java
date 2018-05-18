package com.samosys.paperai.activity.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.ShowPostBean;
import com.samosys.paperai.activity.adapter.MyProfilebookmarsAdapter;
import com.samosys.paperai.activity.utils.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyprofileBookmarkFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyprofileBookmarkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyprofileBookmarkFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<ShowPostBean> postList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rv_mypostBookmark;
    private OnFragmentInteractionListener mListener;

    public MyprofileBookmarkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyprofileBookmarkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyprofileBookmarkFragment newInstance(String param1, String param2) {
        MyprofileBookmarkFragment fragment = new MyprofileBookmarkFragment();
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
        View view = inflater.inflate(R.layout.fragment_myprofile_bookmark, container, false);
        rv_mypostBookmark = (RecyclerView) view.findViewById(R.id.rv_mypostBookmark);
        rv_mypostBookmark.setNestedScrollingEnabled(false);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        postList = new ArrayList<>();
        getmybookmark();
    }

    private void getmybookmark() {
        postList.clear();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("post_bookmarks");
        //query.whereEqualTo("post_id", ParseObject.createWithoutData("Post", listitem.get(position).getObjectId()));
        query.whereEqualTo("user_id", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
        query.include("post_id.user_id");
//        query.include("user");
        Log.e("username123", ParseUser.getCurrentUser().getObjectId() + "");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                Log.e("mybookmark", objects.size() + "");
                if (e == null) {


                    for (int i = 0; i < objects.size(); i++) {

                        ParseObject parseObject = objects.get(i).getParseObject("post_id");
                        ParseObject userObject = parseObject.getParseObject("user");


                        String username = null;
                        try {
                            username = userObject.fetchIfNeeded().getString("fullname");
                            Log.e("username", ParseUser.getCurrentUser().getObjectId() + "");
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        ParseFile user_imge = (ParseFile) userObject.get("profileimage");

                        String text = parseObject.getString("text");

                        String post_userID = userObject.getObjectId();

                        ParseFile image = (ParseFile) parseObject.get("postImage");
                        String post_image = image.getUrl();
                        String post_type = parseObject.getString("post_type");
                        Log.e("mybookmark_image", image + "");
                        String tagUserId = parseObject.getString("tagUserId");
                        String post_file_url = parseObject.getString("post_file_url");
                        String userTag = parseObject.getString("userTag");
                        int CommentCount = parseObject.getInt("CommentCount");
                        int likesCount = parseObject.getInt("likesCount");


                        String objectId = objects.get(i).getObjectId();


                        String updatedAt = objects.get(i).getUpdatedAt().toString();
                        String createdAt = objects.get(i).getCreatedAt().toString();

                        postList.add(new ShowPostBean(username, user_imge, text, objectId, updatedAt, createdAt, post_image, post_type, tagUserId, post_file_url, userTag, CommentCount, likesCount, post_userID));

                    }

                    Collections.reverse(postList);
                    MyProfilebookmarsAdapter adapter = new MyProfilebookmarsAdapter(getActivity(), postList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rv_mypostBookmark.setHasFixedSize(true);
                    rv_mypostBookmark.setLayoutManager(layoutManager);
                    rv_mypostBookmark.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                    rv_mypostBookmark.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                } else {


                    Log.e("else", e.getMessage());
                    // error
                }
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
