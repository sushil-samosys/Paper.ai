package com.samosys.paperai.activity.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.samosys.paperai.activity.AudioRecord.RecordingService;
import com.samosys.paperai.activity.Bean.CategoriesProjectbeanclass;
import com.samosys.paperai.activity.Bean.ChildBean;
import com.samosys.paperai.activity.Bean.ParentBean;
import com.samosys.paperai.activity.Bean.ProjctBean;
import com.samosys.paperai.activity.Bean.ShowPostBean;
import com.samosys.paperai.activity.Bean.WorkspaceBean;
import com.samosys.paperai.activity.adapter.FeedPostAdapter;
import com.samosys.paperai.activity.adapter.MenuProjectAdapter;
import com.samosys.paperai.activity.adapter.WorkspaceAdapter;
import com.samosys.paperai.activity.utils.AnimatorUtils;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.ArcLayout;
import com.samosys.paperai.activity.utils.CircularProgressBar;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.First_Char_Capital;
import com.samosys.paperai.activity.utils.MarshMallowPermission;
import com.samosys.paperai.activity.utils.NetworkAvailablity;
import com.samosys.paperai.activity.utils.RecyclerTouchListener;
import com.samosys.paperai.activity.utils.SimpleDividerItemDecoration;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import cn.zjy.actionsheet.ActionSheet;
import de.hdodenhof.circleimageview.CircleImageView;

//amirkabbara@jadwalak.com, amir@papr.ai
public class HomeFeedActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";

    public static File file = null;
    public static int prog = 0;
    public ArrayList<CategoriesProjectbeanclass> categoriesProjectbeanclasses;
    public ImageView work_space_img, imgWorkSpaceSetting;
    MarshMallowPermission marshMallowPermission;
    NestedScrollView scrollView;
    private ArrayList<String> childList;
    private ImageView img_wrokspace, mypic, img_addpost, img_log, img_mic;
    private RecyclerView listView;
    private CircleImageView imgMenuIcon;
    private boolean mStartRecording = true;
    private ImageView btn_addworkspace;
    private ProgressBar img_progrss_projct;
    private Timer timer;
    private ExpandableListView project_expandableList;
    private View menuLayout;
    private ArcLayout arcLayout;
    private TextView work_txt, workspace_name, txt_all, work_title;
    private CustomFonts customFonts;
    private RecyclerView Rv_feed_post;
    private ArrayList<ShowPostBean> postList;
    private ArrayList<WorkspaceBean> workList;
    private CircularProgressBar circularProgressBar;
    private WorkspaceAdapter adapter;
    private MenuProjectAdapter projectAdapter;
    private Button btn_recordVoice, btn_camera, btn_text;
    private DrawerLayout drawerLayout;
    private int img_height, img_width;
    private ArrayList<ProjctBean> projectList;
    private HashMap<String, List<String>> expandableListDetail;
    private ArrayList<ParentBean> parentBeans = new ArrayList<>();
    private ArrayList<ChildBean> childBeans;
    private MediaRecorder mRecorder = null;

    private RelativeLayout rl_audioRecord;
    private String file_exts[] = {AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP};

    private ACProgressFlower dialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_feed);
        AppConstants.getstatusbar(HomeFeedActivity.this);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findview();


        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
            arcLayout.getChildAt(i).setOnClickListener(this);
        }
        img_addpost.setOnClickListener(this);


        BitmapDrawable bd = (BitmapDrawable) HomeFeedActivity.this.getResources().getDrawable(R.mipmap.img_feed_center_1);
        img_height = bd.getBitmap().getHeight() + (img_height * 2);
        img_width = bd.getBitmap().getWidth() + (img_width);
        if (NetworkAvailablity.chkStatus(HomeFeedActivity.this)) {
            getuserdata();
            getworkspaceList();
        } else {
            Toast.makeText(HomeFeedActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }


        mypic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeFeedActivity.this, MyProfileActivity.class);
//                i.putExtra("id", listitem.get(position).getPost_userID());
                startActivity(i);
            }
        });
        img_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeFeedActivity.this, "Temporary logout", Toast.LENGTH_SHORT).show();
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();

                AppConstants.ClearPreferences(HomeFeedActivity.this);
                Intent intent = new Intent(HomeFeedActivity.this, LoginSignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        project_expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                parent.expandGroup(groupPosition);
                drawerLayout.closeDrawers();
                postList.clear();
                AppConstants.ALL = false;

                Log.e("FileAMAN", "" + childBeans.get(childPosition).getChildObjectId());
                AppConstants.savePreferences(HomeFeedActivity.this, "workid", workList.get(AppConstants.workPOS).getObjectId());
                AppConstants.savePreferences(HomeFeedActivity.this, "workname", workList.get(AppConstants.workPOS).getWorkspace_url());
                AppConstants.savePreferences(HomeFeedActivity.this, "projectID", childBeans.get(childPosition).getChildObjectId());

                workspace_name.setText(First_Char_Capital.capitalizeString(workList.get(AppConstants.workPOS).getWorkspace_name()));
                work_txt.setText(First_Char_Capital.capitalizeString(workList.get(AppConstants.workPOS).getWorkspace_name()));
                //  work_name.setText(First_Char_Capital.capitalizeString(workList.get(AppConstants.workPOS).getWorkspace_name()));
                ParseFile parseFile = workList.get(AppConstants.workPOS).getImage();
                String a = parseFile.getUrl();
                if (workList.get(AppConstants.workPOS).getImage() != null) {

                    Picasso.with(HomeFeedActivity.this)
                            .load(a)
                            .fit()
                            .centerCrop()

                            .into(work_space_img);
                    Picasso.with(HomeFeedActivity.this)
                            .load(a)
                            .fit()
                            .centerCrop()

                            .into(imgMenuIcon);
                }
                work_space_img.getLayoutParams().height = img_height;


                //  getfeedpost();
                adapter.setSelectedItem(AppConstants.workPOS);
                projectAdapter.setSelectedItem(groupPosition);
                projectAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        try {
            project_expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {


                    int a = parentBeans.get(groupPosition).getChildBeans().size();
                    if (a == 0) {


                        Log.e("FileAMAN", "" + parentBeans.get(groupPosition).getCategoryName());

                        AppConstants.savePreferences(HomeFeedActivity.this, "workid", workList.get(AppConstants.workPOS).getObjectId());
                        AppConstants.savePreferences(HomeFeedActivity.this, "workname", workList.get(AppConstants.workPOS).getWorkspace_url());
                        AppConstants.savePreferences(HomeFeedActivity.this, "projectID", parentBeans.get(groupPosition).getCategoryId());

                        drawerLayout.closeDrawers();
                        postList.clear();
                        //parentBeans.clear();
                        //  childBeans.clear();
                        project_expandableList.setVisibility(View.GONE);
                        img_progrss_projct.setVisibility(View.VISIBLE);
                        AppConstants.ALL = false;


                        workspace_name.setText(First_Char_Capital.capitalizeString(parentBeans.get(groupPosition).getCategoryName()));
                        work_txt.setText(First_Char_Capital.capitalizeString(parentBeans.get(groupPosition).getCategoryName()));
                        work_title.setText(First_Char_Capital.capitalizeString(workList.get(AppConstants.workPOS).getWorkspace_name()));

                        String img = parentBeans.get(groupPosition).getImage();
                        Log.e("FileAMAN", "" + img);
                        if (parentBeans.get(groupPosition).getImage() != null) {

                            Picasso.with(HomeFeedActivity.this)
                                    .load(img)
                                    .fit()
                                    .centerCrop()

                                    .into(work_space_img);
                            Picasso.with(HomeFeedActivity.this)
                                    .load(img)
                                    .fit()
                                    .centerCrop()

                                    .into(imgMenuIcon);
                        }
                        work_space_img.getLayoutParams().height = img_height;


                        getfeedpost();
                        adapter.setSelectedItem(AppConstants.workPOS);
                        projectAdapter.setSelectedItem(groupPosition);
                        projectAdapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();

                        //                    AppConstants.savePreferences(HomeFeedActivity.this, "projectID", childBeans.get(Integer.parseInt(AppConstants.loadPreferences(HomeFeedActivity.this, "proPosition"))).getChildObjectId());

                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

//        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                Log.e("scrollX",scrollX+"");
//                Log.e("scrollY",scrollY+"");
//                Log.e("oldScrollX",oldScrollX+"");
//                Log.e("oldScrollY",oldScrollY+"");
//            }
//        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (scrollView != null) {
                    //if (scrollView.getChildAt(0).getBottom() <= (scrollView.getHeight() + scrollView.getScrollY())) {
                    img_addpost.setVisibility(View.VISIBLE);
                } else {
                    img_addpost.setVisibility(View.INVISIBLE);
                }
            }

        });


        txt_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //txt_all.setBackgroundColor(getResources().getColor(R.color.black));

                String img = parentBeans.get(AppConstants.workPOS).getImage();
                Log.e("FileAMAN", "" + img);
                if (parentBeans.get(AppConstants.workPOS).getImage() != null) {

                    Picasso.with(HomeFeedActivity.this)
                            .load(img)
                            .fit()
                            .centerCrop()

                            .into(work_space_img);
                    Picasso.with(HomeFeedActivity.this)
                            .load(img)
                            .fit()
                            .centerCrop()

                            .into(imgMenuIcon);
                }
                work_space_img.getLayoutParams().height = img_height;
                AppConstants.ALL = true;
                postList.clear();
                getfeedpost();
                drawerLayout.closeDrawers();
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                SandriosCamera
//                        .with(HomeFeedActivity.this)
//                        .setShowPicker(false)
//                        .setMediaAction(CameraConfiguration.CAMERA_FACE_FRONT)
//                        .enableImageCropping(true)
//
//                        .launchCamera(new SandriosCamera.CameraCallback() {
//                            @Override
//                            public void onComplete(CameraOutputModel model) {
//
//
//                                // ParseFile file = new ParseFile("",model.getType());
//                                Uri uri = Uri.fromFile(new File(model.getPath()));
//                                try {
//                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(HomeFeedActivity.this.getContentResolver(), uri);
//
//                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                    // Compress image to lower quality scale 1 - 100
//                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                                    byte[] image = stream.toByteArray();
//
//
//                                    // Create the ParseFile
//
//                                    ParseFile parseFile = new ParseFile("postfile.jpg", image);
//                                    createPostImage(parseFile);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//
////                                Log.e("File", "" + model.getPath());
////                                Log.e("Type", "" + model.getType());
////                                Toast.makeText(getApplicationContext(), "Media captured.", Toast.LENGTH_SHORT).show();
//                            }
//                        });

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (!marshMallowPermission.checkPermissionForCamera()) {
                        marshMallowPermission.checkPermissionForCamera();
                    } else {
                        Intent intent = new Intent(HomeFeedActivity.this, CameraPostActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(HomeFeedActivity.this, CameraPostActivity.class);
                    startActivity(intent);
                }

                hideMenu();
            }
        });
        imgWorkSpaceSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opensettingbottomshet();
                drawerLayout.closeDrawers();

            }
        });
        btn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeFeedActivity.this, PostfeedActivity.class);
                intent.putExtra("file", "");
                intent.putExtra("post_type", "0");
                startActivity(intent);
            }
        });

        btn_recordVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startRecording();
                hideMenu();


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        img_addpost.setVisibility(View.GONE);
                        rl_audioRecord.setVisibility(View.VISIBLE);
                    }
                }, 1000);


            }
        });
        rl_audioRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (!marshMallowPermission.checkPermissionForRecord()) {
                        marshMallowPermission.requestPermissionForRecord();
                    } else {
                        //  showDialog();
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:


                                prog = 0;
                                //  if (prog == 1) {

                                onRecord(mStartRecording);
                                mStartRecording = !mStartRecording;
                                //}
                                timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                // Your code

                                                prog++;
                                                if (prog <= 60) {
                                                    circularProgressBar.setProgress(prog);
                                                    Log.e("PROGRESS22", prog + "");

                                                } else {
                                                    img_addpost.setVisibility(View.VISIBLE);
                                                    rl_audioRecord.setVisibility(View.INVISIBLE);

                                                    circularProgressBar.setProgress(0);
                                                    stopRecording();

                                                    timer.cancel();
                                                }
                                            }
                                        });
                                    }
                                }, 1000, 1000);


                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                                //    prog = 0;
                                timer.cancel();
                                img_addpost.setVisibility(View.VISIBLE);
                                rl_audioRecord.setVisibility(View.INVISIBLE);
                                Log.e("PROGRESS11", prog + "");
                                circularProgressBar.setProgress(prog);
                                stopRecording();
                                // RELEASED
                                return true; // if you want to handle the touch event

                        }
                    }
                } else {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:


                            prog = 0;
                            // if (prog == 1) {

                            onRecord(mStartRecording);
                            mStartRecording = !mStartRecording;
                            // }
                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Your code

                                            prog++;
                                            if (prog <= 60) {
                                                circularProgressBar.setProgress(prog);
                                                Log.e("PROGRESS22", prog + "");

                                            } else {
                                                img_addpost.setVisibility(View.VISIBLE);
                                                rl_audioRecord.setVisibility(View.INVISIBLE);

                                                circularProgressBar.setProgress(0);
                                                stopRecording();
                                                timer.cancel();
                                            }
                                        }
                                    });
                                }
                            }, 1000, 1000);


                            return true; // if you want to handle the touch event
                        case MotionEvent.ACTION_UP:
                            //+  prog = 0;
                            timer.cancel();
                            img_addpost.setVisibility(View.VISIBLE);
                            rl_audioRecord.setVisibility(View.INVISIBLE);
                            Log.e("PROGRESS11", prog + "");
                            circularProgressBar.setProgress(prog);
                            stopRecording();
                            // RELEASED
                            return true; // if you want to handle the touch event

                    }
                }


                return false;

            }

        });
        imgMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {

                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        btn_addworkspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //openworkspaceSheet();
                createnBrowseworkspace();
                drawerLayout.closeDrawers();
            }
        });

        listView.addOnItemTouchListener(new RecyclerTouchListener(HomeFeedActivity.this, listView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

//                AppConstants.workPOS = position;
                parentBeans.clear();
                childBeans.clear();
                project_expandableList.setVisibility(View.GONE);
                img_progrss_projct.setVisibility(View.VISIBLE);
                // load data here
                projectList.clear();
                expandableListDetail.clear();


//                                AppConstants.savePreferences(HomeFeedActivity.this, "workid", workList.get(position).getObjectId());

//                if (NetworkAvailablity.chkStatus(HomeFeedActivity.this)) {
//                    getworkspace("1");
//                    drawerLayout.closeDrawer(GravityCompat.END);
//                } else {
//                    Toast.makeText(HomeFeedActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
//                }
                adapter.setSelectedItem(position);
                adapter.notifyDataSetChanged();
//                getuncatproject();
//            getcatogory();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void createnBrowseworkspace() {
        ActionSheet actionSheet = new ActionSheet.Builder()
                // .setTitle("Title", Color.BLUE)
                //.setTitleTextSize(20)
                .setOtherBtn(new String[]{"Create Workspace", "Browse Other Workspace"}, new int[]{Color.parseColor("#2dc8bc"), Color.parseColor("#2dc8bc")})

                //.setOtherBtnSubTextSize(20)
                .setCancelBtn("Cancel", Color.parseColor("#2dc8bc"))
                //.setCancelBtnTextSize(30)
                .setCancelableOnTouchOutside(true)
                .setActionSheetListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isByBtn) {
                        // Toast.makeText(HomeFeedActivity.this, "onDismiss: " + isByBtn, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onButtonClicked(ActionSheet actionSheet, int index) {
                        if (index == 0) {
                            Intent intent = new Intent(HomeFeedActivity.this, NewWorkspaceActivity.class);
                            startActivity(intent);
                        }
                        if (index == 1) {

                            Intent intent = new Intent(HomeFeedActivity.this, BrowesWorkspace.class);

                            startActivity(intent);
                        }


                        //  Toast.makeText(HomeFeedActivity.this, "onButtonClicked: " + index, Toast.LENGTH_SHORT).show();
                    }
                }).build();

        actionSheet.show(getFragmentManager());
    }

    private void stopRecording() {
        Log.e("FILANMEEeee", ">>>>>" + file);
        // if (file != null) {
        Intent intent = new Intent(HomeFeedActivity.this, RecordingService.class);
        stopService(intent);

    }

    private void onRecord(boolean mStartRecording) {

        Intent intent = new Intent(HomeFeedActivity.this, RecordingService.class);

        if (mStartRecording) {
            // start recording

            File ff = new File(Environment.getExternalStorageDirectory() + "/SoundRecorder");
            if (!ff.exists()) {
                //folder /SoundRecorder doesn't exist, create the folder
                ff.mkdir();
            }

            //start Chronometer


            //start RecordingService
            startService(intent);
            //keep screen on while recording
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        } else {
//            //stop recording
//            mRecordButton.setImageResource(R.drawable.ic_mic_white_36dp);
//            //mPauseButton.setVisibility(View.GONE);
//            mChronometer.stop();
//            mChronometer.setBase(SystemClock.elapsedRealtime());
//            timeWhenPaused = 0;
//            mRecordingPrompt.setText(getString(R.string.record_prompt));

            // stopService(intent);
            //allow the screen to turn off again once recording is finished
            //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

//    private void openworkspaceSheet() {
//        final String workid = AppConstants.loadPreferences(HomeFeedActivity.this, "workid");
//        final String workname = AppConstants.loadPreferences(HomeFeedActivity.this, "workname");
//        ViewGroup nullParent = null;
//        LayoutInflater lInflater = (LayoutInflater) HomeFeedActivity.this.getSystemService(
//                Activity.LAYOUT_INFLATER_SERVICE);
//        View view = lInflater.inflate(R.layout.create_work_space_bottomsheet, null);
//        // view.getBackground().setAlpha(51);
//        final Dialog mBottomSheetDialog = new Dialog(HomeFeedActivity.this, R.style.MaterialDialogSheet);
//        mBottomSheetDialog.setContentView(view);
//        mBottomSheetDialog.setCancelable(true);
//        DisplayMetrics metrics = HomeFeedActivity.this.getResources().getDisplayMetrics();
//        int height = metrics.heightPixels;
//        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, (height) / 3);
//        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
//        mBottomSheetDialog.show();
//
//        TextView txt_cancel = (TextView) mBottomSheetDialog.findViewById(R.id.txt_cancel);
//        TextView txtworkspace = (TextView) mBottomSheetDialog.findViewById(R.id.txtworkspace);
//        TextView txtBrowseWS = (TextView) mBottomSheetDialog.findViewById(R.id.txtBrowseWS);
//
//
//        txt_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mBottomSheetDialog.dismiss();
//            }
//        });
//        txtworkspace.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeFeedActivity.this, NewWorkspaceActivity.class);
//                startActivity(intent);
//                mBottomSheetDialog.dismiss();
//            }
//        });
//        txtBrowseWS.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeFeedActivity.this, BrowesWorkspace.class);
//
//                startActivity(intent);
//
//                mBottomSheetDialog.dismiss();
//            }
//        });
//
//    }

    //    private void opensettingbottomshet() {
//        final String workid = AppConstants.loadPreferences(HomeFeedActivity.this, "workid");
//        final String workname = AppConstants.loadPreferences(HomeFeedActivity.this, "workname");
//        ViewGroup nullParent = null;
//        LayoutInflater lInflater = (LayoutInflater) HomeFeedActivity.this.getSystemService(
//                Activity.LAYOUT_INFLATER_SERVICE);
//        View view = lInflater.inflate(R.layout.fragment_modal_bottomsheet, null);
//        // view.getBackground().setAlpha(51);
//        final Dialog mBottomSheetDialog = new Dialog(HomeFeedActivity.this, R.style.MaterialDialogSheet);
//        mBottomSheetDialog.setContentView(view);
//        mBottomSheetDialog.setCancelable(true);
//        DisplayMetrics metrics = HomeFeedActivity.this.getResources().getDisplayMetrics();
//        int height = metrics.heightPixels;
//        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, (height * 3) / 4);
//        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
//        mBottomSheetDialog.show();
//
//
//        TextView txt_cancel = (TextView) mBottomSheetDialog.findViewById(R.id.txt_cancel);
//        TextView txt_createproject = (TextView) mBottomSheetDialog.findViewById(R.id.txt_createproject);
//        TextView txtworkSetting = (TextView) mBottomSheetDialog.findViewById(R.id.txtworkSetting);
//        TextView createCategory = (TextView) mBottomSheetDialog.findViewById(R.id.createCategory);
////        TextView txtlocation = (TextView) mBottomSheetDialog.findViewById(R.id.txtlocation);
//        createCategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeFeedActivity.this, CategoryListingActivity.class);
//                intent.putExtra("id", workid);
//                intent.putExtra("name", workname);
//                startActivity(intent);
//                mBottomSheetDialog.dismiss();
//            }
//        });
//
//        txt_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mBottomSheetDialog.dismiss();
//            }
//        });
//        txt_createproject.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeFeedActivity.this, ProjectSettingActivity.class);
//                intent.putExtra("id", workid);
//                startActivity(intent);
//                mBottomSheetDialog.dismiss();
//            }
//        });
//        txtworkSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeFeedActivity.this, WorkspaceSettingActivity.class);
//
//                startActivity(intent);
//
//                mBottomSheetDialog.dismiss();
//            }
//        });
//
//    }
    private void opensettingbottomshet() {
        final String workid = AppConstants.loadPreferences(HomeFeedActivity.this, "workid");
        final String workname = AppConstants.loadPreferences(HomeFeedActivity.this, "workname");
        ActionSheet actionSheet = new ActionSheet.Builder()
                // .setTitle("Title", Color.BLUE)
                //.setTitleTextSize(20)
                .setOtherBtn(new String[]{"Workspace Settings", "Category Settings", "Project Settings", "Notification Settings"}, new int[]{Color.parseColor("#2dc8bc"), Color.parseColor("#2dc8bc"),
                        Color.parseColor("#2dc8bc"),
                        Color.parseColor("#2dc8bc")})

                //.setOtherBtnSubTextSize(20)
                .setCancelBtn("Cancel", Color.parseColor("#2dc8bc"))
                //.setCancelBtnTextSize(30)
                .setCancelableOnTouchOutside(true)
                .setActionSheetListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isByBtn) {
                        // Toast.makeText(HomeFeedActivity.this, "onDismiss: " + isByBtn, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onButtonClicked(ActionSheet actionSheet, int index) {
                        if (index == 0) {
                            Intent intent = new Intent(HomeFeedActivity.this, WorkspaceSettingActivity.class);
                            startActivity(intent);
                        }
                        if (index == 1) {

                            Intent intent = new Intent(HomeFeedActivity.this, CategoryListingActivity.class);
                            intent.putExtra("id", workid);
                            intent.putExtra("name", workname);
                            startActivity(intent);
                        }
                        if (index == 2) {

                            Intent intent = new Intent(HomeFeedActivity.this, ProjectSettingActivity.class);
                            intent.putExtra("id", workid);
                            startActivity(intent);
                        }


                        //  Toast.makeText(HomeFeedActivity.this, "onButtonClicked: " + index, Toast.LENGTH_SHORT).show();
                    }
                }).build();

        actionSheet.show(getFragmentManager());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1111) {
            File folder = new File(Environment.getExternalStorageDirectory(), "/Sounds");
            long folderModi = folder.lastModified();

            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return (name.endsWith("3gp"));
                }
            };

            File[] folderList = folder.listFiles(filter);

            String recentName = "";

            for (int i = 0; i < folderList.length; i++) {
                long fileModi = folderList[i].lastModified();

                if (folderModi == fileModi) {
                    recentName = folderList[i].getName();
                }
            }

            Log.e("FILANMEEeee", recentName);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //  workList.clear();

        if (NetworkAvailablity.chkStatus(HomeFeedActivity.this)) {
//            getuserdata();
//            getworkspaceList();
            //


//            getcatogory();
//            getuncatproject();
            //getfeedpost();
        } else {
            Toast.makeText(HomeFeedActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getuncatproject() {

        project_expandableList.setVisibility(View.GONE);
        img_progrss_projct.setVisibility(View.VISIBLE);

        parentBeans.clear();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");

        query.whereEqualTo("workspace", ParseObject.createWithoutData("WorkSpace", AppConstants.loadPreferences(HomeFeedActivity.this, "workid")));
//        query.whereEqualTo("category", ParseObject.createWithoutData("Category", objectId));
        query.include("category");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    try {


                        for (int i = 0; i < objects.size(); i++) {
                            String strdefault = objects.get(i).getString("default");


                            if (!objects.get(i).containsKey("category")) {

                                String objectId = objects.get(i).getObjectId();
                                String name = objects.get(i).getString("name");
                                String updatedAt = objects.get(i).getString("updatedAt");
                                String workspaceID = objects.get(i).getString("workspaceID");
                                String objective = objects.get(i).getString("objective");
                                String createdAt = objects.get(i).getString("createdAt");
                                String image = objects.get(i).getString("image");
                                String type = objects.get(i).getString("type");

                                Log.e("CATEGRORY", "d>>>>>" + objectId);
                                //  parentBeans.add(new ParentBean(objectId, name, type, strdefault, childBeans));
                            }
//
                        }
//                        projectAdapter.notifyDataSetChanged();

                    } catch (NullPointerException ea) {
                        ea.printStackTrace();
                    }
                } else {
                    Toast.makeText(HomeFeedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.e("lse", e.getMessage());
                    // error
                }
                //getcatogory();


            }
        });

    }

    public void getcatogory() {


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
        query.whereEqualTo("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));

        query.whereEqualTo("workspaceID", ParseObject.createWithoutData("WorkSpace", AppConstants.loadPreferences(HomeFeedActivity.this, "workid")));
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {

                Log.e("CategoryAMANANA", "d>>>>>" + objects.size());
                if (e == null) {
                    try {

                        String strdefault = "";
                        for (int i = 0; i < objects.size(); i++) {
                            //  childBeans = new ArrayList<>();


                            String cat_name = objects.get(i).getString("name");
                            String cat_id = objects.get(i).getObjectId();
                            JSONArray jsonArray = objects.get(i).getJSONArray("Projects");
//                            Log.e("CategoryAMANANA", "d>>>>>" + jsonArray.length());
                            for (int a = 0; a < jsonArray.length(); a++) {
                                //Log.e("CategoryAMANANA", "d>>>>>" + jsonArray.toString());
                                childBeans = new ArrayList<>();

                                JSONArray jsonArray1 = jsonArray.getJSONArray(a);

                                for (int b = 0; b < jsonArray1.length(); b++) {
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(b);
                                    String name = jsonObject1.getString("name");
                                    String catProjectobjectId = jsonObject1.getString("objectId");
                                    Log.e("CategoryAMANANA", "d>>>>>" + catProjectobjectId);
                                    String objective = jsonObject1.getString("objective");
                                    String createdAt = jsonObject1.getString("createdAt");
                                    String updatedAt = jsonObject1.getString("updatedAt");

                                    String type = jsonObject1.getString("type");
                                    Log.e("Project_IDDSs_12", catProjectobjectId);
                                    childList.add(catProjectobjectId);

                                    if (objects.get(i).has("default")) {
                                        strdefault = objects.get(i).getString("default");


                                        //  categoriesProjectbeanclasses.add(new CategoriesProjectbeanclass(name, objective, createdAt, updatedAt, catProjectobjectId, type));

                                        childBeans.add(new ChildBean(catProjectobjectId, name, updatedAt,
                                                objective, createdAt, type, strdefault));
                                    } else {
                                        //  categoriesProjectbeanclasses.add(new CategoriesProjectbeanclass(name, objective, createdAt, updatedAt, catProjectobjectId, type));


                                        childBeans.add(new ChildBean(catProjectobjectId, name, updatedAt,
                                                objective, createdAt, type, strdefault));
                                    }


                                }
                            }

//                            getprojectlist(objectId,cat_name,cat_id);

                            parentBeans.add(new ParentBean(cat_id, cat_name, "", "", "", childBeans));
                        }


                    } catch (NullPointerException ea) {
                        ea.printStackTrace();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    Toast.makeText(HomeFeedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                    Log.e("lse", e.getMessage());
                    // error


                }
//                d >>>>>LPH07qWwlr
//                d >>>>>n6mgFIKVta
                Log.e("parentBeansHHH", childBeans.size() + "");
                Log.e("childListAAAA", childList.size() + "");

                for (int a = 0; a < childList.size(); a++) {

                    if (childList.contains(parentBeans.get(a).getCategoryId())) {

                        Log.e("INLOOOP", parentBeans.get(a).getCategoryId() + "");

                        parentBeans.remove(a);
                    }
                }

                projectAdapter = new MenuProjectAdapter(HomeFeedActivity.this,
                        projectList, expandableListDetail, parentBeans, project_expandableList);
                project_expandableList.setAdapter(projectAdapter);
                projectAdapter.notifyDataSetChanged();


                project_expandableList.setVisibility(View.VISIBLE);
                img_progrss_projct.setVisibility(View.GONE);


            }
        });

    }

    private void getworkspaceList() {
        dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)

                .fadeColor(Color.DKGRAY).build();
        dialog.show();
        workList = new ArrayList<>();
        workList.clear();
        //demolist.clear();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("WorkSpace");
        query.whereEqualTo("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {


                if (e == null) {

                    workList.clear();
                    for (int i = 0; i < objects.size(); i++) {
                        String objectId = objects.get(i).getObjectId();
//
//                        if (i == 0) {
//
//                            AppConstants.savePreferences(HomeFeedActivity.this, "workid", objectId);
//                        }
//

                        ParseObject user = ParseObject.createWithoutData("WorkSpace", "user");

                        String mission = objects.get(i).getString("mission");
                        String updatedAt = objects.get(i).getUpdatedAt().toString();
                        String workspace_n = objects.get(i).getString("workspace_name");
                        String createdAt = objects.get(i).getCreatedAt().toString();
//                        String image = objects.get(i).getString("image");
                        ParseFile image = (ParseFile) objects.get(i).get("image");
                        String ws_image = image.getUrl();
                        String user_name = objects.get(i).getString("user_name");
                        String workspace_url = objects.get(i).getString("workspace_url");


                        workList.add(new WorkspaceBean(objectId, user, mission, updatedAt, workspace_n, createdAt, image, ws_image, user_name, workspace_url));


                    }
                    String w_id = AppConstants.loadPreferences(HomeFeedActivity.this, "workid");

                    Log.e("MYWORKID", w_id + "");
                    for (int a = 0; a < workList.size(); a++) {
                        if (w_id.equals("00")) {

                            if (a == 0) {


                                workspace_name.setText(First_Char_Capital.capitalizeString(workList.get(a).getWorkspace_name()));
                                work_txt.setText(First_Char_Capital.capitalizeString(workList.get(a).getWorkspace_name()));
                                work_title.setText(First_Char_Capital.capitalizeString(workList.get(a).getWorkspace_name()));

                                //  work_name.setText(First_Char_Capital.capitalizeString(workList.get(a).getWorkspace_name()));
                                ParseFile parseFile = workList.get(a).getImage();
                                String newimage = parseFile.getUrl();
                                if (newimage != null) {

                                    Picasso.with(HomeFeedActivity.this)
                                            .load(newimage)
                                            .fit().placeholder(R.mipmap.sign_up_workspace)

                                            .centerCrop()

                                            .into(work_space_img);
                                    Picasso.with(HomeFeedActivity.this)
                                            .load(newimage)

                                            .fit()
                                            .centerCrop()

                                            .into(imgMenuIcon);
                                }
                                work_space_img.getLayoutParams().height = img_height;
                            }
                            AppConstants.savePreferences(HomeFeedActivity.this, "workid", workList.get(a).getObjectId());
                            AppConstants.savePreferences(HomeFeedActivity.this, "workname", workList.get(a).getWorkspace_url());

                        } else if (workList.get(a).getObjectId().equals(w_id)) {
//                            AppConstants.savePreferences(HomeFeedActivity.this, "workid", workList.get(a).getObjectId());
//                            AppConstants.savePreferences(HomeFeedActivity.this, "workname", workList.get(a).getWorkspace_url());


                            AppConstants.savePreferences(HomeFeedActivity.this, "position", "" + a);
                            //     int pos = Integer.parseInt(AppConstants.loadPreferences(HomeFeedActivity.this, "position"));
                            Log.e("MYWORKIDPOSTIO", a + "");
                            workspace_name.setText(First_Char_Capital.capitalizeString(workList.get(a).getWorkspace_name()));
                            work_txt.setText(First_Char_Capital.capitalizeString(workList.get(a).getWorkspace_name()));
                            work_title.setText(First_Char_Capital.capitalizeString(workList.get(a).getWorkspace_name()));
                            ParseFile parseFile = workList.get(a).getImage();
                            String newimage = parseFile.getUrl();
                            if (newimage != null) {

                                Picasso.with(HomeFeedActivity.this)
                                        .load(newimage)
                                        .fit()
                                        .centerCrop()

                                        .into(work_space_img);
                                Picasso.with(HomeFeedActivity.this)
                                        .load(newimage)
                                        .fit()
                                        .centerCrop()

                                        .into(imgMenuIcon);
                            }
                            work_space_img.getLayoutParams().height = img_height;
                        }
                    }

//                    ArrayList<WorkspaceBean> tempElements = new ArrayList<WorkspaceBean>(workList);
//                    Collections.reverse(tempElements);
                    adapter = new WorkspaceAdapter(HomeFeedActivity.this, workList, workspace_name);

                    listView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HomeFeedActivity.this);
                    listView.setLayoutManager(layoutManager);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(HomeFeedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                }


            }
        });

    }


    private void getuserdata() {

        // userName.setText(ParseUser.getCurrentUser().getUsername());
        if (ParseUser.getCurrentUser().getParseFile("profileimage") != null) {
            ParseUser.getCurrentUser().getParseFile("profileimage").getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    Bitmap bmp = BitmapFactory
                            .decodeByteArray(
                                    data, 0,
                                    data.length);

                    // initialize
                    mypic.setImageBitmap(bmp);
                }
            });
        }


    }

//    public void getworkspace(final String s) {
//        // Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
//
//
//        final ProgressDialog dialog = AppConstants.showProgressDialog(HomeFeedActivity.this, "Loading...");
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("WorkSpace");
//        query.whereEqualTo("objectId", AppConstants.loadPreferences(HomeFeedActivity.this, "workid"));
//
//        query.findInBackground(new FindCallback<ParseObject>() {
//            public void done(List<ParseObject> objects1, ParseException e) {
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//
//
//                if (e == null) {
//
//
//                    if (objects1.size() > 0) {
//
//                        for (int i = 0; i < objects1.size(); i++) {
//
//
//                            String mission = objects1.get(i).getString("mission");
//                            String updatedAt = objects1.get(i).getUpdatedAt().toString();
//                            String workspace_n = objects1.get(i).getString("workspace_name");
//
//                            String createdAt = objects1.get(i).getCreatedAt().toString();
//                            String image = objects1.get(i).getString("image");
//                            ParseFile imageFile = (ParseFile) objects1.get(i).get("image");
//                            String post_image = imageFile.getUrl();
//                            String user_name = objects1.get(i).getString("user_name");
//                            Log.e("imageFile", imageFile + "");
//                            //  AppConstants.savePreferences(HomeFeedActivity.this, "workname", workspace_n);
//                            workspace_name.setText(First_Char_Capital.capitalizeString(workspace_n));
//                            work_txt.setText(First_Char_Capital.capitalizeString(mission));
//                            work_name.setText(First_Char_Capital.capitalizeString(workspace_n));
//                            if (imageFile != null) {
//
//                                Picasso.with(HomeFeedActivity.this)
//                                        .load(post_image)
//                                        .fit()
//                                        .centerCrop()
//
//                                        .into(work_space_img);
//                                Picasso.with(HomeFeedActivity.this)
//                                        .load(post_image)
//                                        .fit()
//                                        .centerCrop()
//
//                                        .into(imgMenuIcon);
//                            }
//                            work_space_img.getLayoutParams().height = img_height;
//
//
//                        }
//                    }
//
//
//                } else {
//                    Toast.makeText(HomeFeedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                    Log.e("else", e.getMessage());
//                    // error
//                }
//
//
//            }
//        });
//    }

    public void getprojectlist() {
        childBeans = new ArrayList<>();
        childList.clear();
        expandableListDetail.clear();
        parentBeans.clear();
        projectList.clear();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");

        query.whereEqualTo("workspace", ParseObject.createWithoutData("WorkSpace", AppConstants.loadPreferences(HomeFeedActivity.this, "workid")));

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    try {
                        //  childBeans = new ArrayList<>();
                        // parentBeans.clear();
//                    Log.e("workspaceID", "d>>>>>" + AppConstants.loadPreferences(HomeFeedActivity.this, "workid"));
                        Log.e("ProjectDATA", "d>>>>>" + objects.size());
                        for (int i = 0; i < objects.size(); i++) {

                            String strdefault = "";
                            String objectId = objects.get(i).getObjectId();
                            String name = objects.get(i).getString("name");
                            String updatedAt = objects.get(i).getString("updatedAt");
                            String workspaceID = objects.get(i).getString("workspaceID");
                            String objective = objects.get(i).getString("objective");
                            String createdAt = objects.get(i).getString("createdAt");
                            ParseFile ws_image = (ParseFile) objects.get(i).get("image");
                            String image = ws_image.getUrl();
//                            String image = objects.get(i).getString("image");
                            String type = objects.get(i).getString("type");
                            if (objects.get(i).has("default")) {
                                strdefault = objects.get(i).getString("default");
                            }

                            Log.e("Project_IDDSs", name + ">>>>>>>>>" + objectId);

//                            if (!childList.contains(objectId)) {
                            parentBeans.add(new ParentBean(objectId, name, type, strdefault, image, childBeans));
//                            }

                        }

                        projectAdapter = new MenuProjectAdapter(HomeFeedActivity.this,
                                projectList, expandableListDetail, parentBeans, project_expandableList);
                        project_expandableList.setAdapter(projectAdapter);
                        projectAdapter.notifyDataSetChanged();


                        project_expandableList.setVisibility(View.VISIBLE);
                        img_progrss_projct.setVisibility(View.GONE);


                    } catch (NullPointerException ea) {
                        ea.printStackTrace();
                    }
                } else {
                    Toast.makeText(HomeFeedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.e("lse", e.getMessage());
                    // error
                }
//                getcatogory();

            }
        });

    }

    public void getfeedpost() {
        postList.clear();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        if (!AppConstants.ALL) {
            query.whereEqualTo("project", ParseObject.createWithoutData("Project", AppConstants.loadPreferences(HomeFeedActivity.this, "projectID")));
            //Toast.makeText(this, "project", Toast.LENGTH_SHORT).show();
            Log.e("worksap_projectID", AppConstants.loadPreferences(HomeFeedActivity.this, "projectID"));
        } else {
            //  Toast.makeText(this, "work", Toast.LENGTH_SHORT).show();
            query.whereEqualTo("workspace", ParseObject.createWithoutData("WorkSpace", AppConstants.loadPreferences(HomeFeedActivity.this, "workid")));
            Log.e("worksapc_WORKSPACe", AppConstants.loadPreferences(HomeFeedActivity.this, "workid"));

        }
        query.orderByAscending("createdAt");

        query.include("user");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    Log.e("FEEDPOST", objects.toString() + "");

                    postList.clear();
                    for (int i = 0; i < objects.size(); i++) {
                        String post_image = "no_image";

                        ParseObject parseObject = objects.get(i).getParseObject("user");

                        String username = parseObject.getString("fullname");
                        ParseFile user_imge = (ParseFile) parseObject.get("profileimage");

                        String post_userID = parseObject.getObjectId();
                        // Log.e("poster_name", username);
                        String text = objects.get(i).getString("text");
                        String objectId = objects.get(i).getObjectId();

                        String updatedAt = objects.get(i).getUpdatedAt().toString();
                        String createdAt = objects.get(i).getCreatedAt().toString();
                        if (objects.get(i).has("postImage")) {
                            ParseFile image = (ParseFile) objects.get(i).get("postImage");
                            post_image = image.getUrl();
                        }
                        String post_type = objects.get(i).getString("post_type");

                        String tagUserId = objects.get(i).getString("tagUserId");
                        String post_file_url = objects.get(i).getString("post_file_url");
                        String post_file = "";
                        if (objects.get(i).has("post_file")) {
                            ParseFile file = (ParseFile) objects.get(i).get("post_file");
                            post_file = file.getUrl();
                        }
                        String userTag = objects.get(i).getString("userTag");
                        int CommentCount = objects.get(i).getInt("CommentCount");
                        int likesCount = objects.get(i).getInt("likesCount");
                        postList.add(new ShowPostBean(username, user_imge, text, objectId, updatedAt, createdAt, post_image, post_type, tagUserId, post_file_url, userTag, CommentCount, likesCount, post_userID, post_file));

                    }
                    Collections.reverse(postList);

                    FeedPostAdapter adapter = new FeedPostAdapter(HomeFeedActivity.this, postList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HomeFeedActivity.this);
                    Rv_feed_post.setHasFixedSize(true);

                    Rv_feed_post.setLayoutManager(layoutManager);
                    Rv_feed_post.addItemDecoration(new SimpleDividerItemDecoration(HomeFeedActivity.this));
                    Rv_feed_post.setAdapter(adapter);
                    Rv_feed_post.invalidate();
                    adapter.notifyDataSetChanged();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    postList.clear();
                    Toast.makeText(HomeFeedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.e("else_post", e.getMessage());
                    // error
                }
            }
        });
    }

    private void findview() {
        mRecorder = new MediaRecorder();
        postList = new ArrayList<>();
        projectList = new ArrayList<>();
        expandableListDetail = new HashMap<>();
        expandableListDetail.clear();
        childList = new ArrayList<>();
        childList.clear();
        projectList.clear();
        scrollView = (NestedScrollView) findViewById(R.id.scrollView_feed);
        marshMallowPermission = new MarshMallowPermission(HomeFeedActivity.this);
        rl_audioRecord = (RelativeLayout) findViewById(R.id.Rl_audioRecord);
        categoriesProjectbeanclasses = new ArrayList<>();
        postList.clear();
        customFonts = new CustomFonts(HomeFeedActivity.this);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        btn_recordVoice = (Button) findViewById(R.id.btn_RecorVoice);
        btn_camera = (Button) findViewById(R.id.btn_camera);
        workList = new ArrayList<>();
        workList.clear();
        img_progrss_projct = (ProgressBar) findViewById(R.id.img_progrss_projct);
        project_expandableList = (ExpandableListView) findViewById(R.id.project_expandableList);
        btn_addworkspace = (ImageView) findViewById(R.id.btn_addworkspace);
        img_mic = (ImageView) findViewById(R.id.img_mic);
        btn_text = (Button) findViewById(R.id.btn_text);
        listView = (RecyclerView) findViewById(R.id.listView);
        listView.setNestedScrollingEnabled(false);
        img_wrokspace = (ImageView) findViewById(R.id.img_wrokspace);
        imgMenuIcon = (CircleImageView) findViewById(R.id.imgMenuIcon);
        work_space_img = (ImageView) findViewById(R.id.work_space_img);
        imgWorkSpaceSetting = (ImageView) findViewById(R.id.imgWorkSpaceSetting);
        Rv_feed_post = (RecyclerView) findViewById(R.id.Rv_feed_post);
        work_title = (TextView) findViewById(R.id.work_title);
        work_txt = (TextView) findViewById(R.id.work_txt);
        txt_all = (TextView) findViewById(R.id.txt_all);
        workspace_name = (TextView) findViewById(R.id.workspace_name);
        Rv_feed_post.setNestedScrollingEnabled(false);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        img_log = (ImageView) findViewById(R.id.img_log);
        mypic = (ImageView) findViewById(R.id.mypic);
        img_addpost = (ImageView) findViewById(R.id.img_addpost);
        arcLayout = (ArcLayout) findViewById(R.id.arc_layout);
        menuLayout = findViewById(R.id.menu_layout);
        // work_name.setTypeface(customFonts.CabinBold);
        workspace_name.setTypeface(customFonts.CabinBold);
        work_title.setTypeface(customFonts.CabinBold);
        work_txt.setTypeface(customFonts.CabinRegular);

    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure to exit?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                startActivity(intent);
                finish();
                System.exit(0);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_addpost: {

                onFabClick(v);
                break;
            }
        }
    }

    private void onFabClick(View v) {
        if (v.isSelected()) {
            hideMenu();
        } else {
            showMenu();
        }
        v.setSelected(!v.isSelected());
    }

    private void showMenu() {
        menuLayout.setVisibility(View.VISIBLE);

        List<Animator> animList = new ArrayList<>();

        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
            animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(500);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animList);
        animSet.start();
    }

    private Animator createShowItemAnimator(View item) {

        float dx = img_addpost.getX() - item.getX();
        float dy = img_addpost.getY() - item.getY();

        item.setRotation(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(0f, 720f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );

        return anim;
    }

    private void hideMenu() {

        List<Animator> animList = new ArrayList<>();

        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(500);
        animSet.setInterpolator(new AnticipateInterpolator());
        animSet.playTogether(animList);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });
        animSet.start();

    }

    private Animator createHideItemAnimator(final View item) {
        float dx = img_addpost.getX() - item.getX();
        float dy = img_addpost.getY() - item.getY();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(720f, 0f),
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)
        );

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });

        return anim;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

}
