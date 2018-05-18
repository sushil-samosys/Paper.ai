package com.samosys.paperai.activity.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samosys.paperai.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomefeedkFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomefeedkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomefeedkFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public HomefeedkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomefeedkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomefeedkFragment newInstance(String param1, String param2) {
        HomefeedkFragment fragment = new HomefeedkFragment();
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
        View v = inflater.inflate(R.layout.fragment_homefeedk, container, false);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //  getfeedpost();

//        getworkspace();

    }

    @Override
    public void onResume() {
        super.onResume();
//        getfeedpost();

    }

//    private void getworkspace() {
//
//        Log.e("AMAN_IS", AppConstants.loadPreferences(getActivity(), "workid") + "");
//        final ProgressDialog dialog = AppConstants.showProgressDialog(getActivity(), "Loading...");
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("WorkSpace");
////        query.whereEqualTo("objectId", AppConstants.loadPreferences(getActivity(), "workid"));
//        query.whereEqualTo("objectId", "awcjqtfdrI");
//
////        query.whereEqualTo("objectId",  ParseObject.createWithoutData("WorkSpace", AppConstants.loadPreferences(getActivity(), "workid").trim()));
//
//        query.findInBackground(new FindCallback<ParseObject>() {
//            public void done(List<ParseObject> objects1, ParseException e) {
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//                if (e == null) {
//
//                    Log.e("objects1Home", objects1.size() + "");
//                    if (objects1.size() > 0) {
//
//                        for (int i = 0; i < objects1.size(); i++) {
//
//
//                            String mission = objects1.get(i).getString("mission");
//                            String updatedAt = objects1.get(i).getUpdatedAt().toString();
//                            String workspace_name = objects1.get(i).getString("workspace_name");
//
//                            String createdAt = objects1.get(i).getCreatedAt().toString();
//                            String image = objects1.get(i).getString("image");
//                            ParseFile imageFile = (ParseFile) objects1.get(i).get("image");
//                            String post_image = imageFile.getUrl();
//                            String user_name = objects1.get(i).getString("user_name");
//                            Log.e("imageFile", imageFile + "");
//                            work_name.setText(First_Char_Capital.capitalizeString(workspace_name));
//                            work_txt.setText(First_Char_Capital.capitalizeString(mission));
//
//                            if (imageFile != null) {
//
//                                Picasso.with(getActivity())
//                                        .load(post_image)
//                                        .fit()
//                                        .centerCrop()
//
//                                        .into(work_space_img);
////                                imageFile.getDataInBackground(new GetDataCallback() {
////                                    @Override
////                                    public void done(byte[] data, ParseException e) {
////                                        Bitmap bmp = BitmapFactory
////                                                .decodeByteArray(
////                                                        data, 0,
////                                                        data.length);
////
////                                        // initialize
////                                        work_space_img.setImageBitmap(bmp);
////                                    }
////                                });
//                            }
//
////                        }
//                            String workspace_url = objects1.get(i).getString("workspace_url");
//                        }
//                    }
//
//
//                } else {
//                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                    Log.e("else", e.getMessage());
//                    // error
//                }
//            }
//        });
//    }
//
//    private void getfeedpost() {
//
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
//        query.whereEqualTo("worksapce", ParseObject.createWithoutData("WorkSpace", "awcjqtfdrI"));
////        query.whereEqualTo("worksapce", ParseObject.createWithoutData("WorkSpace", "awcjqtfdrI"));
//        query.include("user");
//        query.findInBackground(new FindCallback<ParseObject>() {
//            public void done(List<ParseObject> objects, ParseException e) {
//
//                if (e == null) {
//
//
//                    for (int i = 0; i < objects.size(); i++) {
//                        ParseObject parseObject = objects.get(i).getParseObject("user");
//
//                        String username = parseObject.getString("fullname");
//                        ParseFile user_imge = (ParseFile) parseObject.get("profileimage");
//                        Log.e("user_imge", user_imge + "");
//                        String post_userID = parseObject.getObjectId();
//                        // Log.e("poster_name", username);
//                        String text = objects.get(i).getString("text");
//                        String objectId = objects.get(i).getObjectId();
//
//
//                        String updatedAt = objects.get(i).getUpdatedAt().toString();
//                        String createdAt = objects.get(i).getCreatedAt().toString();
//                        ParseFile image = (ParseFile) objects.get(i).get("postImage");
//                        String post_image = image.getUrl();
//                        String post_type = objects.get(i).getString("post_type");
//
//                        String tagUserId = objects.get(i).getString("tagUserId");
//                        String post_file_url = objects.get(i).getString("post_file_url");
//                        String userTag = objects.get(i).getString("userTag");
//                        int CommentCount = objects.get(i).getInt("CommentCount");
//                        int likesCount = objects.get(i).getInt("likesCount");
//                        postList.add(new ShowPostBean(username, user_imge, text, objectId, updatedAt, createdAt, post_image, post_type, tagUserId, post_file_url, userTag, CommentCount, likesCount, post_userID));
//
//                    }
//                    FeedPostAdapter adapter = new FeedPostAdapter(getActivity(), postList);
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//                    Rv_feed_post.setHasFixedSize(true);
//                    Rv_feed_post.setLayoutManager(layoutManager);
//                    Rv_feed_post.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
//                    Rv_feed_post.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//
//                } else {
//                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                    Log.e("else", e.getMessage());
//                    // error
//                }
//            }
//        });
//    }

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
