/*
 * Copyright (C) 2016 Ronald Martin <hello@itsronald.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 10/12/16 11:22 PM.
 */

package com.samosys.paperai.activity.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.Myfollowlist;
import com.samosys.paperai.activity.Bean.WorkspaceBean;
import com.samosys.paperai.activity.activity.NewWorkspaceActivity;
import com.samosys.paperai.activity.adapter.MyFollowsAdapter;
import com.samosys.paperai.activity.adapter.OtherWorksapace;
import com.samosys.paperai.activity.adapter.WorkspaceAdapter;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass that displays its page number in a ViewPager.
 * <p>
 * <p>
 * Use the {@link PageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PageFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TEXT = "param1";
    TextView txt_mywork;
    CustomFonts customFonts;
    ArrayList<WorkspaceBean> workList, otherList;
    ArrayList<Myfollowlist> myFollowlist;
    ArrayList<String> demolist;
    RecyclerView lv_workspce, lv_other_space, lv_myfolow;
    EditText edtsearchSpace;
    String work_Id = "";
    ImageView img_addworkspace;
    WorkspaceAdapter adapter;
    @Nullable
    private String pageText;

    public PageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pageText Parameter 1.
     * @return A new instance of fragment PageFragment.
     */
    public static PageFragment newInstance(@NonNull final String pageText) {
        PageFragment fragment = new PageFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TEXT, pageText);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customFonts = new CustomFonts(getActivity());
        workList = new ArrayList<>();
        otherList = new ArrayList<>();
        demolist = new ArrayList<>();
        myFollowlist = new ArrayList<>();


        getmyList();



        txt_mywork.setTypeface(customFonts.HelveticaNeue);
        img_addworkspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewWorkspaceActivity.class);
                startActivity(intent);
            }
        });


    }

    public void getmyfolloelist() {
        demolist.clear();
        myFollowlist.clear();
        final ProgressDialog dialog = AppConstants.showProgressDialog(getActivity(), "Loading...");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("workspace_follower");
        query.whereEqualTo("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
        query.include("workspace");


        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (e == null) {
                    Log.e("getmyfolloelist", objects.size() + "");
                    if (objects.size() > 0) {


                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject parseObject = objects.get(i).getParseObject("workspace");
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
                      //      workList.add(new WorkspaceBean(ParseUser.getCurrentUser().getObjectId(), user, mission, updatedAt, workspace_name, createdAt, image, user_name, workspace_url));

                        }

//                        MyFollowsAdapter myFollowsAdapter = new MyFollowsAdapter(getActivity(), workList);
//                        lv_myfolow.setHasFixedSize(true);
//                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//                        lv_myfolow.setLayoutManager(layoutManager);
//                        lv_myfolow.setAdapter(myFollowsAdapter);
//                        myFollowsAdapter.notifyDataSetChanged();

//                        WorkspaceAdapter adapter = new WorkspaceAdapter(getActivity(), workList);
//                        lv_workspce.setHasFixedSize(true);
//                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//                        lv_workspce.setLayoutManager(layoutManager);
//                        lv_workspce.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
//                    }
                    }

                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.e("else", e.getMessage());
                    // error
                }
                getOtherList();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

//        getmyList();
//        getOtherList();

    }

    public void getOtherList() {
        otherList.clear();
        final ProgressDialog dialog = AppConstants.showProgressDialog(getActivity(), "Loading...");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("WorkSpace");
        query.whereNotEqualTo("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (e == null) {
                    Log.e("getOtherList", objects.size() + "");

                    for (int i = 0; i < objects.size(); i++) {
                        String objectId = objects.get(i).getObjectId();
                        String user = objects.get(i).getString("user");
                        String mission = objects.get(i).getString("mission");
                        String updatedAt = objects.get(i).getUpdatedAt().toString();
                        String workspace_name = objects.get(i).getString("workspace_name");
                        String createdAt = objects.get(i).getCreatedAt().toString();
//                        String image = objects.get(i).getString("image");
                        ParseFile image = (ParseFile) objects.get(i).get("image");
                        String user_name = objects.get(i).getString("user_name");
                        String workspace_url = objects.get(i).getString("workspace_url");
                        if (!demolist.contains(workspace_url)) {
//                            otherList.add(new WorkspaceBean(objectId, user, mission, updatedAt, workspace_name, createdAt, image, user_name, workspace_url));
                        }
                    }
                    OtherWorksapace adapter = new OtherWorksapace(getActivity(), otherList, PageFragment.this);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    lv_other_space.setLayoutManager(layoutManager);

                    lv_other_space.setAdapter(adapter);


                    adapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.e("else", e.getMessage());
                    // error
                }
            }
        });
    }

    public void getmyList() {
        workList.clear();
        //demolist.clear();
        final ProgressDialog dialog = AppConstants.showProgressDialog(getActivity(), "Loading...");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("WorkSpace");
        query.whereEqualTo("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (e == null) {
                    Log.e("getmyList", objects.toString() + "");

                    for (int i = 0; i < objects.size(); i++) {
                        String objectId = objects.get(i).getObjectId();
//                        String user = objects.get(i).getString("user");
                        ParseObject user = ParseObject.createWithoutData("WorkSpace","user");

                        String mission = objects.get(i).getString("mission");
                        String updatedAt = objects.get(i).getUpdatedAt().toString();
                        String workspace_name = objects.get(i).getString("workspace_name");
                        String createdAt = objects.get(i).getCreatedAt().toString();
//                        String image = objects.get(i).getString("image");
                        ParseFile image = (ParseFile) objects.get(i).get("image");
                        String user_name = objects.get(i).getString("user_name");
                        String workspace_url = objects.get(i).getString("workspace_url");
                        // demolist.add(workspace_url);
                       // workList.add(new WorkspaceBean(objectId, user, mission, updatedAt, workspace_name, createdAt, image, user_name, workspace_url));
                    }


//                    WorkspaceAdapter adapter = new WorkspaceAdapter(getActivity(), workList);
//                    lv_workspce.setHasFixedSize(true);
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//                    lv_workspce.setLayoutManager(layoutManager);
//                    lv_workspce.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();



                } else {
//                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.e("else", e.getMessage());
                    // error
                }

                getmyfolloelist();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        txt_mywork = (TextView) view.findViewById(R.id.txt_mywork);
        edtsearchSpace = (EditText) view.findViewById(R.id.edtsearchSpace);
        lv_workspce = (RecyclerView) view.findViewById(R.id.lv_workspce);
        lv_other_space = (RecyclerView) view.findViewById(R.id.lv_other_space);
        lv_myfolow = (RecyclerView) view.findViewById(R.id.lv_myfolow);

        img_addworkspace = (ImageView) view.findViewById(R.id.img_addworkspace);
        lv_workspce.setNestedScrollingEnabled(false);
        lv_myfolow.setNestedScrollingEnabled(false);
        lv_other_space.setNestedScrollingEnabled(false);


        return view;
    }
}
