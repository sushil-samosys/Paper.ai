package com.samosys.paperai.activity.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.UserBean;
import com.samosys.paperai.activity.Bean.WorkspaceBean;
import com.samosys.paperai.activity.adapter.MyFollowsAdapter;
import com.samosys.paperai.activity.adapter.Useradapter;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.First_Char_Capital;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuThirdFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuThirdFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String id = "", name = "";
    TextView txt_allprct, txtSpceName, userName, txt_followrs;
    ImageView user_image;
    RecyclerView rv_follower;
    CustomFonts customFonts;
    ArrayList<UserBean> userlist;
    ArrayList<WorkspaceBean> workList;
    ArrayList<String> demolist;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public MenuThirdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MenuThirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuThirdFragment newInstance(String param1) {
        MenuThirdFragment fragment = new MenuThirdFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

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
//        txtprojectName
        View view = inflater.inflate(R.layout.fragment_menu_third, container, false);
        txt_allprct = (TextView) view.findViewById(R.id.txt_allprct);
        txt_followrs = (TextView) view.findViewById(R.id.txt_followrs);
        userName = (TextView) view.findViewById(R.id.userName);
        user_image = (ImageView) view.findViewById(R.id.user_image);
        rv_follower = (RecyclerView) view.findViewById(R.id.rv_follower);
        rv_follower.setNestedScrollingEnabled(false);
        txtSpceName = (TextView) view.findViewById(R.id.txtSpceName);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customFonts = new CustomFonts(getActivity());
        id = AppConstants.loadPreferences(getActivity(), "workid");
        name = AppConstants.loadPreferences(getActivity(), "workname");
        workList = new ArrayList<>();
        demolist = new ArrayList<>();
        userlist = new ArrayList<>();
        txtSpceName.setTypeface(customFonts.HelveticaNeueMedium);
        txt_followrs.setTypeface(customFonts.HelveticaNeueMedium);
        txt_allprct.setTypeface(customFonts.HelveticaNeueBold);
        userName.setTypeface(customFonts.HelveticaNeueBold);
        txtSpceName.setText(name);
        txt_allprct.setText("Member(1)");
        getuserdata();
        //getmyfolloelist();
        getallmember();

    }

    private void getallmember() {
        final ProgressDialog dialog = AppConstants.showProgressDialog(getActivity(), "Loading...");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("workspace_follower");
        query.whereEqualTo("WorkSpace", ParseObject.createWithoutData("workspace", AppConstants.loadPreferences(getActivity(), "workid")));
        query.include("user");


        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (e == null) {
                    txt_followrs.setText("Followers(" + userlist.size() + ")");
                    Log.e("parseObject", ""+objects.size());
                    if (objects.size() > 0) {


                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject parseObject = objects.get(i).getParseObject("User");
                            Log.e("parseObject", parseObject.toString());
                            String user = parseObject.getString("user");
                            String mission = parseObject.getString("mission");
                            String updatedAt = parseObject.getUpdatedAt().toString();
                            String workspace_name = parseObject.getString("workspace_name");
                            String createdAt = parseObject.getCreatedAt().toString();
//                            String image = parseObject.getString("image");
                            ParseFile image = (ParseFile) objects.get(i).get("image");
                            String user_name = parseObject.getString("user_name");
                            String workspace_url = parseObject.getString("workspace_url");

                            demolist.add(workspace_url);
//                            workList.add(new WorkspaceBean(ParseUser.getCurrentUser().getObjectId(), user, mission, updatedAt, workspace_name, createdAt, image, user_name, workspace_url));

                        }

                    }


                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.e("else", e.getMessage());
                    // error
                }
            }
        });
    }

    public void getmyfolloelist() {


        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        String objectId = objects.get(i).getObjectId();
                        String username = objects.get(i).getString("username");
                        String email = objects.get(i).getString("email");
                        Log.e("EMAIL_USER", email + ">>" + username);

                        String updatedAt = objects.get(i).getUpdatedAt().toString();
                        String createdAt = objects.get(i).getCreatedAt().toString();
                        String passion = objects.get(i).getString("passion");
                        ParseFile profileimage = (ParseFile) objects.get(i).get("profileimage");
                        String title = objects.get(i).getString("title");
                        String fullname = objects.get(i).getString("fullname");
                        userlist.add(new UserBean(objectId, username, email, updatedAt, createdAt, passion, profileimage, title, fullname));


                    }


                    Useradapter useradapter = new Useradapter(getActivity(), userlist);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rv_follower.setLayoutManager(layoutManager);
                    rv_follower.setAdapter(useradapter);
                    useradapter.notifyDataSetChanged();
                    txt_followrs.setText("Followers(" + userlist.size() + ")");
                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getuserdata() {

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        String objectId = objects.get(i).getObjectId();
                        String username = objects.get(i).getString("username");
                        String email = objects.get(i).getString("email");
                        Log.e("EMAIL_USER", email + ">>" + username);

                        String updatedAt = objects.get(i).getUpdatedAt().toString();
                        String createdAt = objects.get(i).getCreatedAt().toString();
                        String passion = objects.get(i).getString("passion");
                        ParseFile profileimage = (ParseFile) objects.get(i).get("profileimage");
                        String user_name = objects.get(i).getString("title");
                        String fullname = objects.get(i).getString("fullname");
                        userName.setText(First_Char_Capital.capitalizeString(fullname));

                        profileimage.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                Bitmap bmp = BitmapFactory
                                        .decodeByteArray(
                                                data, 0,
                                                data.length);

                                // initialize
                                user_image.setImageBitmap(bmp);
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
