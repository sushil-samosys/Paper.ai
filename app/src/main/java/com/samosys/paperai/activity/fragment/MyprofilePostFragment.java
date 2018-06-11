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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.ShowPostBean;
import com.samosys.paperai.activity.adapter.MypostAdapter;
import com.samosys.paperai.activity.utils.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyprofilePostFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyprofilePostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyprofilePostFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<ShowPostBean> postList;
    private RecyclerView rv_myPost;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public MyprofilePostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyprofilePostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyprofilePostFragment newInstance(String param1, String param2) {
        MyprofilePostFragment fragment = new MyprofilePostFragment();
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
        View view = inflater.inflate(R.layout.fragment_myprofile_post, container, false);
        rv_myPost = (RecyclerView) view.findViewById(R.id.rv_myPost);
        rv_myPost.setNestedScrollingEnabled(false);
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
        getfeedpost();
    }

    private void getfeedpost() {
        postList.clear();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.whereEqualTo("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));

//        query.whereEqualTo("worksapce", ParseObject.createWithoutData("WorkSpace", "awcjqtfdrI"));
        query.include("user");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    Log.e("MYPOST", objects.size() + "");
                    for (int i = 0; i < objects.size(); i++) {
                        String post_image="no_image";
                        ParseObject parseObject = objects.get(i).getParseObject("user");
                        String text = objects.get(i).getString("text");
                        String objectId = objects.get(i).getObjectId();

                        ParseFile user_imge = (ParseFile) parseObject.get("profileimage");

                        String updatedAt = objects.get(i).getUpdatedAt().toString();
                        String createdAt = objects.get(i).getCreatedAt().toString();
                        if (objects.get(i).has("postImage")) {
                            ParseFile image = (ParseFile) objects.get(i).get("postImage");
                            post_image = image.getUrl();
                        }
                        String post_type = objects.get(i).getString("post_type");

                        String tagUserId = objects.get(i).getString("tagUserId");
                        String post_file_url = objects.get(i).getString("post_file_url");
                        String post_file = objects.get(i).getString("post_file");
                        String userTag = objects.get(i).getString("userTag");
                        int CommentCount = objects.get(i).getInt("CommentCount");
                        int likesCount = objects.get(i).getInt("likesCount");
                        postList.add(new ShowPostBean(ParseUser.getCurrentUser().get("fullname") + "",user_imge, text, objectId, updatedAt, createdAt, post_image, post_type, tagUserId, post_file_url, userTag, CommentCount, likesCount, ParseUser.getCurrentUser().getObjectId(), post_file));

                        //  postList.add(new ShowPostBean(username, text, objectId, updatedAt, createdAt, image, post_type, tagUserId, post_file_url, userTag, CommentCount, likesCount,post_userID));

                    }
                    Collections.reverse(postList);
                    MypostAdapter adapter = new MypostAdapter(getActivity(), postList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rv_myPost.setHasFixedSize(true);
                    rv_myPost.setLayoutManager(layoutManager);
                    rv_myPost.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                    rv_myPost.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

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
