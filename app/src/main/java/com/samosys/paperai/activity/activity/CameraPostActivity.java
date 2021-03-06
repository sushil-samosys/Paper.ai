/*
 * Copyright (C) 2016 The Android Open Source Project
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
 */

package com.samosys.paperai.activity.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.android.cameraview.AspectRatio;
import com.google.android.cameraview.CameraView;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.AspectRatioFragment;
import com.samosys.paperai.activity.utils.CircularProgressBar;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.MarshMallowPermission;
import com.samosys.paperai.activity.utils.SimpleTooltip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.github.memfis19.annca.Annca;
import io.github.memfis19.annca.internal.configuration.AnncaConfiguration;


/**
 * This demo app saves the taken picture to a constant file.
 * $ adb pull /sdcard/Android/data/com.google.android.cameraview.demo/files/Pictures/picture.jpg
 */
public class CameraPostActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback,
        AspectRatioFragment.Listener {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String FRAGMENT_DIALOG = "dialog";
    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_ON,
    };
    private static final int REQUEST_CAMERA_PERMISSIONS = 931;
    private static final int[] FLASH_ICONS = {
            R.mipmap.home_camera,
            R.mipmap.home_camera,
            R.mipmap.home_camera,
    };
    private static final int[] FLASH_TITLES = {
            R.string.flash_auto,
            R.string.flash_off,
            R.string.flash_on,
    };
    private static final int CAPTURE_MEDIA = 368;

    private MarshMallowPermission marshMallowPermission;
    private AQuery aq;
    private CustomFonts customFonts;
    private CircularProgressBar circularProgressbar_vid;
    private TextView txtNext, txtHeader;
    private File imageFile = new File("");
    private int SELECT_FILE = 1;
    private ImageView capturedImage, imgback_camera;
    private RelativeLayout rl_bottom_capture, linbar;
    private int mCurrentFlash;
    private ImageView take_picture, img_switchcam, img_gallary;
    private CameraView mCameraView;
    private Handler mBackgroundHandler;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.circularProgressbar_vid:
                    if (mCameraView != null) {
                        mCameraView.takePicture();
                        mCameraView.setVisibility(View.GONE);
                        rl_bottom_capture.setVisibility(View.GONE);
                        capturedImage.setVisibility(View.GONE);
                    }
                    break;

            }
        }
    };
    private CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {


            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "picture.jpg");

                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(file);
                        os.write(data);
                        os.close();
                    } catch (IOException e) {
                        Log.w(TAG, "Cannot write to " + file, e);
                    } finally {
                        if (os != null) {
                            try {
                                os.close();
                            } catch (IOException e) {
                                // Ignore
                            }
                        }
                    }
                    imageFile = file;
                    Intent intent = new Intent(CameraPostActivity.this, RecordAudioActivity.class);

                    intent.putExtra("file", imageFile.toString());

                    startActivity(intent);
                    finish();
//


                }
            });
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        mCameraView = (CameraView) findViewById(R.id.camera);
        aq = new AQuery(CameraPostActivity.this);
        marshMallowPermission = new MarshMallowPermission(CameraPostActivity.this);
        capturedImage = (ImageView) findViewById(R.id.capturedImage);
        customFonts = new CustomFonts(CameraPostActivity.this);
        circularProgressbar_vid = (CircularProgressBar) findViewById(R.id.circularProgressbar_vid);
        txtNext = (TextView) findViewById(R.id.txtNext);
        txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setTypeface(customFonts.CabinBold);
        txtNext.setTypeface(customFonts.calibri);
        rl_bottom_capture = (RelativeLayout) findViewById(R.id.rl_bottom_capture);

        take_picture = (ImageView) findViewById(R.id.take_picture);
        img_switchcam = (ImageView) findViewById(R.id.img_switchcam);
        imgback_camera = (ImageView) findViewById(R.id.imgback_camera);
        img_gallary = (ImageView) findViewById(R.id.img_gallary);


        new SimpleTooltip.Builder(this)
                .anchorView(circularProgressbar_vid)
                .text("Tap to take a picture or press hold to record video")
                .gravity(Gravity.TOP)

                .animated(true)
                .build()
                .show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        String workname = AppConstants.loadPreferences(CameraPostActivity.this, "workname");
        txtHeader.setText(workname);
        img_switchcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {

                        selectImage();
                    }
                } else {

                    selectImage();
                }

            }
        });

        img_gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                askForCameraPermission();
            }
        });


        circularProgressbar_vid.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public boolean onLongClick(View v) {
                AnncaConfiguration.Builder videoLimited = new AnncaConfiguration.Builder(CameraPostActivity.this, CAPTURE_MEDIA);
                videoLimited.setMediaAction(AnncaConfiguration.MEDIA_ACTION_VIDEO);
                videoLimited.setMediaQuality(AnncaConfiguration.MEDIA_QUALITY_AUTO);
                videoLimited.setVideoFileSize(5 * 1024 * 1024);
                videoLimited.setMinimumVideoDuration(60000);

                new Annca(videoLimited.build()).launchCamera();
                return true;
            }
        });

        imgback_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                finish();
            }
        });

        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("imageFile", "imageFile==" + imageFile);
                if (imageFile.exists()) {
                    Intent intent = new Intent(CameraPostActivity.this, RecordAudioActivity.class);
                    intent.putExtra("file", imageFile.toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(CameraPostActivity.this, "Please capture photo", Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (Build.VERSION.SDK_INT > 15) {
            askForPermissions(new String[]{
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.RECORD_AUDIO,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_PERMISSIONS);
        }
    }

    private void selectImage() {
        if (mCameraView != null) {
            int facing = mCameraView.getFacing();
            mCameraView.setFacing(facing == CameraView.FACING_FRONT ?
                    CameraView.FACING_BACK : CameraView.FACING_FRONT);
        }
    }

    protected final void askForPermissions(String[] permissions, int requestCode) {
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), requestCode);
        }
    }

    public void askForCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                marshMallowPermission.requestPermissionForExternalStorage();
            } else {

                opengallary();
            }
        } else {

            opengallary();
        }
    }

    private void opengallary() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Select File"),
                SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                // This method is call for getting image from gallary
                onSelectFromGalleryResult(data);
                //    getdata(data);
            } else if (requestCode == CAPTURE_MEDIA) {


                String filePath = data.getStringExtra(AnncaConfiguration.Arguments.FILE_PATH);
                imageFile = new File(filePath);
                if (imageFile.exists()) {


                    Intent intent = new Intent(CameraPostActivity.this, PostfeedActivity.class);

                    intent.putExtra("file", imageFile.toString());
                    intent.putExtra("post_type", "3");

                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(CameraPostActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                Log.e("selectedImagePath", "" + filePath + "");
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {


        Uri selectedImage = data.getData();
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = getContentResolver().query(selectedImage, filePath,
                null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        String selectedImagePath = c.getString(columnIndex);
        c.close();


//        Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
        Log.e("MEW_BITMAP12", "" + selectedImagePath);


        File file = new File(selectedImagePath);
        mCameraView.setVisibility(View.GONE);
        rl_bottom_capture.setVisibility(View.GONE);
        capturedImage.setVisibility(View.VISIBLE);


        imageFile = file;
        Intent intent = new Intent(CameraPostActivity.this, RecordAudioActivity.class);

        intent.putExtra("file", imageFile.toString());

        startActivity(intent);
        finish();
//

    }


    @Override
    protected void onResume() {
        super.onResume();
        imageFile = null;


        if (circularProgressbar_vid != null) {
            circularProgressbar_vid.setOnClickListener(mOnClickListener);
        }


        if (Build.VERSION.SDK_INT > 15) {
            askForPermissions(new String[]{
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.RECORD_AUDIO,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_PERMISSIONS);
        }
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mCameraView.start();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ConfirmationDialogFragment
                    .newInstance(R.string.camera_permission_confirmation,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA_PERMISSION,
                            R.string.camera_permission_not_granted)
                    .show(getSupportFragmentManager(), FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aspect_ratio:
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (mCameraView != null
                        && fragmentManager.findFragmentByTag(FRAGMENT_DIALOG) == null) {
                    final Set<AspectRatio> ratios = mCameraView.getSupportedAspectRatios();
                    final AspectRatio currentRatio = mCameraView.getAspectRatio();
                    AspectRatioFragment.newInstance(ratios, currentRatio)
                            .show(fragmentManager, FRAGMENT_DIALOG);
                }
                return true;
            case R.id.switch_flash:
                if (mCameraView != null) {
                    mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
                    item.setTitle(FLASH_TITLES[mCurrentFlash]);
                    item.setIcon(FLASH_ICONS[mCurrentFlash]);
                    mCameraView.setFlash(FLASH_OPTIONS[mCurrentFlash]);
                }
                return true;
            case R.id.switch_camera:
                if (mCameraView != null) {
                    int facing = mCameraView.getFacing();
                    mCameraView.setFacing(facing == CameraView.FACING_FRONT ?
                            CameraView.FACING_BACK : CameraView.FACING_FRONT);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAspectRatioSelected(@NonNull AspectRatio ratio) {
        if (mCameraView != null) {
            Toast.makeText(this, ratio.toString(), Toast.LENGTH_SHORT).show();
            mCameraView.setAspectRatio(ratio);
        }
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    public static class ConfirmationDialogFragment extends DialogFragment {

        private static final String ARG_MESSAGE = "message";
        private static final String ARG_PERMISSIONS = "permissions";
        private static final String ARG_REQUEST_CODE = "request_code";
        private static final String ARG_NOT_GRANTED_MESSAGE = "not_granted_message";

        public static ConfirmationDialogFragment newInstance(@StringRes int message,
                                                             String[] permissions, int requestCode, @StringRes int notGrantedMessage) {
            ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_MESSAGE, message);
            args.putStringArray(ARG_PERMISSIONS, permissions);
            args.putInt(ARG_REQUEST_CODE, requestCode);
            args.putInt(ARG_NOT_GRANTED_MESSAGE, notGrantedMessage);
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Bundle args = getArguments();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(args.getInt(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String[] permissions = args.getStringArray(ARG_PERMISSIONS);
                                    if (permissions == null) {
                                        throw new IllegalArgumentException();
                                    }
                                    ActivityCompat.requestPermissions(getActivity(),
                                            permissions, args.getInt(ARG_REQUEST_CODE));
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(),
                                            args.getInt(ARG_NOT_GRANTED_MESSAGE),
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                    .create();
        }

    }
}
